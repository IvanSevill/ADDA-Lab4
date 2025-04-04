package common;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import us.lsi.common.List2;
import us.lsi.common.Map2;

public abstract class PDRTemplate<V, S> {

	protected Map<V, Spm> memory;
	protected Integer mejorValor;

	public S search() {
		this.memory = Map2.empty();
		this.mejorValor = initialBestValue();

		pdr_search(initialVertex(), initialAccumulated());

		return buildSolution();
	}

	private Spm pdr_search(V problem, Integer acum) {
		Spm res = null;

		if (memory.containsKey(problem)) {
			res = memory.get(problem);

		} else if (isTerminal(problem)) {
			res = Spm.of(null, 0);
			memory.put(problem, res);
			if (acum > mejorValor) {
				mejorValor = acum;
			}

		} else {
			List<Spm> soluciones = List2.empty();

			for (Integer action : actions(problem)) {
				Double cota = acotar(acum, problem, action);
				if (cota < mejorValor)
					continue;

				V vecino = neighbor(problem, action);
				int incremento = (action >= 0) ? 1 : 0;
				Spm subsol = pdr_search(vecino, acum + incremento);

				if (subsol != null) {
					Spm ampliado = Spm.of(action, subsol.weight() + incremento);
					soluciones.add(ampliado);
				}
			}

			res = soluciones.stream().max(Comparator.naturalOrder()).orElse(null);
			if (res != null) {
				memory.put(problem, res);
			}
		}

		return res;
	}

	// Métodos abstractos que debes implementar en cada problema
	protected abstract V initialVertex();

	protected abstract boolean isTerminal(V v);

	protected abstract List<Integer> actions(V v);

	protected abstract V neighbor(V v, Integer a);

	protected abstract Double acotar(Integer acum, V v, Integer a);

	protected abstract Integer initialBestValue();

	protected abstract Integer initialAccumulated();

	protected abstract S buildSolution();

	public Map<V, Spm> getMemory() {
		return memory;
	}
}
