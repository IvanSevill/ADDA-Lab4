package ejercicio2;

import java.util.function.Predicate;

import common.DatosCursos;

public class CursoHeuristic {
	public static Double heuristic(CursoVertexInterface v1, Predicate<CursoVertexInterface> goal, CursoVertexInterface v2) {
		Double h = 0.;
		if (v1.indice() < DatosCursos.getNumCursos()) {
			for (int i = v1.indice(); i < DatosCursos.getNumCursos(); i++) {
			    h += DatosCursos.getCurso(i).relevancia();
			}
		} 
		return h;	}
}
