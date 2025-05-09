package ejercicio3Manual;

import common.AuxCommon;
import common.DatosEstaciones;
import common.DatosFestival;
import common.TipoAlgoritmo;
import ejercicio4Manual.EstacionesPDR;

public class Ejercicio3PDRManual {
public static final Integer EJERCICIO = 3;
	
	public static void main(String[] args) {
		for (Integer id_fichero = 1; id_fichero <= AuxCommon.NUM_ARCHIVOS; id_fichero++) {
			ejecutaPDRManual(id_fichero);
		}
	}

	private static void ejecutaPDRManual(int i) {
		DatosFestival.iniDatos("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt", EJERCICIO,
				TipoAlgoritmo.PDR_MANUAL);
		System.out.println("Solucion obtenida: " + FestivalPDR.search());
	}
}
