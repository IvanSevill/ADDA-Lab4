package ejercicio2BTManual;

import java.util.List;

import common.DatosCursos;
import ejercicio2.SolucionCursos;
import us.lsi.common.List2;

public class CursoState {

	CursoProblem actual;
	Double acumulado;
	List<Integer> acciones;
	List<CursoProblem> anteriores;

	private CursoState(CursoProblem prob, Double a, List<Integer> ls1, List<CursoProblem> ls2) {
		actual = prob;
		acumulado = a;
		acciones = ls1;
		anteriores = ls2;
	}

	public static CursoState of(CursoProblem prob, Double acum, List<Integer> lsa, List<CursoProblem> lsp) {
		return new CursoState(prob, acum, lsa, lsp);
	}

	public static CursoState initial() {
		CursoProblem pi = CursoProblem.initial();
		return of(pi, 0., List2.empty(), List2.empty());
	}

	public void forward(Integer a) {
		acumulado = acciones.stream().mapToDouble(i -> DatosCursos.getRelevancia(i)).sum();

		acciones.add(a);
		anteriores.add(actual);
		actual = actual.neighbor(a);
	}

	public void back() {
		int last = acciones.size() - 1;
		acciones.remove(last);
		actual = anteriores.remove(last);
		acumulado = acciones.stream().mapToDouble(i -> DatosCursos.getRelevancia(i)).sum();
	}

	public List<Integer> alternativas() {
		return actual.actions();
	}

	public Double cota(Integer a) {
		Double cota = actual.neighbor(a).heuristic();
		Integer presente = DatosCursos.getRelevancia(a);
		return acumulado + presente + cota;
	}
	

	public boolean esSolucion() {
		return actual.goal() && actual.goalHasSolution();
	}

	public boolean esTerminal() {
		return actual.goal();
	}

	public SolucionCursos getSolucion() {
		return SolucionCursos.of(acciones);
	}

}
