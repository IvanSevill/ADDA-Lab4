package ejercicio3Manual;

import java.util.List;

import common.DatosFestival;
import ejercicio3.SolucionFestival;
import us.lsi.common.List2;

public class FestivalState {
	FestivalProblem actual;
	Double acumulado;
	List<Integer> acciones;
	List<FestivalProblem> anteriores;

	private FestivalState(FestivalProblem p, Double a, List<Integer> ls1, List<FestivalProblem> ls2) {
		actual = p;
		acumulado = a;
		acciones = ls1;
		anteriores = ls2;
	}

	public static FestivalState of(FestivalProblem prob, Double acum, List<Integer> lsa, List<FestivalProblem> lsp) {
		return new FestivalState(prob, acum, lsa, lsp);
	}

	public static FestivalState initial() {
		FestivalProblem pi = FestivalProblem.initial();
		return of(pi, 0., List2.empty(), List2.empty());
	}

	// a es el numero de entradas que se asignan para el indice concreto
	public void forward(Integer a) {
		acumulado = 0.0;
		for (int i = 0; i < actual.indice(); i++) {
			int tipo = actual.getTipoEntrada(i);
			int area = actual.getArea(i);
			int entradasAsignadas = actual.entradasAsignadas().get(i);
			acumulado += DatosFestival.getCosteAsignacion(tipo, area) * entradasAsignadas;
		}

		acciones.add(a);
		anteriores.add(actual);
		actual = actual.neighbor(a);
	}

	public void back() {
		int last = acciones.size() - 1;
		var problemaAnterior = anteriores.get(last);
		Integer a = acciones.get(last);

		acciones.remove(last);
		anteriores.remove(last);
		actual = problemaAnterior;

		acumulado = 0.0;
		for (int i = 0; i < actual.indice(); i++) {
			int tipo = actual.getTipoEntrada(i);
			int area = actual.getArea(i);
			int entradasAsignadas = actual.entradasAsignadas().get(i);
			acumulado += DatosFestival.getCosteAsignacion(tipo, area) * entradasAsignadas;
		}
	}

	public List<Integer> alternativas() {
		return actual.actions();
	}

	public Double cota(Integer a) {
		Double acumulado = 0.0;
		for (int i = 0; i < actual.indice(); i++) {
			int tipo = actual.getTipoEntrada(i);
			int area = actual.getArea(i);
			int entradasAsignadas = actual.entradasAsignadas().get(i);
			acumulado += DatosFestival.getCosteAsignacion(tipo, area) * entradasAsignadas;
		}
		return acumulado + DatosFestival.getCosteAsignacion(actual.getTipoEntrada(actual.indice()),
				actual.getArea(actual.indice())) * a + actual.neighbor(a).noHeuristic();
	}

	public Boolean esSolucion() {
		return actual.goal() && actual.goalHasSolution();
	}

	public Boolean esTerminal() {
		return actual.goal();
	}

	// FestivalState
	public SolucionFestival getSolucion() {
	    return SolucionFestival.create(List.copyOf(acciones)); // o new ArrayList<>(acciones)
	}

}
