package ejercicio1;

import java.util.List;
import java.util.Set;

import us.lsi.graphs.virtual.VirtualVertex;

public interface AlmacenVertexInterface extends VirtualVertex<AlmacenVertexInterface, AlmacenEdge, Integer> {

	Integer indice();

	Double heuristic();

	static AlmacenVertex initial() {
        return AlmacenVertex.of(0, 0, List.of(Set.of()), List.of());
    }
}
