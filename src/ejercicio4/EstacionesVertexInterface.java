package ejercicio4;

import java.util.ArrayList;
import java.util.List;

import us.lsi.graphs.virtual.VirtualVertex;

public interface EstacionesVertexInterface extends VirtualVertex<EstacionesVertexInterface, EstacionesEdge, Integer> {

	Integer indice();
	List<Integer> camino();
	Double costeAcumulado();

	public static EstacionesVertexInterface start() {
		List<Integer> camino = new ArrayList<>();
		camino.add(0);
		return new EstacionesVertex(1, camino, 0.0);
	}

}
