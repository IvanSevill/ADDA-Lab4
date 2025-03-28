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
		int totalProductos = DatosAlmacenes.getNumProductos();

		for (Integer i = 0; i < ls.size(); i++) {
			if (ls.get(i) > 0) {
				Producto producto = DatosAlmacenes.getProducto(i % totalProductos);
				Integer almacen = i / totalProductos;
				// Verificar si el producto ya está asignado
				if (!solucion.containsKey(producto)) {
					this.solucion.put(producto, almacen);
				}
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