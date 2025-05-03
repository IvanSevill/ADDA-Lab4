package ejercicio4Manual;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.DatosEstaciones;
import common.Spm;
import ejercicio4.SolucionEstaciones;
import us.lsi.common.List2;

public class EstacionesPDR {

	public static Map<EstacionesProblem, Spm> memory;
	public static Double mejorValor;

	public static SolucionEstaciones search() {
		memory = new HashMap<>();
		mejorValor = Double.MAX_VALUE;
		pdr_search(EstacionesProblem.initial(), 0, memory);
		return getSolution();
	}

	private static Spm pdr_search(EstacionesProblem prob, Integer acumulado, Map<EstacionesProblem, Spm> memoria) {
		Spm res = null;
		boolean esTerminal = prob.goal();
		boolean esSolucion = prob.goalHasSolution();

		if (memory.containsKey(prob)) {
			res = memory.get(prob);
		} else if (esTerminal) {
			if (esSolucion) {
				res = Spm.of(null, 0);
				memory.put(prob, res);
				if (acumulado < mejorValor) {
					mejorValor = (double) acumulado;
				}
			}
		} else {
			List<Spm> soluciones = List2.empty();
			for (Integer action : prob.actions()) {
				Double cota = acotar((double) acumulado, prob, action);
				if (cota > mejorValor) continue;

				EstacionesProblem vecino = prob.neighbor(action);
				int i = prob.indice();
				int j = vecino.indice();

				if (i < 0 || i >= DatosEstaciones.itemsNumber() || j < 0 || j >= DatosEstaciones.itemsNumber()) continue;
				if (!DatosEstaciones.existeTramo(i, j)) continue;

				Integer tiempo = (int) Math.round(DatosEstaciones.calculaTiempoMedioTramo(i, j));
				Spm s = pdr_search(vecino, acumulado + tiempo, memory);

				if (s != null) {
					Spm amp = Spm.of(action, s.weight() + tiempo);
					soluciones.add(amp);
				}
			}
			res = soluciones.stream().min(Comparator.naturalOrder()).orElse(null);
			if (res != null) memory.put(prob, res);
		}
		return res;
	}

	private static Double acotar(Double acum, EstacionesProblem p, Integer a) {
		int i = p.indice();
		int j = a;
		if (i < 0 || j < 0 || i >= DatosEstaciones.itemsNumber() || j >= DatosEstaciones.itemsNumber()) return Double.MAX_VALUE;
		if (!DatosEstaciones.existeTramo(i, j)) return Double.MAX_VALUE;
		Double tiempo = DatosEstaciones.calculaTiempoMedioTramo(i, j);
		return acum + tiempo + p.neighbor(a).heuristic();
	}

	private static SolucionEstaciones getSolution() {
		List<Integer> acciones = List2.empty();
		EstacionesProblem prob = EstacionesProblem.initial();
		Spm spm = memory.get(prob);

		while (spm != null && spm.a() != null) {
			acciones.add(spm.a());
			prob = prob.neighbor(spm.a());
			spm = memory.get(prob);
		}

		return SolucionEstaciones.of(acciones);
	}
}
