package ejercicio1PDRManual;

import common.AuxCommon;
import common.DatosAlmacenes;
import common.TipoAlgoritmo;

public class Ejercicio1PDRManual {

	public static void main(String[] args) {

		for (Integer id_fichero = 1; id_fichero < DatosAlmacenes.ntest; id_fichero++) {
			ejecutaPDRManual1(id_fichero);
		}
	}

	private static void ejecutaPDRManual1(int i) {
		DatosAlmacenes.iniDatos("resources/ejercicio1/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio1/DatosEntrada" + i + ".txt", 1,
				TipoAlgoritmo.PDR_MANUAL);
		System.out.println("Solucion obtenida: " + AlmacenPDR.search());
	}

}
