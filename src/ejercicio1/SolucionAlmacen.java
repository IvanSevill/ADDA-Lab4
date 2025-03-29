package ejercicio1;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import common.DatosAlmacenes;
import common.DatosAlmacenes.Producto;

public class SolucionAlmacen {
	// VARIABLES
	private Integer numproductos;
	private Map<Producto, Integer> solucion;

	// CREACIÓN VACIA
	public static SolucionAlmacen empty(List<Integer> ls) {
		return new SolucionAlmacen();
	}

	public SolucionAlmacen() {
		this.numproductos = 0;
		this.solucion = new HashMap<>();
	}

	// CREACIÓN CON LISTA
	public static SolucionAlmacen create(List<Integer> ls) {
		return new SolucionAlmacen(ls);
	}

	private SolucionAlmacen(List<Integer> ls) {
		this.solucion = new HashMap<>();

		for (int i = 0; i < ls.size(); i++) {
			Integer almacen = ls.get(i);
			if (almacen != null && almacen >= 0) {
				Producto producto = DatosAlmacenes.getProducto(i);
				this.solucion.put(producto, almacen);
			}
		}

		this.numproductos = solucion.size();
	}

	@Override
	public String toString() {
		String reparto = solucion.entrySet().stream()
			.sorted(Comparator.comparing(e -> e.getKey().producto()))
			.map(p -> p.getKey().producto() + ": Almacen " + p.getValue()
				+ " (" + p.getKey().metroscubicosrequeridos() + "/"
				+ DatosAlmacenes.getMetrosCubicosAlmacen(p.getValue()) + ")")
			.collect(Collectors.joining("\n",
				"Reparto de productos y almacen en el que se coloca cada uno de ellos:\n\n",
				"\n"));

		// Calcular ocupación por almacén
		Map<Integer, Integer> ocupacion = solucion.entrySet().stream()
			.collect(Collectors.groupingBy(
				e -> e.getValue(),
				Collectors.summingInt(e -> e.getKey().metroscubicosrequeridos())
			));

		// Resumen por almacén
		String resumen = ocupacion.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(e -> String.format("Almacén %d → %d / %d",
				e.getKey(),
				e.getValue(),
				DatosAlmacenes.getMetrosCubicosAlmacen(e.getKey())))
			.collect(Collectors.joining("\n"));

		// Totales generales
		int volumenTotal = ocupacion.values().stream().mapToInt(Integer::intValue).sum();
		int capacidadTotal = 0;
		for (int i = 0; i < DatosAlmacenes.getNumAlmacenes(); i++) {
			capacidadTotal += DatosAlmacenes.getMetrosCubicosAlmacen(i);
		}

		String totales = String.format(
			"\n\nProductos colocados: %d\nVolumen ocupado: %d / %d",
			numproductos, volumenTotal, capacidadTotal);

		return reparto +"\n"+ resumen + totales;
	}


}