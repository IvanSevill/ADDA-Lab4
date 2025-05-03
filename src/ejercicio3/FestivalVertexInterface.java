package ejercicio3;

import java.util.List;

import us.lsi.common.List2;
import us.lsi.graphs.virtual.VirtualVertex;

public interface FestivalVertexInterface extends VirtualVertex<FestivalVertexInterface, FestivalEdge, Integer> {

	Integer indice();

	Integer coste();

	List<Integer> entradasAsignadas();

	public static FestivalVertex start() {
		return FestivalVertex.of(0, List2.empty(), 0);
	}

	Integer getTipoEntrada(Integer i);
	public Integer getArea(Integer i);

}
