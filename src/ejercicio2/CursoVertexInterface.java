package ejercicio2;

import java.util.List;

import common.DatosCursos;
import us.lsi.common.List2;
import us.lsi.common.Set2;
import us.lsi.graphs.virtual.VirtualVertex;

public interface CursoVertexInterface extends VirtualVertex<CursoVertexInterface, CursoEdge, Integer> {

	Integer indice();
	Integer presupuestoRestante();
	Boolean goal();
	Boolean goalHasSolution();
	List<Integer> cursosSeleccionados();

	public static CursoVertexInterface start() {
		return CursoVertex.of(0, List2.empty(), DatosCursos.getPresupuestoTotal(), Set2.empty());
	}
}
