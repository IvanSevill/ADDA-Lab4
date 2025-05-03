package common;

public class AuxCommon {

	private static final Integer NUMERO_COMMON =140;
	private static final String BLANCO = " ";
	public static final String CARACTER = "-";
	public static final Integer NUM_ARCHIVOS = 3;

	public static void imprimeCabecera(String input) {
		separador(CARACTER, NUMERO_COMMON);
		String s = input;
		Integer espaciosPorLado = (NUMERO_COMMON - s.length()) / 2 - 2;
		System.out.println(" |" + BLANCO.repeat(espaciosPorLado) + s + BLANCO.repeat(espaciosPorLado) + "|");
		separador(CARACTER, NUMERO_COMMON);		
	}
	
	public static void imprimeCabecera(String datosEntrada, Integer ejercicio) {
		System.out.println("\n\n");
		separador(CARACTER, NUMERO_COMMON);
		String s = "Ejecutando ejercicio " + ejercicio + " con datos de entrada: " + datosEntrada;
		Integer espaciosPorLado = (NUMERO_COMMON - s.length()) / 2 - 2;
		System.out.println(" |" + BLANCO.repeat(espaciosPorLado) + s + BLANCO.repeat(espaciosPorLado) + "|");
		separador(CARACTER, NUMERO_COMMON);
	}
	
	public static void imprimeCabeceraAlgoritmo(String datosEntrada, Integer ejercicio, TipoAlgoritmo algoritmo) {
		System.out.println("\n\n");
		separador(CARACTER, NUMERO_COMMON);
		String s = "Ejecutando "+algoritmo+" | Ejercicio " + ejercicio + " con datos de entrada: " + datosEntrada;
		Integer espaciosPorLado = (NUMERO_COMMON - s.length()) / 2 - 2;
		System.out.println(" |" + BLANCO.repeat(espaciosPorLado) + s + BLANCO.repeat(espaciosPorLado) + "|");
		separador(CARACTER, NUMERO_COMMON);
	}

	public static void separador(String caracter, Integer n) {
		System.out.println(caracter.repeat(n));
	}

	public static void separador() {
		System.out.println(CARACTER.repeat(NUMERO_COMMON));
	}


}
