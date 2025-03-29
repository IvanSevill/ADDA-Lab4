package ejercicio2PDR;

import common.AuxCommon;
import common.DatosAlmacenes;
import ejercicio2.SolucionCursos;

public class TestEjercicio2PDR {
	public static final Integer FILE_NUM = 3;

	public static void main(String[] args) {
		for (Integer i = 1; i <= FILE_NUM; i++) {
			DatosAlmacenes.iniDatos("resources/ejercicio2/DatosEntrada" + i + ".txt");
			AuxCommon.imprimeCabecera("resources/ejercicio2/DatosEntrada" + i + ".txt", 1);

			Ejercicio2PDR solver = new Ejercicio2PDR();
			SolucionCursos solucion = solver.search();

			System.out.println(solucion);
			AuxCommon.separador();
		}

	}
}
