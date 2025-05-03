package ejercicio2Manual;

import common.AuxCommon;
import common.DatosCursos;
import common.TipoAlgoritmo;

public class Ejercicio2PDRManual {

	public static void main(String[] args) {
		for (Integer id_fichero = 1; id_fichero <= DatosCursos.ntest; id_fichero++) {
			ejecutaPDRManual(id_fichero);
		}
	}

	private static void ejecutaPDRManual(int i) {
		DatosCursos.iniDatos("resources/ejercicio2/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio2/DatosEntrada" + i + ".txt", 2,
				TipoAlgoritmo.PDR_MANUAL);
		System.out.println("Solucion obtenida: " + CursoPDR.search());
	}
}
