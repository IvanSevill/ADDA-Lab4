package ejercicio3Manual;

import ejercicio3.SolucionFestival;

public class FestivalBT {
	private static Double mejorValor;
	private static FestivalState estado;
	private static SolucionFestival solucion;
	
	public static void search() {
		solucion = null;
		mejorValor = Double.MAX_VALUE;
		estado = FestivalState.initial();
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

	public static SolucionFestival getSolucion() {
		return solucion;
	}
}
