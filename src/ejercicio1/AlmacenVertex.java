package ejercicio1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.DatosAlmacenes;

// Indice: por que elemento voy de la iteración
// ProductosAlmacenados: Por cada almacén, productos que se almacenan en él 
// Espacio dispobile: m3 disponibles en cada almacén

public record AlmacenVertex(Integer indice, Integer cantidadAlmacenada, List<Set<Integer>> productosAlmacenados,
		List<Integer> espacioDisponible) implements AlmacenVertexInterface {

	public static AlmacenVertex of(Integer indice, Integer cantidadAlmacenada, List<Set<Integer>> productosAlmacenados,
			List<Integer> espacioDisponible) {
		return new AlmacenVertex(indice, cantidadAlmacenada, productosAlmacenados, espacioDisponible);
	}

	public static AlmacenVertex initial() {
		List<Set<Integer>> productosIniciales = new ArrayList<>();
		List<Integer> espacios = new ArrayList<>();

		for (int i = 0; i < DatosAlmacenes.getNumAlmacenes(); i++) {
			productosIniciales.add(new HashSet<>());
			espacios.add(DatosAlmacenes.getMetrosCubicosAlmacen(i));

		}

		return of(0, 0, productosIniciales, espacios);
	}

	public AlmacenEdge edge(Integer a) {
		return AlmacenEdge.of(this, this.neighbor(a), a);
	}

	public Boolean goal() {
		return indice == DatosAlmacenes.getNumProductos();
	}


	public List<Integer> actions() {
		if (this.indice >= DatosAlmacenes.getNumProductos()) {
			return List.of(); // No hay más productos que asignar
		}

		List<Integer> acciones = new ArrayList<>();
		int productoActual = this.indice;
		int volumenProducto = DatosAlmacenes.getMetrosCubicosProducto(productoActual);

		for (int i = 0; i < DatosAlmacenes.getNumAlmacenes(); i++) {
			if (espacioDisponible.get(i) >= volumenProducto) {
				boolean incompatible = productosAlmacenados.get(i).stream()
						.anyMatch(p -> DatosAlmacenes.sonIncompatibles(p, productoActual)
								|| DatosAlmacenes.sonIncompatibles(productoActual, p));
				if (!incompatible) {
					acciones.add(i);
				}
			}
		}

		acciones.add(-1); // opción de no almacenar el producto
		return acciones;
	}


	public AlmacenVertex neighbor(Integer action) {

		Integer nIndice = indice + 1;
		Integer nCantidadAlmacenada = this.cantidadAlmacenada;
		List<Set<Integer>> nProductosAlmacenados = new ArrayList<>(this.productosAlmacenados);
		List<Integer> nEspacioDisponible = new ArrayList<>(this.espacioDisponible);

		if (action >= 0) {
			// Incrementar la cantidad almacenada
			nCantidadAlmacenada++;

			// Añadir el producto al conjunto correspondiente del almacén
			Set<Integer> conjuntoAlmacen = new HashSet<>(nProductosAlmacenados.get(action));
			conjuntoAlmacen.add(indice);
			nProductosAlmacenados.set(action, conjuntoAlmacen);

			// Actualizar el espacio disponible en ese almacén
			int espacioRestante = nEspacioDisponible.get(action) - DatosAlmacenes.getMetrosCubicosProducto(indice);
			nEspacioDisponible.set(action, espacioRestante);
		}

		return AlmacenVertex.of(nIndice, nCantidadAlmacenada, nProductosAlmacenados, nEspacioDisponible);
	}

	public Double heuristic() {
		int posibles = 0;

		for (int i = indice; i < DatosAlmacenes.getNumProductos(); i++) {
			int volumen = DatosAlmacenes.getMetrosCubicosProducto(i);
			final int producto = i; // 👈 necesario para usar en lambda

			for (int j = 0; j < espacioDisponible.size(); j++) {
				if (espacioDisponible.get(j) >= volumen) {

					boolean compatible = productosAlmacenados.get(j).stream()
						.noneMatch(p -> DatosAlmacenes.sonIncompatibles(p, producto)
						              || DatosAlmacenes.sonIncompatibles(producto, p));

					if (compatible) {
						posibles++;
						break;
					}
				}
			}
		}

		return (double) posibles;
	}



}
