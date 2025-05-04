package ejercicio3Manual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import common.DatosFestival;
import us.lsi.common.List2;


public record FestivalProblem(Integer indice, List<Integer> entradasAsignadas, Integer coste) {

	public static FestivalProblem of(Integer indice, List<Integer> entradasAsignadas, Integer coste) {
		return new FestivalProblem(indice, entradasAsignadas, coste);
	}

	public static FestivalProblem initial() {
		int n = DatosFestival.getNumAreas() * DatosFestival.getNumTiposEntrada();
		List<Integer> asignadas = new ArrayList<>(Collections.nCopies(n, 0));
		return FestivalProblem.of(0, asignadas, 0);
	}

	public int totalCasos() {
		return DatosFestival.getNumAreas() * DatosFestival.getNumTiposEntrada();
	}

	public int getArea(int i) {
		return i % DatosFestival.getNumAreas();
	}

	public int getTipoEntrada(int i) {
		return i / DatosFestival.getNumAreas();
	}

	public int areaActual() {
		return getArea(indice);
	}

	public int tipoEntradaActual() {
		return getTipoEntrada(indice);
	}

	public boolean goal() {
		return indice.equals(totalCasos());
	}

	public boolean goalHasSolution() {
		List<Integer> cuentaPorTipo = List2.nCopies(0, DatosFestival.getNumTiposEntrada());
		for (int i = 0; i < indice; i++) { // solo lo ya decidido
			int tipo = getTipoEntrada(i);
			cuentaPorTipo.set(tipo, cuentaPorTipo.get(tipo) + entradasAsignadas.get(i));
		}
		for (int tipo = 0; tipo < DatosFestival.getNumTiposEntrada(); tipo++)
			if (cuentaPorTipo.get(tipo) < DatosFestival.getCuotaMinima(tipo))
				return false;
		return true;
	}

	public List<Integer> actions() {
		if (indice >= totalCasos())
			return List2.empty();

		int asignadasEnArea = 0;
		for (int i = 0; i < indice; i++)
			if (getArea(i) == areaActual())
				asignadasEnArea += entradasAsignadas.get(i);

		int aforoDisponible = DatosFestival.getAforoMaximoArea(areaActual()) - asignadasEnArea;

		// múltiplos de 5 hasta aforoDisponible
		return IntStream.rangeClosed(0, aforoDisponible).filter(x -> x % 5 == 0).boxed().toList();
	}

	public FestivalProblem neighbor(Integer a) {
		List<Integer> nuevas = List2.copy(entradasAsignadas);
		nuevas.set(indice, a); 

		int costeNuevo = coste + DatosFestival.getCosteAsignacion(tipoEntradaActual(), areaActual()) * a;
		return FestivalProblem.of(indice + 1, nuevas, costeNuevo);
	}

	public double noHeuristic() {
		return 0.;
	}
}
