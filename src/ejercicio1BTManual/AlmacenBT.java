package ejercicio1BTManual;

import ejercicio1.SolucionAlmacen;

public class AlmacenBT {

	private static Double mejorValor;
	private static AlmacenState estado;
	private static SolucionAlmacen solucion;

	public static void search() {
		solucion = null;
		mejorValor = Double.MAX_VALUE;
		estado = AlmacenState.initial();
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

	public static SolucionAlmacen getSolucion() {
		return solucion;
	}

}
