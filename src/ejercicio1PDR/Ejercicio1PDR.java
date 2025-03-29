package ejercicio1PDR;

import java.util.List;

import common.DatosAlmacenes;
import common.PDRTemplate;
import common.Spm;
import ejercicio1.AlmacenVertex;
import ejercicio1.SolucionAlmacen;
import us.lsi.common.List2;

public class Ejercicio1PDR extends PDRTemplate<AlmacenVertex, SolucionAlmacen> {

	protected AlmacenVertex initialVertex() {
		return AlmacenVertex.initial();
	}

	protected boolean isTerminal(AlmacenVertex v) {
		return v.indice().equals(DatosAlmacenes.getNumProductos());
	}

	@Override
	protected List<Integer> actions(AlmacenVertex v) {
		return v.actions();
	}

	@Override
	protected AlmacenVertex neighbor(AlmacenVertex v, Integer a) {
		return v.neighbor(a);
	}

	@Override
	protected Double acotar(Integer acum, AlmacenVertex v, Integer a) {
		int incremento = (a >= 0) ? 1 : 0;
		return acum + incremento + v.neighbor(a).heuristic();
	}

	@Override
	protected Integer initialBestValue() {
		return Integer.MIN_VALUE;
	}

	@Override
	protected Integer initialAccumulated() {
		return 0;
	}

	@Override
	protected SolucionAlmacen buildSolution() {
		List<Integer> acciones = List2.empty();
		AlmacenVertex v = initialVertex();
		Spm spm = memory.get(v);

		while (spm != null && spm.a() != null) {
			acciones.add(spm.a());
			v = v.neighbor(spm.a());
			spm = memory.get(v);
		}

		return SolucionAlmacen.create(acciones);
	}
}
