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
	public static final String FICHERO_SALIDA = "generated_files/ejercicio" + EJERCICIO + "/PDR_";
	public static final String FICHERO = "resources/ejercicio" + EJERCICIO + "/";

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
		EGraph<AlmacenVertexInterface, AlmacenEdge> virtualGraph = EGraph.virtual(start).pathType(PathType.Sum)
				.type(Type.Max).edgeWeight(x -> x.weight()).heuristic(AlmacenHeuristic::heuristic).build();

		// Objeto del Algoritmo PDR
		PDR<AlmacenVertexInterface, AlmacenEdge, ?> pdr = PDR.ofGreedy(virtualGraph);

		Optional<GraphPath<AlmacenVertexInterface, AlmacenEdge>> gp = pdr.search();

		if (gp.isPresent()) {
			GraphPath<AlmacenVertexInterface, AlmacenEdge> solucion = gp.get();
			List<Integer> gp_as = solucion.getEdgeList().stream().map(e -> e.action()).collect(Collectors.toList());
			SolucionAlmacen s_as = SolucionAlmacen.of(gp_as);
			System.out.println(s_as);
			
			// NO hace realmente falta
			// guardaGrafoSolucion(pdr, FICHERO_SALIDA + id_fichero + ".gv");
		} else {
			System.out.println("No se ha encontrado solución");
		}

	}

	private static void guardaGrafoSolucion(PDR<AlmacenVertexInterface, AlmacenEdge, ?> pdr,
			String ficheroSalida) {

		GraphPath<AlmacenVertexInterface, AlmacenEdge> gp = pdr.search().get();

		// Usar GraphColors.toDot para generar el archivo DOT
		GraphColors.toDot(pdr.outGraph(), ficheroSalida, v -> v.toGraphString(), e -> e.action().toString(),
				v -> GraphColors.colorIf(Color.red, v.goal()),
				e -> GraphColors.colorIf(Color.red, gp.getEdgeList().contains(e)));
		;

		System.out.println("\n( El grafo se ha guardado en el fichero " + ficheroSalida + " )");
	}

}
