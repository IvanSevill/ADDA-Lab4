package ejercicio2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import common.DatosAlmacenes;
import common.DatosAlmacenes.Producto;
import common.DatosCursos;
import common.DatosCursos.Curso;

public class SolucionCursos {

	public static SolucionCursos create(List<Integer> ls) {
		return new SolucionCursos(ls);
	}

	private Integer numCursos;
	private Map<Integer, Integer> solucion;
	private Double puntuacionTotal;
	private Integer costeTotal;

	private SolucionCursos(List<Integer> ls) {
		this.solucion = new HashMap<>();
		costeTotal = 0;
		puntuacionTotal = 0.;
		
		for (Integer i = 0; i < ls.size(); i++) {
			if (ls.get(i) > 0) {
				int index = i % DatosCursos.getNumCursos();
				Curso curso = DatosCursos.getCurso(index);
				Integer coste = DatosCursos.getCoste(index);
				// Verificar si el producto ya está asignado
				solucion.put(curso.id(), coste);
				costeTotal += coste;
				puntuacionTotal += curso.relevancia();
			}
		}
		numCursos = solucion.size();

	}

	@Override
	public String toString() {
		return solucion.entrySet().stream().map(p -> "Curso " + p.getKey() + ": Seleccionado")
				.collect(Collectors.joining("\n", "Cursos seleccionados:\n",
						String.format("\nTotal de cursos seleccionados: %d\nPuntuación total: %.2f\nCoste total: %d",
								numCursos, puntuacionTotal, costeTotal)));
	}

	public Integer getNumCursos() {
		return numCursos;
	}

	public Map<Integer, Integer> getSolucion() {
		return solucion;
	}

	public Double getPuntuacionTotal() {
		return puntuacionTotal;
	}

	public Integer getCosteTotal() {
		return costeTotal;
	}
}
