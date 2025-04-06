package ejercicio2Algoritmos;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import common.AuxCommon;
import common.DatosCursos;
import common.TipoAlgoritmo;
import ejercicio2.CursoEdge;
import ejercicio2.CursoHeuristic;
import ejercicio2.CursoVertexInterface;
import ejercicio2.SolucionCursos;
import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.alg.GreedyOnGraph;
import us.lsi.graphs.alg.PDR;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio2PDR {
	public static final Integer EJERCICIO = 2;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio" + EJERCICIO + "/PDR_";
	public static final String FICHERO = "resources/ejercicio" + EJERCICIO + "/";

	public static void main(String[] args) {
		for (Integer i = 1; i <= DatosCursos.ntest; i++) {
			ejecucionPDR(i);
		}
	}

	public static void ejecucionPDR(Integer id_fichero) {

		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosCursos.iniDatos(fichero);

		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.PDR);

		// Vertice inicial
		CursoVertexInterface start = CursoVertexInterface.start();

		// Grafo virtual
		EGraph<CursoVertexInterface, CursoEdge> virtualGraph = EGraph.virtual(start).pathType(PathType.Sum)
				.type(Type.Max).edgeWeight(x -> x.weight()).heuristic(CursoHeuristic::heuristic).build();

		Optional<GraphPath<CursoVertexInterface, CursoEdge>> optionalVoraz = GreedyOnGraph.of(virtualGraph).search();

		// Si encuentra una solucion, inicializo el PDR con ella, sino no
		PDR<CursoVertexInterface, CursoEdge, SolucionCursos> pdrSolver = aproximacionVoraz(optionalVoraz, virtualGraph);

		// Algoritmo PDR
		AuxCommon.imprimeCabecera("PDR");
		Optional<GraphPath<CursoVertexInterface, CursoEdge>> optPDR = pdrSolver.search();

		if (optPDR.isPresent()) {
			SolucionCursos solucion = construirSolucion(optPDR.get());
			System.out.println(solucion);
			guardaGrafoSolucion(pdrSolver, FICHERO_SALIDA + id_fichero + ".gv");
		} else {
			System.out.println("No hay solución con PDR.");
		}

	}

	private static void guardaGrafoSolucion(PDR<CursoVertexInterface, CursoEdge, ?> pdr, String ficheroSalida) {
		// Obtener el camino óptimo (ya que en el contexto global no está disponible)
		GraphPath<CursoVertexInterface, CursoEdge> gp = pdr.search().get();

		GraphColors.toDot(pdr.outGraph(), ficheroSalida, v -> v.toGraphString(), e -> e.action() > 0 ? "Y" : "N",
				v -> GraphColors.colorIf(Color.red, gp.getVertexList().contains(v)),
				e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e)) // Color rojo si es camino óptimo
		);

		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}

	private static PDR<CursoVertexInterface, CursoEdge, SolucionCursos> aproximacionVoraz(
			Optional<GraphPath<CursoVertexInterface, CursoEdge>> optVoraz, EGraph<CursoVertexInterface, CursoEdge> vg) {

		AuxCommon.imprimeCabecera("Aproximación Voraz");

		if (optVoraz.isPresent()) {
			// En el caso de que haya solucion, la paso como valores iniciales de PDR
			SolucionCursos sVoraz = construirSolucion(optVoraz.get());
			System.out.println(sVoraz);
			return PDR.of(vg, Ejercicio2PDR::construirSolucion, optVoraz.get().getWeight(), optVoraz.get(), true);
		} else {
			// En el caso de que no haya solucion, no puedo pasarle nada
			System.out.println("No hay solución voraz.");
			return PDR.of(vg, Ejercicio2PDR::construirSolucion, null, null, true);
		}
	}

	private static SolucionCursos construirSolucion(GraphPath<CursoVertexInterface, CursoEdge> path) {
		List<Integer> acciones = path.getEdgeList().stream().map(CursoEdge::action).collect(Collectors.toList());
		return SolucionCursos.of(acciones);
	}
}
