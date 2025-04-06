package ejercicio1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.DatosAlmacenes;
import us.lsi.common.Set2;

// Indice: por que elemento voy de la iteración
// ProductosAlmacenados: Por cada almacén, productos que se almacenan en él 
// Espacio dispobile: m3 disponibles en cada almacén

public record AlmacenVertex(Integer indice, Integer cantidadAlmacenada, List<Set<Integer>> productosAlmacenados,
		List<Integer> espacioDisponible) implements AlmacenVertexInterface {

	public static AlmacenVertex of(Integer indice, Integer cantidadAlmacenada, List<Set<Integer>> productosAlmacenados,
			List<Integer> espacioDisponible) {
		return new AlmacenVertex(indice, cantidadAlmacenada, productosAlmacenados, espacioDisponible);
	}

	public Boolean goal() {
		return indice == DatosAlmacenes.getNumProductos();
	}

	public Boolean goalHasSolution() {
		return true;
	}

	/**
	 * Devuelve los almacenes en los que se puede almacenar el producto actual,
	 * teniendo en cuenta el espacio disponible y las incompatibilidades.
	 */
	public List<Integer> actions() {
		List<Integer> acciones = new ArrayList<>();
		acciones.add(-1); // opción de no almacenar el producto (siempre disponible)

		if (this.indice >= DatosAlmacenes.getNumProductos()) {
			return acciones; // No hay más productos que asignar
		}

		int productoActual = this.indice;
		int volumenProducto = DatosAlmacenes.getMetrosCubicosProducto(productoActual);

		for (int i = 0; i < DatosAlmacenes.getNumAlmacenes(); i++) {
			if (espacioDisponible.get(i) >= volumenProducto) {
				boolean incompatible = productosAlmacenados.get(i).stream()
						.anyMatch(p -> sonIncompatiblesBidireccional(p, productoActual));

				if (!incompatible) {
					acciones.add(0, i);
				}
			}
		}

		return acciones;
	}

	// ( Está mal los datos y tienes que comprobar en los dos sentidos )
	private boolean sonIncompatiblesBidireccional(Integer p1, Integer p2) {
		return DatosAlmacenes.sonIncompatibles(p1, p2) || DatosAlmacenes.sonIncompatibles(p2, p1);
	}

	/**
	 * Aplica la acción de almacenar el producto en el almacén que indique action.
	 * Sabemos que la siguiente accion tiene un indice ++, que la cantidad
	 * almacenada aumenta en 1 si es que se guarda y que los productos almacenados y
	 * el espacio disponible se actualizan en función de la acción tomada
	 */

	public AlmacenVertexInterface neighbor(Integer action) {
		Integer nIndice = this.indice + 1; // Siempre aumento el indice
		Integer nCantidadAlmacenada = this.cantidadAlmacenada;
		List<Set<Integer>> nProductosAlmacenados = nonMutableCopyListSet(this.productosAlmacenados);
		List<Integer> nEspacioDisponible = new ArrayList<>(this.espacioDisponible);

		// Si lo almaceno en alguno de los almacenes, actualizo el resto de variables
		// del estado
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

	public static List<Set<Integer>> nonMutableCopyListSet(List<Set<Integer>> ls) {
		List<Set<Integer>> res = new ArrayList<>();
		for (Set<Integer> conj : ls) {
			Set<Integer> conjProd = Set2.copy(conj);
			res.add(conjProd);
		}
		return res;
	}

	public AlmacenEdge edge(Integer a) {
		return AlmacenEdge.of(this, this.neighbor(a), a);
	}

	@Override
	public Integer cantidadAlmacenada() {
		return this.cantidadAlmacenada;
	}

	@Override
	public Double accionReal() {
		return null;
	}
	
	public String toString() {
		return "AlmacenesVertexI [indice=" + indice + ", productosAlmacenados=" + productosAlmacenados
				+ ", espacioDisponible=" + espacioDisponible + ", cantidadAlmacenda=" + cantidadAlmacenada + "]";
	}

	@Override
	public String toGraphString() {
		return "P"+ this.indice() + 
				"\nAlmacenes: " + this.productosAlmacenados().toString() +
				"\nEspacios: " + this.espacioDisponible().toString() + 
				"\n Cantidad A: " + this.cantidadAlmacenada().toString();
	}

}
