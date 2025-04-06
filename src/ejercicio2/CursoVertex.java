package ejercicio2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import common.DatosCursos;
import common.DatosCursos.Curso;
import us.lsi.common.List2;
import us.lsi.common.Multiset;
import us.lsi.common.Set2;

// Indice: curso de la lista de cursos dada por la que vamos
// cursosSeleccionados: lista de cursos seleccionados 
// presupuestoRestante: dinero restante para los cursos
// areas: conjunto de las diferentes areas ya seleccionadas

public record CursoVertex(Integer indice, List<Integer> cursosSeleccionados, Integer presupuestoRestante,
		Set<Integer> areas) implements CursoVertexInterface {

	static CursoVertex of(Integer indice, List<Integer> cursosSeleccionados, Integer presupuestoTotal,
			Set<Integer> areas) {
		return new CursoVertex(indice, cursosSeleccionados, presupuestoTotal, areas);
	}

	/*
	 * Valor: 0 no se selecciona el curso actual, 1 en c.c.
	 */
	public List<Integer> actions() {
		List<Integer> alternativas = new ArrayList<>(); // Usar ArrayList directamente
		if (indice < DatosCursos.getNumCursos()) {
			Integer area = DatosCursos.getArea(indice);

			// Verificar si el presupuesto restante es suficiente
			if (this.presupuestoRestante >= DatosCursos.getCoste(indice)) {
				alternativas.add(1); // Añadir 1 si se puede cubrir el curso
			}

			// Se añade 0 si el área estaba en areas o si hay cursos restantes
			if (areas.contains(area) || !cursosElegidosAreaConcretaIndice(area, indice + 1).isEmpty()) {
				alternativas.add(0); // Añadir 0 si el área ya está cubierta o si hay cursos restantes
			}
		}
		return alternativas;
	}

	private List<Curso> cursosElegidosAreaConcretaIndice(Integer area, Integer i) {
		List<Curso> ls = new ArrayList<Curso>();
		for (int k = i; k < DatosCursos.getNumCursos(); k++) {
			if (DatosCursos.getArea(k).equals(area)) {
				ls.add(DatosCursos.getCurso(k));
			}
		}
		return ls;
	}

	public CursoVertexInterface neighbor(Integer a) {
		Integer nIndice = this.indice + 1;
		Integer nPresupuestoRestante = this.presupuestoRestante;
		List<Integer> nCursosSeleccionados = List2.copy(this.cursosSeleccionados);
		Set<Integer> nAreas = Set2.copy(this.areas);

		// Si se selecciona el curso actual
		if (a != 0) {
			// Se añade el curso a la lista de cursos seleccionados
			nCursosSeleccionados.add(this.indice);

			// Se añade el area a la que pertenece el curso escogido
			nAreas.add(DatosCursos.getArea(this.indice));

			// Resto al presupuesto restante el coste del curso escogido
			nPresupuestoRestante -= DatosCursos.getCoste(this.indice);
		}

		return CursoVertex.of(nIndice, nCursosSeleccionados, nPresupuestoRestante, nAreas);
	}

	public CursoEdge edge(Integer a) {
		return CursoEdge.of(this, this.neighbor(a), a);
	}

	public Boolean goal() {
		return this.indice == DatosCursos.getNumCursos();
	}

	public Boolean goalHasSolution() {
		Boolean res = true;

		// 1. No puede pasar el presupuesto
		if (presupuestoRestante < 0) {
			return false;
		}

		// 2. Tienes que haber seleccionado al menos un curso (#div/0!)
		if (cursosSeleccionados.size() < 1) {
			return false;
		}

		// 3. La media de duración de los cursos seleccionados debe ser superior o igual
		// a 20
		Multiset<Integer> mAreas = Multiset.empty();
		Double duracionMedia = 0.0;
		for (Integer indexCurso : cursosSeleccionados) {
			Curso curso = DatosCursos.getCurso(indexCurso);
			mAreas.add(curso.area());
			duracionMedia += curso.duracion();
		}
		duracionMedia = duracionMedia / cursosSeleccionados.size();
		res = res && duracionMedia >= 20.0;

		// 4. El número de cursos seleccionados por cada área no puede ser superior al
		// número de cursos de tecnología (area: 0)
		for (int i = 0; i < DatosCursos.getNumAreas(); i++) {
			res = res && mAreas.count(i) > 0 && mAreas.count(i) <= mAreas.count(0);
		}

		return res;

	}

	public String toString() {
		return "CursosVertexI [indice=" + indice + ", cursosSeleccionados=" + cursosSeleccionados
				+ ", presupuestoRestante=" + presupuestoRestante + ", areas=" + areas + "]";
	}

}
