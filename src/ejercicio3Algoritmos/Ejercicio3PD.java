package ejercicio3Algoritmos;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import common.AuxCommon;
import common.DatosFestival;
import common.TipoAlgoritmo;
import ejercicio3.FestivalEdge;
import ejercicio3.FestivalHeuristic;
import ejercicio3.FestivalVertexInterface;
import ejercicio3.SolucionFestival;
import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.alg.PDR;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio3PD {

	public static final Integer EJERCICIO = 3;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio" + EJERCICIO + "/PD_";
	public static final String FICHERO = "resources/ejercicio" + EJERCICIO + "/";

	public static void main(String[] args) {
		for (int i = 1; i <= AuxCommon.NUM_ARCHIVOS; i++) {
			ejecucionPD(i);
		}
	}

	public static void ejecucionPD(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosFestival.iniDatos(fichero);

		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.PD);

		FestivalVertexInterface start = FestivalVertexInterface.start();

		EGraph<FestivalVertexInterface, FestivalEdge> graph = 
			EGraph.virtual(start)
			.pathType(PathType.Sum)
			.type(Type.Min)
			.edgeWeight(x -> x.weight())
			.heuristic(FestivalHeuristic::noHeuristic)
			.build();

		PDR<FestivalVertexInterface, FestivalEdge, ?> pd = PDR.of(graph);

		GraphPath<FestivalVertexInterface, FestivalEdge> gp = pd.search().get();

		List<Integer> gp_as = gp.getEdgeList().stream().map(x -> x.action()).collect(Collectors.toList());

		SolucionFestival s_as = SolucionFestival.create(gp_as);

		System.out.println(s_as);

		// Descomenta si quieres exportar el grafo en .gv
		// guardaGrafoSolucion(pd, FICHERO_SALIDA + id_fichero + ".gv");
	}

	private static void guardaGrafoSolucion(PDR<FestivalVertexInterface, FestivalEdge, ?> pd, String ficheroSalida) {
		GraphPath<FestivalVertexInterface, FestivalEdge> gp = pd.search().get();

		GraphColors.toDot(pd.outGraph(), ficheroSalida, 
			v -> v.toString(), 
			e -> e.action().toString(),
			v -> GraphColors.colorIf(Color.red, gp.getVertexList().contains(v)),
			e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e))
		);

		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}
}
