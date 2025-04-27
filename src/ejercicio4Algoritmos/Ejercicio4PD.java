package ejercicio4Algoritmos;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import common.AuxCommon;
import common.DatosEstaciones;
import common.TipoAlgoritmo;
import ejercicio4.EstacionesEdge;
import ejercicio4.EstacionesHeuristic;
import ejercicio4.EstacionesVertexInterface;
import ejercicio4.SolucionEstaciones;
import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.alg.PDR;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio4PD {

	public static final Integer EJERCICIO = 4;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio" + EJERCICIO + "/AStar_";
	public static final String FICHERO = "resources/ejercicio" + EJERCICIO + "/";

	public static void main(String[] args) {
		for (Integer i = 1; i <= 3; i++) {
			ejecucionPD(i);
		}
	}

	public static void ejecucionPD(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosEstaciones.iniDatos(fichero);

		// No hace falta
		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.PD);

		EstacionesVertexInterface start = EstacionesVertexInterface.start();

		// Algoritmo A*
		EGraph<EstacionesVertexInterface, EstacionesEdge> graph =
				EGraph.virtual(start)
				.pathType(PathType.Sum)
				.type(Type.Min)
				.edgeWeight(x -> x.weight())
				.heuristic(EstacionesHeuristic::heuristic)
				.build();
				
		PDR<EstacionesVertexInterface,EstacionesEdge,?> pd = PDR.ofGreedy(graph);
		
		GraphPath<EstacionesVertexInterface, EstacionesEdge> gp = pd.search().get();
		
		List<Integer> gp_as = gp.getEdgeList().stream().map(x -> x.action())
				.collect(Collectors.toList()); // getEdgeList();
		
		
		SolucionEstaciones s_as = SolucionEstaciones.of(gp_as);
		
		
		System.out.println(s_as);
		System.out.println(gp_as);

        // No va
		// guardaGrafoSolucion(pd, FICHERO_SALIDA + id_fichero + ".gv");
	}

	private static void guardaGrafoSolucion(PDR<EstacionesVertexInterface, EstacionesEdge, ?> pd, String ficheroSalida) {
		GraphPath<EstacionesVertexInterface, EstacionesEdge> gp = pd.search().get();

		GraphColors.toDot(pd.outGraph(), ficheroSalida, 
				v -> v.toString(), 
				e -> e.action().toString(),
				v -> GraphColors.colorIf(Color.red, gp.getVertexList().contains(v)),
				e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e)) // Color rojo si es camino óptimo
		);

		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}
	
}
