package ejercicio4Manual;

import common.AuxCommon;
import common.DatosEstaciones;
import common.TipoAlgoritmo;
import ejercicio2Manual.CursoPDR;

public class Ejercicio4PDRManual {
	public static final Integer EJERCICIO = 4;
	
	public static void main(String[] args) {
		for (Integer id_fichero = 1; id_fichero <= AuxCommon.NUM_ARCHIVOS; id_fichero++) {
			ejecutaPDRManual(id_fichero);
		}
	}

	private static void ejecutaPDRManual(int i) {
		DatosEstaciones.iniDatos("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt", EJERCICIO,
				TipoAlgoritmo.PDR_MANUAL);
		System.out.println("Solucion obtenida: " + EstacionesPDR.search());
	}
	
}
