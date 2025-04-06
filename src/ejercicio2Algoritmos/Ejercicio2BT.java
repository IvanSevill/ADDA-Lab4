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
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.alg.GreedyOnGraph;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio2BT {
	public static final Integer EJERCICIO = 2;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio" + EJERCICIO + "/BT_";
	public static final String FICHERO = "resources/ejercicio" + EJERCICIO + "/";

	public static void main(String[] args) {
		for (Integer i = 1; i <= DatosCursos.ntest; i++) {
			ejecucionBT(i);
		}
	}

	public static void ejecucionBT(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosCursos.iniDatos(fichero);

		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.BT);

		CursoVertexInterface start = CursoVertexInterface.start();

		EGraph<CursoVertexInterface, CursoEdge> virtualGraph = EGraph.virtual(start).pathType(PathType.Sum)
				.type(Type.Max).edgeWeight(x -> x.weight()).heuristic(CursoHeuristic::heuristic).build();

		// Algoritmo Voraz
		Optional<GraphPath<CursoVertexInterface, CursoEdge>> optionalVoraz = GreedyOnGraph.of(virtualGraph).search();

		// Si encuentra una solucion, inicializo el BT con ella, sino no
		BT<CursoVertexInterface, CursoEdge, SolucionCursos> btSolver = aproximacionVoraz(optionalVoraz, virtualGraph);

		// Algoritmo Backtracking
		AuxCommon.imprimeCabecera("Backtracking");
		Optional<GraphPath<CursoVertexInterface, CursoEdge>> optBT = btSolver.search();

		if (optBT.isPresent()) {
			SolucionCursos solucion = construirSolucion(optBT.get());
			System.out.println(solucion);
			guardaGrafoSolucion(btSolver, FICHERO_SALIDA + id_fichero + ".gv");
		} else {
			System.out.println("No hay solución con BT.");
		}
	}

	private static void guardaGrafoSolucion(BT<CursoVertexInterface, CursoEdge, ?> bt, String ficheroSalida) {
		// Obtener el camino óptimo (ya que en el contexto global no está disponible)
		GraphPath<CursoVertexInterface, CursoEdge> gp = bt.search().get();

		GraphColors.toDot(bt.outGraph(), ficheroSalida, v -> v.toGraphString(), e -> e.action() > 0 ? "Y" : "N",
				v -> GraphColors.colorIf(Color.red, gp.getVertexList().contains(v)),
				e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e)) // Color rojo si es camino óptimo
		);

		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}

	private static BT<CursoVertexInterface, CursoEdge, SolucionCursos> aproximacionVoraz(
			Optional<GraphPath<CursoVertexInterface, CursoEdge>> optVoraz, EGraph<CursoVertexInterface, CursoEdge> vg) {

		AuxCommon.imprimeCabecera("Aproximación Voraz");

		if (optVoraz.isPresent()) {
			// En el caso de que haya solucion, la paso como valores iniciales de BT
			SolucionCursos sVoraz = construirSolucion(optVoraz.get());
			System.out.println(sVoraz);
			return BT.of(vg, Ejercicio2BT::construirSolucion, optVoraz.get().getWeight(), optVoraz.get(), true);
		} else {
			// En el caso de que no haya solucion, no puedo pasarle nada
			System.out.println("No hay solución voraz.");
			return BT.of(vg, Ejercicio2BT::construirSolucion, null, null, true);
		}
	}

	// Para evitar que el codigo quede tan farragoso, lo he separado en un metodo
	private static SolucionCursos construirSolucion(GraphPath<CursoVertexInterface, CursoEdge> path) {
		List<Integer> acciones = path.getEdgeList().stream().map(CursoEdge::action).collect(Collectors.toList());
		return SolucionCursos.of(acciones);
	}
}