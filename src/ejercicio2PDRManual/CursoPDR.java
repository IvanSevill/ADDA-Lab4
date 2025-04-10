package ejercicio2PDRManual;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import common.DatosCursos;
import common.Spm;
import ejercicio2.SolucionCursos;
import us.lsi.common.List2;
import us.lsi.common.Map2;

public class CursoPDR {
	public static Map<CursoProblem, Spm> memory;
	public static Integer mejorValor; // Estamos maximizando

	// Método general
	public static SolucionCursos search() {
		memory = Map2.empty();
		mejorValor = Integer.MIN_VALUE;
		pdr_search(CursoProblem.initial(), 0, memory);
		return getSolution();
	}

	// Explicacion extensa para ayudar a la comprensión
	private static Spm pdr_search(CursoProblem prob, Integer acumulado, Map<CursoProblem, Spm> memoria) {

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

				CursoProblem vecino = prob.neighbor(action);
				Integer relevancia = DatosCursos.getCurso(prob.indice()).relevancia();

				// RECURSIVIDAD, busco el Spm del siguiente estado. Uso el vecino, el valor
				// acumulado del estado anterior más la relevancia del estado actual (en el caso
				// de que lo añada) y la memoria
				Spm s = pdr_search(vecino, (int) (acumulado + relevancia * action), memory);

				// Si el valor encontrado no es null, es decir, que existe una posible solución,
				// la añado a la lista de soluciones
				if (s != null) {
					Spm amp = Spm.of(action, s.weight() + relevancia * action);
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

	// La cota determina el volumen del "problema maximo" según un estado dado, es
	// decir, cuanto llevo acumulado hasta ese estado, más el valor de ese estado,
	// más la heursitica del vecino, es decir, el número máximo que puedo alcanzar
	// en caso de que se escojan todos los cursos por la relevancia.
	private static Double acotar(Integer acum, CursoProblem p, Integer a) {
		Double relevancia = p.indice() < DatosCursos.getNumCursos() ? DatosCursos.getCurso(p.indice()).relevancia()
				: 0.0;
		return acum + a * relevancia + p.neighbor(a).heuristic();
	}

	// Construyo la solución
	private static SolucionCursos getSolution() {
		// Empiezo con una lista vacía de acciones, el problema inicial y el Spm del
		// problema inicial
		List<Integer> res = List2.empty();
		CursoProblem prob = CursoProblem.initial();
		Spm spm = memory.get(prob);

		// Como indiqué antes, busco en el diccionario hasta encontrar un Spm solución
		// (el que sea null y 0). Recorremos el diccionario en busca de el camino final
		// de null y 0 y terminamos devolviendo el conjunto de acciones recorridas hasta
		// llegar al final
		while (spm != null && spm.a() != null) {
			CursoProblem before = prob;
			res.add(spm.a());
			prob = before.neighbor(spm.a());
			spm = memory.get(prob);
		}

		return SolucionCursos.of(res);
	}

}
