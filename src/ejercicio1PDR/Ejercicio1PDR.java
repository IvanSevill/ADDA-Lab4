package ejercicio1PDR;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import common.DatosAlmacenes;
import common.Spm;
import ejercicio1.AlmacenVertex;
import ejercicio1.SolucionAlmacen;
import us.lsi.common.List2;
import us.lsi.common.Map2;

public class Ejercicio1PDR {
	public static Map<AlmacenVertex, Spm> memory;
	public static Integer mejorValor;

	public static SolucionAlmacen search() {
		memory = Map2.empty();
		mejorValor = Integer.MIN_VALUE; // Estamos maximizando
		pdr_search(AlmacenVertex.initial(), 0, memory);
		return getSolucion();
	}

	public static Spm pdr_search(AlmacenVertex problem, Integer acum, Map<AlmacenVertex, Spm> memory) {
		Spm res = null;
		Boolean esTerminal = problem.indice().equals(DatosAlmacenes.getNumProductos());

		if (memory.containsKey(problem)) {
			res = memory.get(problem);

		} else if (esTerminal) {
			res = Spm.of(null, 0);
			memory.put(problem, res);
			if (acum > mejorValor) { // Estamos maximizando
				mejorValor = acum;
			}

		} else {
			List<Spm> soluciones = List2.empty();

			for (Integer action : problem.actions()) {
				Double cota = acotar(acum, problem, action); // poda
				if (cota < mejorValor) { // Si la mejor cota posible no supera el mejor valor → descartar
					continue;
				}

				AlmacenVertex vecino = problem.neighbor(action); // siguiente estado
				int incremento = action >= 0 ? 1 : 0; // si se almacena el producto, aumenta el acumulado
				Spm s = pdr_search(vecino, acum + incremento, memory);

				if (s != null) {
					Spm ampliado = Spm.of(action, s.weight() + incremento);
					soluciones.add(ampliado);
				}
			}

			// Maximizar
			res = soluciones.stream().max(Comparator.naturalOrder()).orElse(null);
			if (res != null)
				memory.put(problem, res);
		}

		return res;
	}

	public static Double acotar(Integer acum, AlmacenVertex p, Integer a) {
	    int incremento = (a >= 0) ? 1 : 0; // si se almacena el producto
	    AlmacenVertex vecino = p.neighbor(a);
	    return acum + incremento + vecino.heuristic();
	}

	public static SolucionAlmacen getSolucion() {
	    List<Integer> acciones = List2.empty();
	    AlmacenVertex prob = AlmacenVertex.initial(); // Debes tener este método bien definido
	    Spm spm = memory.get(prob);

	    while (spm != null && spm.a() != null) {
	        acciones.add(spm.a());
	        prob = prob.neighbor(spm.a());
	        spm = memory.get(prob);
	    }

	    return SolucionAlmacen.create(acciones);
	}


}
