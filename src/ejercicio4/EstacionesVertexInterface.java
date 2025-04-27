package ejercicio4;

import java.util.List;

import us.lsi.common.List2;
import us.lsi.graphs.virtual.VirtualVertex;

public interface EstacionesVertexInterface extends VirtualVertex<EstacionesVertexInterface, EstacionesEdge, Integer> {

	Integer indice();
	List<Integer> camino();

	public static EstacionesVertex start() {
	    List <Integer> camino = List2.empty();
	    camino.add(0); // Nodo donde se inicia el camnino
		return  EstacionesVertex.of(0,camino);
	}

}
