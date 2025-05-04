package ejercicio3Manual;

import common.AuxCommon;
import common.DatosEstaciones;
import common.DatosFestival;
import common.TipoAlgoritmo;
import ejercicio4Manual.EstacionesBT;

public class Ejercicio3BTManual {
public static final Integer EJERCICIO = 3;

	public static void main(String[] args) {
		for (Integer id_fichero = 1; id_fichero <= AuxCommon.NUM_ARCHIVOS; id_fichero++) {
			ejecutaBTManual(id_fichero);
		}
	}
	
	private static void ejecutaBTManual(int i) {
		DatosFestival.iniDatos("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt", 2,
				TipoAlgoritmo.BT_MANUAL);
		FestivalBT.search();
		System.out.println("Solucion obtenida: " + FestivalBT.getSolucion());
	}
}
