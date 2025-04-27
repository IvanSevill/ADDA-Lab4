package ejercicio4BTManual;

import ejercicio4.SolucionEstaciones;

public class EstacionesBT {

	private static Double mejorValor;
	private static EstacionesState estado;
	private static SolucionEstaciones solucion;

	public static void search() {
		solucion = null;
		mejorValor = Double.MAX_VALUE;
		estado = EstacionesState.initial();
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

	public static SolucionEstaciones getSolucion() {
		return solucion;
	}
}
