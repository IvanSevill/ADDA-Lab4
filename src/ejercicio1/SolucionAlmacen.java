package ejercicio1;

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
		return solucion.entrySet().stream().map(p -> "P" + p.getKey().producto() + ": Almacen " + p.getValue())
				.collect(Collectors.joining("\n",
						"Reparto de productos y almacen en el que se coloca cada uno de ellos:\n",
						String.format("\nProductos colocados: %d", numproductos)));
	}
}