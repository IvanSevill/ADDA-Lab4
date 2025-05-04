package ejercicio3;

import java.util.List;
import java.util.stream.IntStream;

import common.DatosFestival;
import us.lsi.common.List2;

public record FestivalVertex(Integer indice, List<Integer> entradasAsignadas, Integer coste)
		implements FestivalVertexInterface {

	public Integer getArea(Integer i) {
		return i % DatosFestival.getNumAreas();
	}

	public Integer getTipoEntrada(Integer i) {
		return i / DatosFestival.getNumAreas();
	}



	public Integer getTotalCasos() {
		return DatosFestival.getNumAreas() * DatosFestival.getNumTiposEntrada() ;
	}

	public static FestivalVertex of(Integer indice, List<Integer> entradasAsignadas, Integer coste) {
		return new FestivalVertex(indice, entradasAsignadas, coste);
	}

	public Boolean goal() {
		return this.indice.equals(getTotalCasos());
	}

	public Boolean goalHasSolution() {
		List<Integer> cuentaPorTipo = List2.nCopies(0, DatosFestival.getNumTiposEntrada());

		for (int i = 0; i < entradasAsignadas.size(); i++) {
			int tipo = getTipoEntrada(i);
			cuentaPorTipo.set(tipo, cuentaPorTipo.get(tipo) + entradasAsignadas.get(i));
		}

		for (int tipo = 0; tipo < DatosFestival.getNumTiposEntrada(); tipo++) {
			if (cuentaPorTipo.get(tipo) < DatosFestival.getCuotaMinima(tipo)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public List<Integer> actions() {
		List<Integer> alternativas = List2.empty();

		if (this.indice < getTotalCasos()) {
			int tipo = getTipoEntrada(this.indice);
			int area = getArea(this.indice);

			// Calcular lo ya asignado en esta área
			int asignadasEnArea = 0;
			for (int i = 0; i < this.indice; i++) {
				if (getArea(i).equals(area)) {
					asignadasEnArea += entradasAsignadas.get(i);
				}
			}

			int aforoDisponible = DatosFestival.getAforoMaximoArea(area) - asignadasEnArea;

			// Solo múltiplos de 5 que no superen el aforo
			alternativas = IntStream.rangeClosed(0, aforoDisponible)
					.filter(x -> x % 5 == 0) // Literal que sin esta linea peta, desborda la memoria
					.boxed()
					.toList();
		}

		return alternativas;
	}

	@Override
	public FestivalVertex neighbor(Integer a) {
		List<Integer> nuevasAsignaciones = List2.copy(entradasAsignadas);
		nuevasAsignaciones.add(a);

		int tipo = getTipoEntrada(this.indice);
		int area = getArea(this.indice);
		int nuevoCoste = coste + DatosFestival.getCosteAsignacion(tipo, area) * a;

		return FestivalVertex.of(this.indice + 1, nuevasAsignaciones, nuevoCoste);
	}


	@Override
	public FestivalEdge edge(Integer a) {
		return FestivalEdge.of(this, this.neighbor(a), a);
	}


	public String toGraphString() {
		return "Idx=" + indice + ", C=" + coste + ", Asig=" + entradasAsignadas;
	}
}
