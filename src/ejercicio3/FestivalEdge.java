package ejercicio3;

import common.DatosFestival;
import us.lsi.graphs.virtual.SimpleEdgeAction;

public record FestivalEdge(FestivalVertexInterface source, FestivalVertexInterface target, Integer action,
		Double weight) implements SimpleEdgeAction<FestivalVertexInterface, Integer> {

	public static FestivalEdge of(FestivalVertexInterface c1, FestivalVertexInterface c2, Integer action) {
		int tipo = c1.getTipoEntrada(c1.indice());
		int area = c1.getArea(c1.indice());
		double coste = DatosFestival.getCosteAsignacion(tipo, area) * action;
		return new FestivalEdge(c1, c2, action, coste);
	}



	
	
}
