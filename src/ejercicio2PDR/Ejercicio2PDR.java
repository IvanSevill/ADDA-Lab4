package ejercicio2PDR;

import java.util.List;

import common.PDRTemplate;
import ejercicio2.CursosVertex;
import ejercicio2.SolucionCursos;

public class Ejercicio2PDR extends PDRTemplate<CursosVertex, SolucionCursos> {

	@Override
	protected CursosVertex initialVertex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isTerminal(CursosVertex v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected List<Integer> actions(CursosVertex v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CursosVertex neighbor(CursosVertex v, Integer a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Double acotar(Integer acum, CursosVertex v, Integer a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Integer initialBestValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Integer initialAccumulated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SolucionCursos buildSolution() {
		// TODO Auto-generated method stub
		return null;
	}

}
