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

	/*
	 * Diferentes almacenes en los que se puede almacenar el producto actual
	 * teniendo en cuenta las decisiones tomadas hasta ahora guardadas en
	 * espacioDisponible y productosAlmacenados
	 */
	public List<Integer> actions() {
		if (this.indice >= DatosAlmacenes.getNumProductos()) {
			return List.of(); // No hay más productos que asignar
		}

		List<Integer> acciones = new ArrayList<>();
		int productoActual = this.indice;
		int volumenProducto = DatosAlmacenes.getMetrosCubicosProducto(productoActual);

		for (int i = 0; i < DatosAlmacenes.getNumAlmacenes(); i++) {
			// Si el volumen del almacen es mayor o igual que el volumen del producto
			if (espacioDisponible.get(i) >= volumenProducto) {
				// ( El csv está fatal, hay que comprobar las incompatibilidades de los dos lados )
				boolean incompatible = productosAlmacenados.get(i).stream()
						.anyMatch(p -> DatosAlmacenes.sonIncompatibles(p, productoActual)
								|| DatosAlmacenes.sonIncompatibles(productoActual, p));
				// Añado esta opción porque si que es una opción valida
				if (!incompatible) {
					acciones.add(i);
				}
			}
		}

		acciones.add(-1); // opción de no almacenar el producto que está siempre
		return acciones;
	}

	/**
	 * Aplica la acción de almacenar el producto en el almacén que indique action.
	 * Sabemos que la siguiente accion tiene un indice ++, que la cantidad
	 * almacenada aumenta en 1 si es que se guarda y que los productos almacenados y
	 * el espacio disponible se actualizan en función de la acción tomada
	 */

	public AlmacenVertex neighbor(Integer action) {

		Integer nIndice = indice + 1; // Siempre aumento en uno el índice
		Integer nCantidadAlmacenada = this.cantidadAlmacenada;
		List<Set<Integer>> nProductosAlmacenados = new ArrayList<>(this.productosAlmacenados);
		List<Integer> nEspacioDisponible = new ArrayList<>(this.espacioDisponible);

		// Si lo almaceno en alguno de los almacenes, actualizo el resto de variables del estado
		if (action > -1) {
			// Incrementar la cantidad almacenada en 1
			nCantidadAlmacenada++;

			// Añadir el producto al conjunto correspondiente del almacén
			Set<Integer> conjuntoAlmacen = new HashSet<>(nProductosAlmacenados.get(action));
			conjuntoAlmacen.add(this.indice);
			nProductosAlmacenados.set(action, conjuntoAlmacen);

			// Actualizar el espacio disponible en ese almacén
			int espacioRestante = nEspacioDisponible.get(action) - DatosAlmacenes.getMetrosCubicosProducto(this.indice);
			nEspacioDisponible.set(action, espacioRestante);
		}

		return AlmacenVertex.of(nIndice, nCantidadAlmacenada, nProductosAlmacenados, nEspacioDisponible);
	}

	/*
	 * La heuristica represnta el NUMERO de almacenes diferentes se puede almacenar el
	 * producto actual teniendo en cuenta las decisiones tomadas hasta ahora
	 */
	public Double heuristic() {
		int posibles = 0;

		for (int i = indice; i < DatosAlmacenes.getNumProductos(); i++) {
			int volumen = DatosAlmacenes.getMetrosCubicosProducto(i);
			final int producto = i; // 👈 necesario para usar en lambda

			for (int j = 0; j < espacioDisponible.size(); j++) {
				if (espacioDisponible.get(j) >= volumen) {

					Boolean compatible = productosAlmacenados.get(j).stream()
							.noneMatch(k -> DatosAlmacenes.sonIncompatibles(k, producto)
									|| DatosAlmacenes.sonIncompatibles(producto, k));

					// Si es compatible y cabe en el almacén, sumamos 1
					if (compatible) {
						posibles++;
					}
				}
			}
		}

		return (double) posibles;
	}

}
