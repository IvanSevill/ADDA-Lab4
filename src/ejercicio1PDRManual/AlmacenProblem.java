package ejercicio1PDRManual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.DatosAlmacenes;
import us.lsi.common.Set2;

public record AlmacenProblem(Integer indice, Integer cantidadAlmacenada, List<Set<Integer>> productosAlmacenados,
		List<Integer> espacioDisponible) {

	public static AlmacenProblem of(Integer indice, Integer cantidadAlmacenada, List<Set<Integer>> productosAlmacenados,
			List<Integer> espacioDisponible) {
		return new AlmacenProblem(indice, cantidadAlmacenada, productosAlmacenados, espacioDisponible);
	}

	public static AlmacenProblem initial() {
		List<Set<Integer>> productosIniciales = new ArrayList<>();
		List<Integer> espacios = new ArrayList<>();

		for (int i = 0; i < DatosAlmacenes.getNumAlmacenes(); i++) {
			productosIniciales.add(new HashSet<>());
			espacios.add(DatosAlmacenes.getMetrosCubicosAlmacen(i));

		}
		return of(0, 0, productosIniciales, espacios);
	}

	public Boolean goal() {
		return indice == DatosAlmacenes.getNumProductos();
	}

	public Boolean goalHasSolution() {
		return true;
	}

	/*
	 * Diferentes almacenes en los que se puede almacenar el producto actual
	 * teniendo en cuenta las decisiones tomadas hasta ahora guardadas en
	 * espacioDisponible y productosAlmacenados
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

	public List<Set<Integer>> copyListSet(List<Set<Integer>> ls) {
		List<Set<Integer>> copy = new ArrayList<>();
		for (Set<Integer> set : ls) {
			Set<Integer> newSet = new HashSet<>(set);
			copy.add(newSet);
		}
		return copy;
	}

	public AlmacenProblem neighbor(Integer action) {
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

		return AlmacenProblem.of(nIndice, nCantidadAlmacenada, nProductosAlmacenados, nEspacioDisponible);
	}

	public static List<Set<Integer>> nonMutableCopyListSet(List<Set<Integer>> ls) {
		List<Set<Integer>> res = new ArrayList<>();
		for (Set<Integer> conj : ls) {
			Set<Integer> conjProd = Set2.copy(conj);
			res.add(conjProd);
		}
		return res;
	}

	public Double heuristic() {
		return indice() < DatosAlmacenes.getNumAlmacenes() ? DatosAlmacenes.getNumProductos() - indice() : 0.;
	}

}
