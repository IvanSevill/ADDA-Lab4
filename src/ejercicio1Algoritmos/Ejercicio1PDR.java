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
import us.lsi.graphs.alg.PDR;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Ejercicio1PDR {
	public static final Integer EJERCICIO = 1;
	public static final String FICHERO_SALIDA = "generated_files/ejercicio"+EJERCICIO+"/PDR_";
	public static final String FICHERO = "resources/ejercicio"+EJERCICIO+"/";
	
	public static void main(String[] args) {
		for (Integer i = 1; i < DatosAlmacenes.ntest; i++) {
			ejecucionPD(i);
		}
	}
	
	public static void ejecucionPD(Integer id_fichero) {
		String fichero = FICHERO + "DatosEntrada" + id_fichero + ".txt";
		DatosAlmacenes.iniDatos(fichero);

		// No hace falta
		AuxCommon.imprimeCabeceraAlgoritmo(fichero, EJERCICIO, TipoAlgoritmo.PDR);

		// Vertices clave
		AlmacenVertexInterface start = AlmacenVertexInterface.start();

		// Grafo virtual para el PDR
		EGraph<AlmacenVertexInterface, AlmacenEdge> virtualGraph = 
				EGraph.virtual(start)
				.pathType(PathType.Sum)
				.type(Type.Max)
				.edgeWeight(x -> x.weight())
				.heuristic(AlmacenHeuristic::heuristic)
				.build();

		// Objeto del Algoritmo PDR
		PDR<AlmacenVertexInterface, AlmacenEdge, ?> pdr = PDR.of(virtualGraph, null, null, null, true);

		Optional<GraphPath<AlmacenVertexInterface, AlmacenEdge>> gp = pdr.search();

		SolucionAlmacen s_pdr = null;
		
		if (gp.isPresent()) {
			List<Integer> gp_pdr = gp.get().getEdgeList().stream().map(x -> x.action()).collect(Collectors.toList());
			// System.out.println(gp_pdr);
			s_pdr = SolucionAlmacen.of(gp_pdr);
		} 
		
		// Imprimo la solucion que sea mejor
		System.out.println(s_pdr);
		
		// En teoria está bien, pero no funciona ni siquiera en los ejemplos del repopositorio
		guardaGrafoSolucion(gp, FICHERO_SALIDA + id_fichero + ".gv");
	}

	private static void guardaGrafoSolucion(Optional<GraphPath<AlmacenVertexInterface, AlmacenEdge>> gp, String ficheroSalida) {

		GraphPath<AlmacenVertexInterface, AlmacenEdge> pdr = gp.get();
		
	    // Usar GraphColors.toDot para generar el archivo DOT
		GraphColors.toDot(pdr.getGraph(), ficheroSalida, 
				v -> v.toGraphString(),
				e -> e.action().toString(), 
				v -> GraphColors.colorIf(Color.red, v.goal()),
				e -> GraphColors.colorIf(Color.red, pdr.getEdgeList().contains(e)));
	    ;

	    System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}
	
	
}
