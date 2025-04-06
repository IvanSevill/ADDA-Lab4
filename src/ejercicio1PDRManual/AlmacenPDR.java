package ejercicio1PDRManual;

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
	public static Integer mejorValor = Integer.MIN_VALUE;

	public static SolucionAlmacen search() {
		memory = Map2.empty();
		mejorValor = Integer.MIN_VALUE; // Estamos maximizando

		pdr_search(AlmacenProblem.initial(), 0, memory);
		return getSolucion();
	}

	private static Spm pdr_search(AlmacenProblem prob, Integer acumulado, Map<AlmacenProblem, Spm> memoria) {

		Spm res = null;
		Boolean esTerminal = prob.goal();
		Boolean esSolucion = prob.goalHasSolution();

		if (memory.containsKey(prob)) {
			res = memory.get(prob);

		} else if (esTerminal && esSolucion) {
			res = Spm.of(null, 0);
			memory.put(prob, res);
			if (acumulado > mejorValor) { // Estamos maximizando
				// if (acumulado < mejorValor) { // Estamos minimizando
				mejorValor = acumulado;
			}
		} else {
			List<Spm> soluciones = List2.empty();
			for (Integer action : prob.actions()) {
				Double cota = acotar(acumulado, prob, action);
				if (cota < mejorValor) { // Estamos maximizando
					// if (cota > mejorValor) { // Estamos minimizando
					continue;
				}
				AlmacenProblem vecino = prob.neighbor(action);
				Integer espacioRequerido = 0;
				if (action >= 0 && prob.indice() < DatosAlmacenes.getNumProductos()) {
					espacioRequerido = 1;
				}
				Spm s = pdr_search(vecino, acumulado + espacioRequerido, memory);
				if (s != null) {
					Spm amp = Spm.of(action, s.weight() + action);
					soluciones.add(amp);
				}
			}
			res = soluciones.stream().max(Comparator.naturalOrder()).orElse(null); // Estamos maximizando
			// res = soluciones.stream().min(Comparator.naturalOrder()).orElse(null); //
			// Estamos minimizando

			if (res != null)
				memory.put(prob, res);
		}

		return res;
	}

	private static Double acotar(Integer acum, AlmacenProblem p, Integer a) {
		Integer espacioRequerido = 0;
		if (a >= 0 && p.indice() < DatosAlmacenes.getNumProductos()) {
			espacioRequerido = 1;
		}
		return acum + espacioRequerido + p.neighbor(a).heuristic();
	}

	public static SolucionAlmacen getSolucion() {
		List<Integer> acciones = List2.empty();
		AlmacenProblem prob = AlmacenProblem.initial();
		Spm spm = memory.get(prob);
		while (spm != null && spm.a() != null) {
			AlmacenProblem old = prob;
			acciones.add(spm.a());
			prob = old.neighbor(spm.a());
			spm = memory.get(prob);
		}
		return SolucionAlmacen.of(acciones);
	}
	

}
