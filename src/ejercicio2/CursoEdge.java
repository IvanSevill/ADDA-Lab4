package ejercicio2;

import common.DatosCursos;
import us.lsi.graphs.virtual.SimpleEdgeAction;

public record CursoEdge(CursoVertexInterface source, CursoVertexInterface target, Integer action, Double weight)
		implements SimpleEdgeAction<CursoVertexInterface, Integer> {

	public static CursoEdge of(CursoVertexInterface c1, CursoVertexInterface c2, Integer action) {
		Double relevancia = c1.indice() < DatosCursos.getNumCursos() 
				? DatosCursos.getCurso(c1.indice()).relevancia()
				: 0.0;
		return new CursoEdge(c1, c2, action, relevancia * action);
	}

}
