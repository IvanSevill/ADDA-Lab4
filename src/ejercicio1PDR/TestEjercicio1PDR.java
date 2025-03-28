package ejercicio1PDR;

import common.DatosAlmacenes;
import common.AuxCommon;

public class TestEjercicio1PDR {
	public static final Integer FILE_NUM = 3;

	public static void main(String[] args) {
		for (Integer i = 1; i <= FILE_NUM; i++) {
			DatosAlmacenes.iniDatos("resources/ejercicio1/DatosEntrada" + i + ".txt");
			AuxCommon.imprimeCabecera("resources/ejercicio1/DatosEntrada" + i + ".txt", 1);
			System.out.println(Ejercicio1PDR.search());
			AuxCommon.separador();
		}

	}

}
