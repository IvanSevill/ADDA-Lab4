package ejercicio1;

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record AlmacenEdge(AlmacenVertexInterface source, AlmacenVertexInterface target, Integer action, Double weight)
		implements SimpleEdgeAction<AlmacenVertexInterface, Integer> {

	public static AlmacenEdge of(AlmacenVertexInterface c1, AlmacenVertexInterface c2, Integer action) {
		return new AlmacenEdge(c1, c2, action, action > -1 ? 1.0 : 0.0);
	}

}
