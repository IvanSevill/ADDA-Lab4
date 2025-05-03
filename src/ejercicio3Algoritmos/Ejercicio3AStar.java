package ejercicio3Algoritmos;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import common.AuxCommon;
import common.DatosFestival;
import common.TipoAlgoritmo;
import ejercicio3.FestivalEdge;
import ejercicio3.FestivalHeuristic;
import ejercicio3.FestivalVertex;
import ejercicio3.FestivalVertexInterface;
import ejercicio3.SolucionFestival;
import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio3AStar {
	public static final Integer EJERCICIO = 3;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio" + EJERCICIO + "/AStar_";
	public static final String FICHERO = "resources/ejercicio" + EJERCICIO + "/";

	public static void main(String[] args) {
		for (Integer i = 1; i <= AuxCommon.NUM_ARCHIVOS; i++) {
			ejecucionAStar(i);
		}
	}

	public static void ejecucionAStar(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosFestival.iniDatos(fichero);

		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.AStar);

		FestivalVertexInterface start = FestivalVertexInterface.start();

		EGraph<FestivalVertexInterface, FestivalEdge> graph =
				EGraph.virtual(start)
				.pathType(PathType.Sum)
				.type(Type.Min)
				.edgeWeight(x -> x.weight())
				.heuristic(FestivalHeuristic::noHeuristic)
				.build();
		
		
		AStar<FestivalVertexInterface, FestivalEdge, ?> aStar = AStar.ofGreedy(graph);

		GraphPath<FestivalVertexInterface, FestivalEdge> gp = aStar.search().orElse(null);

		if (gp != null) {
			List<Integer> acciones = gp.getEdgeList().stream()
				.map(FestivalEdge::action)
				.collect(Collectors.toList());

			SolucionFestival solucion = SolucionFestival.create(acciones);
			System.out.println(solucion);

			// guardaGrafoSolucion(aStar, FICHERO_SALIDA + id_fichero + ".gv");
		} else {
			System.out.println("No se encontró solución para el fichero " + fichero);
		}
	}


	private static void guardaGrafoSolucion(AStar<FestivalVertex, FestivalEdge, ?> aStar, String ficheroSalida) {
		GraphPath<FestivalVertex, FestivalEdge> gp = aStar.search().get();

		GraphColors.toDot(aStar.outGraph(), ficheroSalida, 
			v -> v.toGraphString(),
			e -> e.action().toString(),
			v -> GraphColors.colorIf(Color.red, gp.getVertexList().contains(v)),
			e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e))
		);

		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}
}
