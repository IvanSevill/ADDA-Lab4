package common;


public class AuxCommon {

	private static final Integer NUMERO_GUROBI_COMMON =140;
	private static final String BLANCO = " ";
	public static final String CARACTER = "-";

	public static void imprimeCabecera(String datosEntrada, Integer ejercicio) {
		System.out.println("\n\n");
		separador(CARACTER, NUMERO_GUROBI_COMMON);
		String s = "Ejecutando ejercicio " + ejercicio + " con datos de entrada: " + datosEntrada;
		Integer espaciosPorLado = (NUMERO_GUROBI_COMMON - s.length()) / 2 - 2;
		System.out.println(" |" + BLANCO.repeat(espaciosPorLado) + s + BLANCO.repeat(espaciosPorLado) + "|");
		separador(CARACTER, NUMERO_GUROBI_COMMON);
	}

	public static void separador(String caracter, Integer n) {
		System.out.println(caracter.repeat(n));
	}

	public static void separador() {
		System.out.println(CARACTER.repeat(NUMERO_GUROBI_COMMON));
	}
}
