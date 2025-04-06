package ejercicio2PDRManual;

import common.DatosCursos;

public class Ejercicio2PDRManual {

	public static void main(String[] args) {

		for (Integer id_fichero = 1; id_fichero <= DatosCursos.ntest; id_fichero++) {
			DatosCursos.iniDatos("resources/ejercicio2/DatosEntrada" + id_fichero + ".txt");
			System.out.println("\n\n>\tResultados para el test " + id_fichero + "\n");
			System.out.println("Solucion obtenida: " + CursoPDR.search());
		}
	}

}
