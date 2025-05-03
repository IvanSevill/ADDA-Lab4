package ejercicio2Manual;

import common.AuxCommon;
import common.DatosCursos;
import common.TipoAlgoritmo;

public class Ejercicio2BTManual {
	
	public static final Integer EJERCICIO = 2;
	public static final Integer NUM_ARCHIVOS = 3;
	
	public static void main(String[] args) {
		for (Integer id_fichero = 1; id_fichero <= NUM_ARCHIVOS; id_fichero++) {
			ejecutaBTManual(id_fichero);
		}
	}
	
	private static void ejecutaBTManual(int i) {
		DatosCursos.iniDatos("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt", EJERCICIO,
				TipoAlgoritmo.BT_MANUAL);
		CursoBT.search();
		System.out.println("Solucion obtenida: " + CursoBT.getSolucion());
	}
	
}
