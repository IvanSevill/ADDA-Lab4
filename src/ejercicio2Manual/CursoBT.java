package ejercicio2Manual;

import ejercicio2.SolucionCursos;

public class CursoBT {

	private static Double mejorValor;
	private static CursoState estado;
	private static SolucionCursos solucion;
	
	public static void search() {
		solucion = null;
		mejorValor = Double.MAX_VALUE;
		estado = CursoState.initial();
		bt_search();
	}
	
	private static void bt_search() {
		if (estado.esSolucion()) {
			Double valorObtenido = estado.acumulado;
			if (valorObtenido < mejorValor) {
				mejorValor = valorObtenido;
				solucion = estado.getSolucion();
			}
		} else if (!estado.esTerminal()) {
			for (Integer a : estado.alternativas()) {
				if (estado.cota(a) < mejorValor) {
					estado.forward(a);
					bt_search();
					estado.back();
				}
			}
		}
	}
	
	public static SolucionCursos getSolucion() {
		return solucion;
	}
	
	
}
