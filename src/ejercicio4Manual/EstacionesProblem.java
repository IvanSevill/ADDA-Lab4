package ejercicio4Manual;

import java.util.List;
import java.util.Set;

import common.DatosEstaciones;
import us.lsi.common.List2;

public record EstacionesProblem(Integer indice, List<Integer> camino) {

	public static EstacionesProblem initial() {
		List<Integer> camino = List2.empty();
		camino.add(0); // Nodo donde se inicia el camnino
		return EstacionesProblem.of(0, camino);
	}

	public static EstacionesProblem of(Integer indice, List<Integer> camino) {
		return new EstacionesProblem(indice, camino);
	}

	public Boolean goal() {
		return this.indice == DatosEstaciones.itemsNumber();
	}

	public Boolean goalHasSolution() {
		return esSolucion();
	}

	public List<Integer> actions() {
		List<Integer> alternativas = List2.empty();

		if (indice == DatosEstaciones.itemsNumber() - 1) {
			Integer vin = camino.get(indice);
			Integer vout = camino.get(0);
			if (DatosEstaciones.existeTramo(vin, vout)) {
				alternativas.add(vout);
			}
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

	public EstacionesProblem neighbor(Integer a) {
		List<Integer> caminoAux = List2.copy(camino);
		caminoAux.add(a);
		return EstacionesProblem.of(this.indice + 1, caminoAux);
	}

	public Double heuristic() {
		Double h = 0.;
		return h;
	}

	@Override
	public String toString() {
		return "EstacionesVertexI [indice=" + indice + ", camino=" + camino + "]";
	}

}
