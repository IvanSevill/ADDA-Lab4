package ejercicio3Manual;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.DatosFestival;
import common.Spm;
import ejercicio3.SolucionFestival;
import us.lsi.common.List2;

public class FestivalPDR {

	private static Map<FestivalProblem, Spm> memory;
	private static double mejorValor;

	public static SolucionFestival search() {
		memory = new HashMap<>();
		mejorValor = Double.MAX_VALUE;

		pdr_search(FestivalProblem.initial(), 0);
		return reconstruir();
	}

	private static Spm pdr_search(FestivalProblem prob, int acumulado) {

		if (memory.containsKey(prob))
			return memory.get(prob);

		boolean esMeta = prob.goal();
		boolean esSolucion = esMeta && prob.goalHasSolution();
		Spm res = null;

		if (esSolucion) {
			res = Spm.of(null, acumulado);
			mejorValor = Math.min(mejorValor, acumulado);
		} else if (!esMeta) {
			List<Spm> cand = List2.empty();

			for (Integer action : prob.actions()) {

				double cota = acotar(acumulado, prob, action);
				if (cota > mejorValor)
					continue;

				int pesoActual = DatosFestival.getCosteAsignacion(prob.tipoEntradaActual(), prob.areaActual()) * action;

				FestivalProblem vecino = prob.neighbor(action);
				Spm sHijo = pdr_search(vecino, acumulado + pesoActual);

				if (sHijo != null)
					cand.add(Spm.of(action, sHijo.weight()));
			}
			res = cand.stream().min(Comparator.naturalOrder()).orElse(null);
		}

		if (res != null)
			memory.put(prob, res);
		return res;
	}

	/*----------- cota -----------*/
	private static double acotar(int acum, FestivalProblem prob, int a) {
		int pesoActual = DatosFestival.getCosteAsignacion(prob.tipoEntradaActual(), prob.areaActual()) * a;
		return acum + pesoActual + prob.neighbor(a).noHeuristic();
	}

	/*----------- reconstrucción -----------*/
	private static SolucionFestival reconstruir() {
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
