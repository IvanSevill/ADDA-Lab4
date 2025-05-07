package ejercicio3Manual;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import common.DatosFestival;
import common.Spm;
import ejercicio3.SolucionFestival;
import us.lsi.common.List2;
import us.lsi.common.Map2;

public class FestivalPDR {

	public static Map<FestivalProblem, Spm> memory;
	public static Integer mejorValor; // Estamos minimizando

	public static SolucionFestival search() {
		memory = Map2.empty();
		mejorValor = Integer.MAX_VALUE;
		pdr_search(FestivalProblem.initial(), 0, memory);
		return getSolution();
	}

	private static Spm pdr_search(FestivalProblem prob, Integer acumulado, Map<FestivalProblem, Spm> memoria) {
		Spm res = null;
		Boolean esTerminal = prob.goal();
		Boolean esSolucion = prob.goalHasSolution();

		if (memory.containsKey(prob)) {
			res = memory.get(prob);
		} else if (esTerminal && esSolucion) {
			res = Spm.of(null, 0);
			memory.put(prob, res);

			if (acumulado < mejorValor) {
				mejorValor = acumulado;
			}
		} else {
			List<Spm> soluciones = List2.empty();
			for (Integer action : prob.actions()) {
				Double cota = acotar(acumulado, prob, action);
				if (cota > mejorValor) { // Estamos minimizando
					continue;
				}

				int pesoActual = DatosFestival.getCosteAsignacion(prob.tipoEntradaActual(), prob.areaActual()) * action;
				FestivalProblem vecino = prob.neighbor(action);

				Spm s = pdr_search(vecino, acumulado + pesoActual, memory);
				if (s != null) {
					Spm amp = Spm.of(action, s.weight() + pesoActual);
					soluciones.add(amp);
				}
			}
			res = soluciones.stream().min(Comparator.naturalOrder()).orElse(null); // Minimizar
			if (res != null) memory.put(prob, res);
		}
		return res;
	}

	private static Double acotar(Integer acum, FestivalProblem p, Integer a) {
		int pesoActual = DatosFestival.getCosteAsignacion(p.tipoEntradaActual(), p.areaActual()) * a;
		return acum + pesoActual + p.neighbor(a).noHeuristic();
	}

	private static SolucionFestival getSolution() {
		List<Integer> acciones = List2.empty();
		FestivalProblem p = FestivalProblem.initial();
		Spm spm = memory.get(p);

		while (spm != null && spm.a() != null) {
			acciones.add(spm.a());
			p = p.neighbor(spm.a());
			spm = memory.get(p);
		}

		if (acciones.isEmpty())
			throw new IllegalStateException("La búsqueda terminó sin encontrar solución factible.");

		return SolucionFestival.create(List2.copy(acciones));
	}
}
