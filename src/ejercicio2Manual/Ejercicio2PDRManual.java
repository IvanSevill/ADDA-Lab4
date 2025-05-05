package ejercicio2Manual;

import common.AuxCommon;
import common.DatosCursos;
import common.TipoAlgoritmo;
import ejercicio4Manual.EstacionesPDR;


public class Ejercicio2PDRManual {

	public static final Integer EJERCICIO = 2;
	public static final Integer NUM_ARCHIVOS = 3;
	
	public static void main(String[] args) {
		for (Integer id_fichero = 1; id_fichero <= NUM_ARCHIVOS; id_fichero++) {
			ejecutaPDRManual(id_fichero);
		}
	}

	private static void ejecutaPDRManual(int i) {
		DatosCursos.iniDatos("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt", EJERCICIO,
				TipoAlgoritmo.PDR_MANUAL);
		System.out.println("Solucion obtenida: " + CursoPDR.search());
	}
}
