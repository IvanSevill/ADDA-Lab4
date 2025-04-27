package ejercicio4;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import common.DatosEstaciones;
import us.lsi.common.List2;
import us.lsi.common.String2;
import us.lsi.gurobi.GurobiSolution;

public class SolucionEstaciones {

	private Integer numEstaciones;
	private List<Estacion> camino;
	private Double sumaTiempoMedios;

	// Crear a partir de una solución Gurobi
	public static SolucionEstaciones create(GurobiSolution gs) {
		return new SolucionEstaciones(gs.objVal, gs.values);
	}

	// Crear a partir de lista de índices
	public static SolucionEstaciones of(List<Integer> ls) {
		return new SolucionEstaciones(ls);
	}

	// Crear a partir de un GraphPath (ahora usando EstacionesVertexInterface como
	// vértices)
	public static SolucionEstaciones ofGraphPath(GraphPath<? extends EstacionesVertexInterface, EstacionesEdge> path) {
		List<Integer> alternativas = path.getEdgeList().stream().map(EstacionesEdge::action)
				.collect(Collectors.toList());
		return new SolucionEstaciones(alternativas);
	}

	// Crear a partir de lista de aristas
	public static SolucionEstaciones ofEdges(List<EstacionesEdge> ls) {
		List<Integer> alternativas = List2.empty();
		for (EstacionesEdge alternativa : ls) {
			alternativas.add(0, alternativa.action()); // camino invertido
		}
		return new SolucionEstaciones(alternativas);
	}

	private SolucionEstaciones(Double vo, Map<String, Double> vbles) {
		this.numEstaciones = DatosEstaciones.n;
		this.camino = vbles.entrySet().stream().filter(e -> e.getValue() > 0).sorted(Map.Entry.comparingByKey())
				.map(e -> DatosEstaciones.grafoTiempo.getVertex(Integer.parseInt(e.getKey())))
				.collect(Collectors.toList());
		this.sumaTiempoMedios = calculaTiempoTotal(this.camino);
	}

	private SolucionEstaciones(List<Integer> ls) {
		List<Integer> caminoIndices = List2.copy(ls);

		// Asegurar que empieza en Sol (índice 0)
		if (caminoIndices.isEmpty() || caminoIndices.get(0) != 0) {
			caminoIndices.add(0, 0);
		}

		// Asegurar que termina en Sol (índice 0)
		if (caminoIndices.get(caminoIndices.size() - 1) != 0) {
			caminoIndices.add(0);
		}

		this.numEstaciones = caminoIndices.size();
		this.camino = caminoIndices.stream().map(i -> DatosEstaciones.grafoTiempo.getVertex(i))
				.collect(Collectors.toList());
		this.sumaTiempoMedios = calculaTiempoTotal(this.camino);
	}

	private SolucionEstaciones() {
		this.numEstaciones = 0;
		this.camino = List.of();
		this.sumaTiempoMedios = 0.0;
	}

	// Calcular tiempo medio
	public static Double getTiempoMedio(List<Integer> ls) {
		Integer numEstaciones = ls.size();
		List<Estacion> camino = ls.stream().map(i -> DatosEstaciones.grafoTiempo.getVertex(i))
				.collect(Collectors.toList());
		Double tiempoTotal = calculaTiempoTotal(camino);
		return tiempoTotal / numEstaciones;
	}

	// Calcular tiempo total de un camino
	public static Double calculaTiempoTotal(List<Estacion> camino) {
		Double costeTotal = 0.0;
		if (camino.isEmpty())
			return 0.0;

		for (int i = 0; i < camino.size() - 1; i++) {
			Tramo tramo = DatosEstaciones.grafoCompleto.getEdge(camino.get(i), camino.get(i + 1));
			if (tramo != null) {
				costeTotal += tramo.tiempo();
			} else {
				System.err.printf("No existe tramo entre %s y %s%n", camino.get(i).nombre(),
						camino.get(i + 1).nombre());
				costeTotal += 10000.0; // Penalización si no hay tramo
			}
		}
		return costeTotal;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("\n").append(camino.reversed().stream().map(Estacion::nombre).collect(Collectors.joining(" -> ")))
				.append("\n");
		result.append(String.format("¿Pasa por todas las estaciones?" + " %s%n",
				camino.size() == DatosEstaciones.n + 1 ? "Sí" : "No"));
		return result.toString();
	}

	public Integer getNumEstaciones() {
		return numEstaciones;
	}

	public List<Estacion> getCamino() {
		return camino;
	}

	public Double getSumaTiemposMedios() {
		return sumaTiempoMedios;
	}
}
