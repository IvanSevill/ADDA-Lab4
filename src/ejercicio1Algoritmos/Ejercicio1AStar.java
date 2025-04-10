package ejercicio1Algoritmos;

import common.AuxCommon;
import common.DatosAlmacenes;
import common.TipoAlgoritmo;
import ejercicio1.AlmacenEdge;
import ejercicio1.AlmacenHeuristic;
import ejercicio1.AlmacenVertexInterface;
import ejercicio1.SolucionAlmacen;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.alg.GreedyOnGraph;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio1AStar {

	public static final Integer EJERCICIO = 1;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio" + EJERCICIO + "/AStar_";
	public static final String FICHERO = "resources/ejercicio" + EJERCICIO + "/";

	public static void main(String[] args) {
		for (Integer i = 1; i < DatosAlmacenes.ntest; i++) {
			ejecucionAStar(i);
		}
	}

	// Existen dos formas de hacerlo, usando el ofGreedy o sin usarlo directamente.

	// Con ofGreedy
	public static void ejecucionAStar(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosAlmacenes.iniDatos(fichero);

		// No hace falta, es solo presentación
		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.AStar);

		// Grafo virtual para el A*
		AlmacenVertexInterface start = AlmacenVertexInterface.start();

		EGraph<AlmacenVertexInterface, AlmacenEdge> virtualGraph = 
				EGraph.virtual(start)
				.pathType(PathType.Sum)
				.type(Type.Min)
				.edgeWeight(x -> x.weight())
				.heuristic(AlmacenHeuristic::heuristic)
				.build();

		// Objeto del Algoritmo A* con ofGreedy
		AStar<AlmacenVertexInterface, AlmacenEdge, ?> aStar = AStar.ofGreedy(virtualGraph);

		// Ejecucion de A* para encontrar el camino optimo
		Optional<GraphPath<AlmacenVertexInterface, AlmacenEdge>> gp = aStar.search();

		if (gp.isPresent()) {
			GraphPath<AlmacenVertexInterface, AlmacenEdge> solution = aStar.search().get();
			List<Integer> gp_as = solution.getEdgeList().stream().map(x -> x.action()).collect(Collectors.toList());
			SolucionAlmacen s_as = SolucionAlmacen.of(gp_as);
            System.out.println(s_as);

			// NO hace realmente falta
			guardaGrafoSolucion(aStar, FICHERO_SALIDA + id_fichero + ".gv");
		} else {
			System.out.println("No se ha encontrado solución");
		}
	}

	// Sin ofGreedy
	public static void ejecucionAStar2(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosAlmacenes.iniDatos(fichero);

		// No hace falta
		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.AStar);

		// Vertices clave
		AlmacenVertexInterface start = AlmacenVertexInterface.start();

		// Grafo virtual para el A*
		EGraph<AlmacenVertexInterface, AlmacenEdge> virtualGraph = 
				EGraph.virtual(start)
				.pathType(PathType.Sum)
				.type(Type.Min)
				.edgeWeight(x -> x.weight())
				.heuristic(AlmacenHeuristic::heuristic)
				.build();

		// Resultado del Algoritmo Voraz y inicializamos el objeto AStar
		Optional<GraphPath<AlmacenVertexInterface, AlmacenEdge>> optionalVoraz = GreedyOnGraph.of(virtualGraph)
				.search();
		AStar<AlmacenVertexInterface, AlmacenEdge, ?> aStar = null;

		// En caso de que exista una solución Voraz, la utilizamos como valores
		// iniciales
		if (optionalVoraz.isPresent()) {
			GraphPath<AlmacenVertexInterface, AlmacenEdge> solucionVoraz = optionalVoraz.get();
			aStar = AStar.of(virtualGraph, solucionVoraz.getWeight(), solucionVoraz);
			// En caso contrario, no se utiliza y se inicializa sin valores iniciales (null)
		} else {
			aStar = AStar.of(virtualGraph);
		}

		// Ejecucion de A* para encontrar el camino optimo
		Optional<GraphPath<AlmacenVertexInterface, AlmacenEdge>> gp = aStar.search();

		if (gp.isPresent()) {
			GraphPath<AlmacenVertexInterface, AlmacenEdge> solution = aStar.search().get();
			List<Integer> gp_as = solution.getEdgeList().stream().map(x -> x.action()).collect(Collectors.toList());
			SolucionAlmacen s_as = SolucionAlmacen.of(gp_as);
            System.out.println(s_as);
			// NO hace realmente falta
			guardaGrafoSolucion(aStar, FICHERO_SALIDA + id_fichero + ".gv");
		} else {
			System.out.println("No se ha encontrado solución");
		}
	}

	private static void guardaGrafoSolucion(AStar<AlmacenVertexInterface, AlmacenEdge, ?> aStar, String ficheroSalida) {
		GraphPath<AlmacenVertexInterface, AlmacenEdge> gp = aStar.search().get();

		GraphColors.toDot(aStar.outGraph(), ficheroSalida, v -> v.toGraphString(), e -> "A" + e.action(),
				v -> GraphColors.colorIf(Color.red, DatosAlmacenes.getNumProductos() == v.indice()),
				e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e)));

		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}

}
