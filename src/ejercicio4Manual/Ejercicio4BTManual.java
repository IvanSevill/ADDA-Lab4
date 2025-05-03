package ejercicio4Manual;

import common.AuxCommon;
import common.DatosCursos;
import common.DatosEstaciones;
import common.TipoAlgoritmo;

public class Ejercicio4BTManual {
	public static final Integer EJERCICIO = 4;

	
	public static void main(String[] args) {
		for (Integer id_fichero = 1; id_fichero <= DatosCursos.ntest; id_fichero++) {
			ejecutaBTManual(id_fichero);
		}
	}
	
	private static void ejecutaBTManual(int i) {
		DatosEstaciones.iniDatos("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt", 2,
				TipoAlgoritmo.BT_MANUAL);
		EstacionesBT.search();
		System.out.println("Solucion obtenida: " + EstacionesBT.getSolucion());
	}


}
