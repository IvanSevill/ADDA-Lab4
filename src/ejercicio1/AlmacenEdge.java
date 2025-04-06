package ejercicio1;

import common.DatosAlmacenes;
import us.lsi.graphs.virtual.SimpleEdgeAction;

public record AlmacenEdge(AlmacenVertexInterface source, AlmacenVertexInterface target, Integer action, Double weight)
		implements SimpleEdgeAction<AlmacenVertexInterface, Integer> {

	public static AlmacenEdge of(AlmacenVertexInterface c1, AlmacenVertexInterface c2, Integer action) {
		// El peso indica si se pone el producto en esa arista
		return new AlmacenEdge(c1, c2, action, (action > -1 && c2.indice() < DatosAlmacenes.getNumProductos()) ? 1.0 : 0.0);
	}

}
