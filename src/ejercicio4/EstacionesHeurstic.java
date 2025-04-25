package ejercicio4;

import java.util.function.Predicate;

import common.DatosEstaciones;

public class EstacionesHeurstic {
	public static Double noHeuristic(EstacionesVertexInterface v1, Predicate<EstacionesVertex> goal,
			EstacionesVertexInterface v2) {
		return 0.0;
	}

	public static Double heuristic(EstacionesVertexInterface v1, Predicate<EstacionesVertex> goal,
			EstacionesVertexInterface v2) {
		Double h = 0.0;
		if (v1.indice() < DatosEstaciones.n) {
			Integer aristasRestantes = DatosEstaciones.n - v1.indice();
			h = aristasRestantes * DatosEstaciones.menorPesoArista;
		}
		return h;
	}
}
