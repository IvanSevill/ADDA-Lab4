package ejercicio4Manual;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.DatosEstaciones;
import us.lsi.common.List2;

public class EstacionesProblem {

	private List<Integer> camino;
	private Integer actual;
	private Set<Integer> visitados;

	public EstacionesProblem(List<Integer> camino, Integer actual, Set<Integer> visitados) {
		this.camino = camino;
		this.actual = actual;
		this.visitados = visitados;
	}

	public static EstacionesProblem initial() {
		List<Integer> camino = List2.of(0);
		Set<Integer> visitados = new HashSet<>();
		visitados.add(0);
		return new EstacionesProblem(camino, 0, visitados);
	}

	public List<Integer> getCamino() {
		return camino;
	}

	public Integer indice() {
		return actual;
	}

	public boolean goal() {
		return visitados.size() == DatosEstaciones.itemsNumber()
				&& actual == 0
				&& camino.size() == DatosEstaciones.itemsNumber() + 1;
	}

	public boolean goalHasSolution() {
		if (!goal()) return false;
		double coste = DatosEstaciones.calcularCosteTrayectoCerrado(camino);
		double limite = DatosEstaciones.getCosteTrayectoCompleto() * 0.75;
		int numConsecSatisf = DatosEstaciones.calculaEstacionesConsecSatisf(camino);
		return coste <= limite && numConsecSatisf > 0;
	}

	public List<Integer> actions() {
		Set<Integer> vecinos = DatosEstaciones.vecinos(actual);
		return vecinos.stream()
				.filter(i -> !visitados.contains(i) || (visitados.size() == DatosEstaciones.itemsNumber() && i == 0))
				.filter(i -> DatosEstaciones.existeTramo(actual, i))
				.toList();
	}

	public EstacionesProblem neighbor(Integer a) {
		List<Integer> nuevoCamino = List2.copy(camino);
		Set<Integer> nuevosVisitados = new HashSet<>(visitados);
		nuevoCamino.add(a);
		if (a != 0) nuevosVisitados.add(a);
		return new EstacionesProblem(nuevoCamino, a, nuevosVisitados);
	}

	public double heuristic() {
		int restantes = DatosEstaciones.itemsNumber() - visitados.size();
		return restantes * DatosEstaciones.menorPesoArista;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EstacionesProblem other)) return false;
		return camino.equals(other.camino);
	}

	@Override
	public int hashCode() {
		return camino.hashCode();
	}

	@Override
	public String toString() {
		return "EstacionesProblem{" + "camino=" + camino + '}';
	}
}
