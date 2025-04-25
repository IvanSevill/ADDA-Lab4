package ejercicio4;

import common.DatosEstaciones;
import us.lsi.graphs.virtual.SimpleEdgeAction;

public record EstacionesEdge(EstacionesVertexInterface source, EstacionesVertexInterface target, Integer action,
		Double weight) implements SimpleEdgeAction<EstacionesVertexInterface, Integer>{

	public static EstacionesEdge of(EstacionesVertexInterface c1, EstacionesVertexInterface c2, Integer action) {
		Double peso = DatosEstaciones.grafoCoste.getEdgeWeight(c1.indice(), c2.indice());
		return new EstacionesEdge(c1, c2, action, peso);
	}

}
