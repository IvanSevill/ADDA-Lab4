package ejercicio2Algoritmos;

import java.util.List;
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
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio2AStar {
	public static final Integer EJERCICIO = 2;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio" + EJERCICIO + "/AStar_";
	public static final String FICHERO = "resources/ejercicio" + EJERCICIO + "/";

	public static void main(String[] args) {
		for (Integer i = 1; i <= DatosCursos.ntest; i++) {
			ejecucionAStar(i);
		}
	}

	public static void ejecucionAStar(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosCursos.iniDatos(fichero);

		// No hace falta
		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.AStar);

		// Vertices clave
		CursoVertexInterface start = CursoVertexInterface.start();

		// Grafo virtual para el A*
		EGraph<CursoVertexInterface, CursoEdge> virtualGraph = 
				EGraph.virtual(start)
				.pathType(PathType.Sum)
				.type(Type.Max)
				.edgeWeight(x -> x.weight())
				.heuristic(CursoHeuristic::heuristic)
				.build();

		// Objeto del Algoritmo A*
		AStar<CursoVertexInterface, CursoEdge, ?> aStar = AStar.ofGreedy(virtualGraph);

		// Ejecucion de A* para encontrar el camino optimo
		GraphPath<CursoVertexInterface, CursoEdge> gp = aStar.search().get();

		// Me quedo con los vertices y su accion (almacen al que va el producto i)
		List<Integer> gp_as = gp.getEdgeList().stream().map(x -> x.action()).collect(Collectors.toList());

		// Construyo la solución con la lista de acciones
		SolucionCursos s_as = SolucionCursos.of(gp_as);

		System.out.println(s_as);

        // Funciona
		guardaGrafoSolucion(aStar, FICHERO_SALIDA + id_fichero + ".gv");
	}

	private static void guardaGrafoSolucion(AStar<CursoVertexInterface, CursoEdge, ?> aStar, String ficheroSalida) {
		// Obtener el camino óptimo (ya que en el contexto global no está disponible)
		GraphPath<CursoVertexInterface, CursoEdge> gp = aStar.search().get();

		GraphColors.toDot(aStar.outGraph(), ficheroSalida, 
				v -> v.toGraphString(), 
				e -> e.action() > 0 ? "Y" : "N",
				v -> GraphColors.colorIf(Color.red, gp.getVertexList().contains(v)),
				e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e)) // Color rojo si es camino óptimo
		);

		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}

}
