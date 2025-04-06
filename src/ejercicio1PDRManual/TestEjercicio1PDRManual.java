package ejercicio1PDRManual;

import common.DatosAlmacenes;

public class TestEjercicio1PDRManual {

	public static void main(String[] args) {

		for (Integer id_fichero = 1; id_fichero < DatosAlmacenes.ntest; id_fichero++) {
			DatosAlmacenes.iniDatos("resources/ejercicio1/DatosEntrada" + id_fichero + ".txt");
			System.out.println("\n\n>\tResultados para el test " + id_fichero + "\n");
			System.out.println("Solucion obtenida: " + AlmacenPDR.search());
		}
	}

}
