package ejercicio4;

import java.util.List;
import java.util.Set;

import common.DatosEstaciones;
import us.lsi.common.List2;

public record EstacionesVertex(Integer indice, List<Integer> camino) implements EstacionesVertexInterface {

	public static EstacionesVertex of(Integer indice, List<Integer> camino) {
		return new EstacionesVertex(indice, camino);
	}

	public Boolean goal() {
		return this.indice == DatosEstaciones.itemsNumber();
	}

	public Boolean goalHasSolution() {
		Boolean b = esSolucion();
		return b;
	}

	@Override
	public List<Integer> actions() {
		List<Integer> alternativas = List2.empty();

		// Ultimo indice
		if (indice == DatosEstaciones.itemsNumber() - 1) {
			Integer vin = camino.get(indice);
			Integer vout = camino.get(0);
			if (DatosEstaciones.existeTramo(vin, vout)) {
				alternativas.add(vout);
			}
			// Caso general
		} else if (indice < DatosEstaciones.itemsNumber()) {
			Integer v = camino.get(indice);
			Set<Integer> vecinos = DatosEstaciones.vecinos(v);
			for (int i = 0; i < indice; i++) {
				vecinos.remove(camino.get(i));
			}
			alternativas = List2.ofCollection(vecinos);
		}
		return alternativas;
	}

	private boolean esSolucion() {

		if (!goal()) { // Esto es un parche para AStart
			return false;
		}
		Double costeTrayectoCerrado = DatosEstaciones.calcularCosteTrayectoCerrado(camino);
		Double costeTrayectoCompleto = DatosEstaciones.getCosteTrayectoCompleto();

		if (costeTrayectoCerrado > 0.75 * costeTrayectoCompleto) {
			return false;
		}

		Integer numEstacionesConsecSatisf = DatosEstaciones.calculaEstacionesConsecSatisf(camino);
		if (numEstacionesConsecSatisf == 0) {
			return false;
		}

		return true;
	}

	@Override
	public EstacionesVertex neighbor(Integer a) {
		List<Integer> caminoAux = List2.copy(camino);
		caminoAux.add(a);
		return EstacionesVertex.of(this.indice + 1, caminoAux);
	}

	@Override
	public EstacionesEdge edge(Integer a) {
		return EstacionesEdge.of(this, this.neighbor(a), a);
	}

	@Override
	public String toString() {
		return "EstacionesVertexI [indice=" + indice + ", camino=" + camino + "]";
	}

	public Object toGraphString() {
		return "EstacionesVertexI [indice=" + indice + ", camino=" + camino + "]";
	}

}
