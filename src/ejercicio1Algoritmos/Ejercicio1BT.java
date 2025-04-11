package ejercicio1Algoritmos;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import common.AuxCommon;
import common.DatosAlmacenes;
import common.TipoAlgoritmo;
import ejercicio1.AlmacenEdge;
import ejercicio1.AlmacenHeuristic;
import ejercicio1.AlmacenVertexInterface;
import ejercicio1.SolucionAlmacen;
import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio1BT {

	public static final Integer EJERCICIO = 1;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio"+EJERCICIO+"/BT_";
	public static final String FICHERO = "resources/ejercicio"+EJERCICIO+"/";

	public static void main(String[] args) {
		for (Integer i = 1; i < DatosAlmacenes.ntest; i++) {
			ejecucionBT(i);
		}
	}

	public static void ejecucionBT(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosAlmacenes.iniDatos(fichero);

		// No hace falta
		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.BT);

		// Vertices clave
		AlmacenVertexInterface start = AlmacenVertexInterface.start();

		// Grafo virtual para el BT
		EGraph<AlmacenVertexInterface, AlmacenEdge> virtualGraph = 
				EGraph.virtual(start)
				.pathType(PathType.Sum)
				.type(Type.Max)
				.edgeWeight(x -> x.weight())
				.heuristic(AlmacenHeuristic::heuristic)
				.build();

		// Objeto del Algoritmo BT
		BT<AlmacenVertexInterface, AlmacenEdge, SolucionAlmacen> bta  = BT.ofGreedy(virtualGraph);
		
		// Ejecucion de BT para encontrar el camino optimo
		Optional<GraphPath<AlmacenVertexInterface, AlmacenEdge>> optionalSolucion = bta.search();
		
		if (optionalSolucion.isPresent()) {
			GraphPath<AlmacenVertexInterface, AlmacenEdge> solution = optionalSolucion.get();
			List<Integer> gp_as = solution.getEdgeList().stream().map(e -> e.action()).collect(Collectors.toList());
			System.out.println(SolucionAlmacen.of(gp_as));
			
			// NO hace realmente falta
			// guardaGrafoSolucion(bta, FICHERO_SALIDA + id_fichero + ".gv");

		} else {
			System.out.println("No se ha encontrado solución");
		}

	}

	private static void guardaGrafoSolucion(BT<AlmacenVertexInterface, AlmacenEdge, ?> bt, String ficheroSalida) {
		// Obtener el camino óptimo (ya que en el contexto global no está disponible)
		GraphPath<AlmacenVertexInterface, AlmacenEdge> gp = bt.search().get();

		// Significando P el producto y A el almacen al que se guarda
		GraphColors.toDot(bt.outGraph(), ficheroSalida, 
				v -> v.toGraphString(),				
				e -> "A"+e.action(),
				v -> GraphColors.colorIf(Color.red, DatosAlmacenes.getNumProductos() == v.indice()),
				e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e)));
		
		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}
	
}
