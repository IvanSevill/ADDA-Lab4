package ejercicio1;

import us.lsi.graphs.virtual.VirtualVertex;

public interface AlmacenVertexInterface extends VirtualVertex<AlmacenVertexInterface, AlmacenEdge, Integer> {

	// Nuevos métodos necesarios además de los que hereda de VirtualVertex
	
	Integer indice();

	Double heuristic();

}
