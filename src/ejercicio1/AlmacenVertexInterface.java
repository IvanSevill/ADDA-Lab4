package ejercicio1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.DatosAlmacenes;
import us.lsi.graphs.virtual.VirtualVertex;

public interface AlmacenVertexInterface extends VirtualVertex<AlmacenVertexInterface, AlmacenEdge, Integer> {

	Integer indice();
	Integer cantidadAlmacenada();
	Double accionReal();
	String toGraphString();
	List<Integer> espacioDisponible();
	List<Set<Integer>> productosAlmacenados();
	
	public static AlmacenVertex start() {

		List<Set<Integer>> productosIniciales = new ArrayList<>();
		List<Integer> espacios = new ArrayList<>();

		for (int i = 0; i < DatosAlmacenes.getNumAlmacenes(); i++) {
			productosIniciales.add(new HashSet<>());
			espacios.add(DatosAlmacenes.getMetrosCubicosAlmacen(i));
		}

		return AlmacenVertex.of(0, 0, productosIniciales, espacios);
	}

}
