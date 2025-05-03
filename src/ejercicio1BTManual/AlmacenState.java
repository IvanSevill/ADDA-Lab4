package ejercicio1BTManual;

import java.util.List;

import ejercicio1.SolucionAlmacen;
import us.lsi.common.List2;

public class AlmacenState {

	AlmacenProblem actual;
	Double acumulado;
	List<Integer> acciones;
	List<AlmacenProblem> anteriores;

	private AlmacenState(AlmacenProblem prob, Double a, List<Integer> ls1, List<AlmacenProblem> ls2) {
		actual = prob;
		acumulado = a;
		acciones = ls1;
		anteriores = ls2;
	}

	public static AlmacenState of(AlmacenProblem prob, Double acum, List<Integer> lsa, List<AlmacenProblem> lsp) {
		return new AlmacenState(prob, acum, lsa, lsp);
	}

	public static AlmacenState initial() {
		AlmacenProblem pi = AlmacenProblem.initial();
		return of(pi, 0., List2.empty(), List2.empty());
	}

	public void forward(Integer a) {
		acciones.add(a);
		acumulado = -actual.cantidadAlmacenada().doubleValue();  // Actualizamos correctamente el acumulado

		anteriores.add(actual);
		actual = actual.neighbor(a);
	}

	public void back() {
		int last = acciones.size() - 1;
		acciones.remove(last);
		actual = anteriores.remove(last);
		acumulado = +actual.cantidadAlmacenada().doubleValue();  // Recalculamos el acumulado
	}

	public List<Integer> alternativas() {
		return actual.actions();
	}

	public Double cota(Integer a) {
		AlmacenProblem vecino = actual.neighbor(a);
		return -vecino.cantidadAlmacenada() + vecino.heuristic(); // Cota para maximizar cantidad almacenada
	}

	public boolean esSolucion() {
		return actual.goal() && actual.goalHasSolution();
	}

	public boolean esTerminal() {
		return actual.goal();
	}

	public SolucionAlmacen getSolucion() {
		return SolucionAlmacen.of(acciones);
	}
}
