package ejercicio4;

import java.util.function.Predicate;

import common.DatosEstaciones;

public class EstacionesHeuristic {
	public static Double noHeuristic(EstacionesVertexInterface v1, Predicate<EstacionesVertexInterface> goal,
			EstacionesVertexInterface v2) {
		return 0.0;
	}

	public static Double heuristic(EstacionesVertexInterface v1, Predicate<EstacionesVertexInterface> goal,
			EstacionesVertexInterface v2) {
		Double h = 0.0;
		if (v1.indice() < DatosEstaciones.n) {
			Integer aristasRestantes = DatosEstaciones.n - v1.indice() + 1 ;
			h = aristasRestantes * DatosEstaciones.menorPesoArista;
		}
		return h;
	}
}
