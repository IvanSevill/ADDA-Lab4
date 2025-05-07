package ejercicio3;

import java.util.List;
import java.util.function.Predicate;

import common.DatosFestival;
import us.lsi.common.List2;

public class FestivalHeuristic {
	public static Double noHeuristic(FestivalVertexInterface v1, Predicate<FestivalVertexInterface> goal, FestivalVertexInterface v2) {
		Double h = 0.;
		return h;
	}
	
	public static Double heuristic(FestivalVertexInterface v1, Predicate<FestivalVertexInterface> goal, FestivalVertexInterface v2) {
		Double h = 0.;
		Integer costeMinimo = DatosFestival.getCosteMinimo();
		List<Integer> cuentaPorTipo = List2.nCopies(0, DatosFestival.getNumTiposEntrada());
		for (int i = 0; i< v1.entradasAsignadas().size(); i++) {
			int tipo = v1.getTipoEntrada(i);
			cuentaPorTipo.set(tipo, cuentaPorTipo.get(tipo) + v1.entradasAsignadas().get(i));
		}
		
		for (int i = 0; i < cuentaPorTipo.size(); i++) {
			if (cuentaPorTipo.get(i) > 0) {
				h += cuentaPorTipo.get(i) * costeMinimo;
			}
		}
		
		return h;
		
	}
}
