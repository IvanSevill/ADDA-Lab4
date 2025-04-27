package ejercicio4BTManual;

import java.util.List;

import common.DatosEstaciones;
import ejercicio4.SolucionEstaciones;
import ejercicio4PDRManual.EstacionesProblem;
import us.lsi.common.List2;

public class EstacionesState {

	EstacionesProblem actual;
	Double acumulado;
	List<Integer> acciones;
	List<EstacionesProblem> anteriores;

	private EstacionesState(EstacionesProblem p, Double a, List<Integer> ls1, List<EstacionesProblem> ls2) {
		actual = p;
		acumulado = a;
		acciones = ls1;
		anteriores = ls2;
	}

	public static EstacionesState of(EstacionesProblem prob, Double acum, List<Integer> lsa,
			List<EstacionesProblem> lsp) {
		return new EstacionesState(prob, acum, lsa, lsp);
	}

	public static EstacionesState initial() {
		EstacionesProblem pi = EstacionesProblem.initial();
		return of(pi, 0., List2.empty(), List2.empty());
	}

	public void forward(Integer a) {
		Integer v = actual.camino().get(actual.indice());
		Double tiempoMedioTramo = 0.0;

		if (actual.indice() < DatosEstaciones.itemsNumber()) {
			tiempoMedioTramo = DatosEstaciones.calculaTiempoMedioTramo(v, a);
		}

		acumulado += tiempoMedioTramo;
		acciones.add(a);
		anteriores.add(actual);
		actual = actual.neighbor(a);
	}

	public void back() {
		int last = acciones.size() - 1;
		var problemaAnterior = anteriores.get(last);
		Integer a = actual.camino().get(actual.indice());

		Integer v = problemaAnterior.camino().get(problemaAnterior.indice());
		Double tiempoMedioTramo = 0.0;

		if (actual.indice() < DatosEstaciones.itemsNumber()) {
			tiempoMedioTramo = DatosEstaciones.calculaTiempoMedioTramo(v, a);
		}

		acumulado -= tiempoMedioTramo;
		acciones.remove(last);
		anteriores.remove(last);
		actual = problemaAnterior;
	}

	public List<Integer> alternativas() {
		return actual.actions();
	}

	public Double cota(Integer a) {
		Double weight = 0.;
		EstacionesProblem nextProblem = actual.neighbor(a);
		Double tiempoMedioTramo = 0.0;
		
		// Caso general
		if (nextProblem.indice() < DatosEstaciones.itemsNumber()) {
			Integer vin = actual.camino().get(actual.indice());
			Integer vout = nextProblem.camino().get(nextProblem.indice());
			tiempoMedioTramo = (double) DatosEstaciones.calculaTiempoMedioTramo(vin, vout);
		// Caso final
		} else {
			Integer vin = actual.camino().get(actual.indice());
			Integer vout = nextProblem.camino().get(0);
			tiempoMedioTramo = (double) DatosEstaciones.calculaTiempoMedioTramo(vin, vout);
		}
		
		weight = tiempoMedioTramo;
		return acumulado + weight + nextProblem.heuristic();
	}

	public Boolean esSolucion() {
		return actual.goal() && actual.goalHasSolution();
	}

	public boolean esTerminal() {
		return actual.goal();
	}

	public SolucionEstaciones getSolucion() {
		return SolucionEstaciones.of(acciones);
	}

}
