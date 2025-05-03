package ejercicio1Manual;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import common.DatosAlmacenes;
import common.Spm;
import ejercicio1.SolucionAlmacen;
import us.lsi.common.List2;
import us.lsi.common.Map2;

public class AlmacenPDR {

	public static Map<AlmacenProblem, Spm> memory;
	public static Integer mejorValor;

	// Método general
	public static SolucionAlmacen search() {
		memory = Map2.empty();
		mejorValor = Integer.MIN_VALUE; // Estamos maximizando

		pdr_search(AlmacenProblem.initial(), 0, memory);
		return getSolucion();
	}

	// Explicacion extensa para ayudar a la comprensión
	private static Spm pdr_search(AlmacenProblem prob, Integer acumulado, Map<AlmacenProblem, Spm> memoria) {

		Spm res = null;
		Boolean esTerminal = prob.goal();
		Boolean esSolucion = prob.goalHasSolution();

		// Si ya tengo la solucion de ese vertice devuelvo el Spm
		if (memory.containsKey(prob)) {
			res = memory.get(prob);

			// En caso de que esa solución no exista pero sea un vertice de la goal, añado
			// al diccionario el problema actual y un spm con null y peso 0, que usaremos
			// mas adelante para discernir entre mejores y peores soluciones
		} else if (esTerminal && esSolucion) {
			res = Spm.of(null, 0);
			memory.put(prob, res);

			// Estamos maximizando
			if (acumulado > mejorValor) {
				mejorValor = acumulado;
			}

			// Caso generico de que sea un vertice intermedio
		} else {
			List<Spm> soluciones = List2.empty();
			for (Integer action : prob.actions()) {
				Double cota = acotar(acumulado, prob, action);

				// Si la cota es menor que el mejor valor encontrado, ni siquiera considero esa
				// posible solución porque sé que aunque use la cota no voy a llegar a un valor
				// mejor
				if (cota < mejorValor) { // Estamos maximizando
					continue; // Continue sigue el bucle pero por la siguiente iteracion
				}

				AlmacenProblem vecino = prob.neighbor(action);
				
				// Si añado el producto, y el indice es menor que el número de productos,
				// entonces lo uso
				Integer espacioRequerido = 0;
				if (action >= 0 && prob.indice() < DatosAlmacenes.getNumProductos()) {
					espacioRequerido = 1;
				}

				// RECURSIVIDAD, busco el Spm del siguiente estado. Uso el vecino, el valor
				// acumulado del estado anterior más el espacio del nuevo producto (en el caso
				// de que lo añada) y la memoria
				Spm s = pdr_search(vecino, acumulado + espacioRequerido, memory);

				// Si el valor encontrado no es null, es decir, que existe una posible solución,
				// la añado a la lista de soluciones
				if (s != null) {
					Spm amp = Spm.of(action, s.weight() + action);
					soluciones.add(amp);
				}
			}

			// Me quedo con la mejor solución
			res = soluciones.stream().max(Comparator.naturalOrder()).orElse(null); // Estamos maximizando

			// En el caso de que la mejor solución siga siendo null, entonces añado al
			// diccionario el problema con ese Spm de null y 0
			if (res != null)
				memory.put(prob, res);
		}

		return res;
	}

	// La cota determina el volumen del problema maximo según un estado dado, es
	// decir, cuanto llevo acumulado hasta ese estado, más el valor de ese estado,
	// más la heursitica del vecino, es decir, el número máximo que puedo alcanzar
	// en caso de que se escojan todos los productos
	private static Double acotar(Integer acum, AlmacenProblem p, Integer a) {
		Integer espacioRequerido = 0;
		if (a >= 0 && p.indice() < DatosAlmacenes.getNumProductos()) {
			espacioRequerido = 1;
		}
		return acum + espacioRequerido + p.neighbor(a).heuristic();
	}

	private static SolucionAlmacen getSolucion() {
		// Empiezo con una lista vacía de acciones, el problema inicial y el Spm del
		// problema inicial
		List<Integer> acciones = List2.empty();
		AlmacenProblem prob = AlmacenProblem.initial();
		Spm spm = memory.get(prob);

		// Como indiqué antes, busco en el diccionario hasta encontrar un Spm solución
		// (el que sea null y 0). Recorremos el diccionario en busca de el camino final
		// de null y 0 y terminamos devolviendo el conjunto de acciones recorridas hasta
		// llegar al final
		while (spm != null && spm.a() != null) {
			AlmacenProblem old = prob;
			acciones.add(spm.a());
			prob = old.neighbor(spm.a());
			spm = memory.get(prob);
		}
		return SolucionAlmacen.of(acciones);
	}

}
