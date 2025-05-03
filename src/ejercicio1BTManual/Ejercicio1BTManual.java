package ejercicio1BTManual;

import common.AuxCommon;
import common.DatosAlmacenes;
import common.TipoAlgoritmo;

public class Ejercicio1BTManual {
	public static final Integer EJERCICIO = 1;
	public static final Integer NUM_ARCHIVOS = 3;
	
	public static void main(String[] args) {
		for (Integer id_fichero = 1; id_fichero <= NUM_ARCHIVOS; id_fichero++) {
			ejecutaBTManual(id_fichero);
		}
	}
	
	private static void ejecutaBTManual(int i) {
		DatosAlmacenes.iniDatos("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt");
		AuxCommon.imprimeCabeceraAlgoritmo("resources/ejercicio"+EJERCICIO+"/DatosEntrada" + i + ".txt", EJERCICIO,
				TipoAlgoritmo.BT_MANUAL);
		AlmacenBT.search();
		System.out.println("Solucion obtenida: " + AlmacenBT.getSolucion());
	}

}
