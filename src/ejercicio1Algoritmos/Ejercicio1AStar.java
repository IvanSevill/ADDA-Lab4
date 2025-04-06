package ejercicio1Algoritmos;

import common.AuxCommon;
import common.DatosAlmacenes;
import common.TipoAlgoritmo;
import ejercicio1.AlmacenEdge;
import ejercicio1.AlmacenHeuristic;
import ejercicio1.AlmacenVertexInterface;
import ejercicio1.SolucionAlmacen;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio1AStar {

	public static final Integer EJERCICIO = 1;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio"+EJERCICIO+"/AStar_";
	public static final String FICHERO = "resources/ejercicio"+EJERCICIO+"/";

	public static void main(String[] args) {
		for (Integer i = 1; i < DatosAlmacenes.ntest; i++) {
			ejecucionAStar(i);
		}
	}

	public static void ejecucionAStar(Integer id_fichero) {
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

		// Objeto del Algoritmo A*
		AStar<AlmacenVertexInterface, AlmacenEdge, ?> aStar = AStar.ofGreedy(virtualGraph);

		// Ejecucion de A* para encontrar el camino optimo
		GraphPath<AlmacenVertexInterface, AlmacenEdge> gp = aStar.search().get();

		// Me quedo con los vertices y su accion (almacen al que va el producto i)
		List<Integer> gp_as = gp.getEdgeList().stream().map(x -> x.action()).collect(Collectors.toList());

		// Construyo la solución con la lista de acciones
		SolucionAlmacen s_as = SolucionAlmacen.of(gp_as);

		System.out.println(s_as);

		// En teoria está bien, pero no funciona ni siquiera en los ejemplos del repopositorio
		guardaGrafoSolucion(aStar, FICHERO_SALIDA + id_fichero + ".gv");
	}

	private static void guardaGrafoSolucion(AStar<AlmacenVertexInterface, AlmacenEdge, ?> aStar, String ficheroSalida) {
	    // Obtener el camino óptimo (ya que en el contexto global no está disponible)
	    GraphPath<AlmacenVertexInterface, AlmacenEdge> gp = aStar.search().get();

	    GraphColors.toDot(aStar.outGraph(), ficheroSalida, 
				v -> v.toGraphString(),
	            e -> "A" + e.action(),  // Etiquetado de aristas
	            v -> GraphColors.colorIf(Color.red, DatosAlmacenes.getNumProductos() == v.indice()),  // Color rojo si es el último producto
	            e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e)) // Color rojo si la arista está en el camino óptimo
	    );

	    System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}


}
