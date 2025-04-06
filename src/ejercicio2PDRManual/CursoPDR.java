package ejercicio2PDRManual;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import common.DatosCursos;
import common.Spm;
import ejercicio2.SolucionCursos;
import us.lsi.common.List2;
import us.lsi.common.Map2;

public class CursoPDR {
	public static Map<CursoProblem, Spm> memory;
	public static Integer mejorValor = Integer.MIN_VALUE; // Estamos maximizando

	public static SolucionCursos search() {
		memory = Map2.empty();
		pdr_search(CursoProblem.initial(), 0, memory);
		return getSolution();
	}

	private static Spm pdr_search(CursoProblem prob, Integer acumulado, Map<CursoProblem, Spm> memoria) {
		
		Spm res = null;
		Boolean esTerminal = prob.goal();
		Boolean esSolucion = prob.goalHasSolution();
		// System.out.println("esTerminal:" + esTerminal + " esSolucion:" + esSolucion);
		if (memory.containsKey(prob)) {
			res = memory.get(prob);
		} else if (esTerminal && esSolucion) {
			res = Spm.of(null, 0);
			memory.put(prob, res);
			if (acumulado > mejorValor) { // Estamos maximizando
			//if (acumulado < mejorValor) { // Estamos minimizando
				mejorValor = acumulado;
			}
		} else {
			List<Spm> soluciones = List2.empty();
			for (Integer action : prob.actions()) {
				Double cota = acotar(acumulado, prob, action);   		
				if (cota < mejorValor) { // Estamos maximizando
				//if (cota > mejorValor) { // Estamos manimizando
					continue;
				}
				CursoProblem vecino = prob.neighbor(action);
				//Hay que adaptar al problema
				Integer relevancia = DatosCursos.getCurso(prob.indice()).relevancia();
				Spm s = pdr_search(vecino, (int)(acumulado + relevancia * action), memory);
				if (s != null) {
					Spm amp = Spm.of(action, s.weight() + relevancia * action);
					soluciones.add(amp);
				}
			}
			// Estamos minimizando -< maximizando
			res = soluciones.stream().max(Comparator.naturalOrder()).orElse(null); //Estamos maximizando
			//res = soluciones.stream().min(Comparator.naturalOrder()).orElse(null); //Estamos minimizando
			if (res != null)
				memory.put(prob, res);
		}
		return res;
		
	}

	// Lo que tengo hasta al momento, más lo que añado ahora, más lo que puedo usar
	private static Double acotar(Integer acum, CursoProblem p, Integer a) {
		Double relevancia = p.indice() < DatosCursos.getNumCursos() ? DatosCursos.getCurso(p.indice()).relevancia() : 0.0;		
		return acum + a * relevancia + p.neighbor(a).heuristic();
	}

	// Construyo la solución
	private static SolucionCursos getSolution() {
		List<Integer> res = List2.empty();

		CursoProblem prob = CursoProblem.initial();
		Spm spm = memory.get(prob);

//		System.out.println(memory);
		
		while (spm != null && spm.a() != null) {
			CursoProblem before = prob;
			res.add(spm.a());
			prob = before.neighbor(spm.a());
			spm = memory.get(prob);
		}

		return SolucionCursos.of(res);
	}

}
