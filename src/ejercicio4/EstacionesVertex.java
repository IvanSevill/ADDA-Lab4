package ejercicio4;

import java.util.List;

public record EstacionesVertex(Integer indice, List<Integer> camino, Double costeAcumulado)
		implements EstacionesVertexInterface {

	public static EstacionesVertex of(Integer indice, List<Integer> camino, Double costeAcumulado) {
		return new EstacionesVertex(indice, camino, costeAcumulado);
	}

	public List<Integer> actions() {
		return null;
	}

	public EstacionesVertex neighbor(Integer a) {
		return null;
	}

	public EstacionesEdge edge(Integer a) {
		return EstacionesEdge.of(this, this.neighbor(a), a);
	}
	
}
