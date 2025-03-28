package ejercicio3;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import common.DatosFestival;
import us.lsi.common.IntPair;

import java.util.HashMap;

public class SolucionFestival {

	public static SolucionFestival create(List<Integer> ls) {
		return new SolucionFestival(ls);
	}

	private Integer numAsignaciones;
	private List<Integer> solucion;
	private Double costeTotal;
	private Integer unidadesTotales;

	private SolucionFestival(List<Integer> ls) {
		numAsignaciones = ls.size();
		this.solucion = ls;
		costeTotal = 0.;
		unidadesTotales = 0;

		for (int i = 0; i < DatosFestival.getNumTiposEntrada() * DatosFestival.getNumAreas(); i++) {
			unidadesTotales += ls.get(i);
			costeTotal += DatosFestival.getCosteAsignacion(i / DatosFestival.getNumAreas(),
					i % DatosFestival.getNumAreas())* ls.get(i);
		}

	}

	private String mostrarAsignaciones(List<Integer> ls) {
		Map<Integer, Integer> sumaAreas = new HashMap<Integer, Integer>();
		Map<Integer, Integer> sumaProductos = new HashMap<Integer, Integer>();
		for (int i = 0; i < DatosFestival.getNumAreas(); i++) {
			sumaAreas.put(i, 0);
		}
		
		for (int i = 0; i < DatosFestival.getNumTiposEntrada(); i++) {
			sumaProductos.put(i, 0);
		}

		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i) > 0) {
				Integer area = i % DatosFestival.getNumAreas();
				Integer tipo = i / DatosFestival.getNumAreas();
				sumaAreas.put(area, sumaAreas.get(area) + ls.get(i));
				sumaProductos.put(tipo, sumaProductos.get(tipo) + ls.get(i));
			}
		}

		StringBuilder s = new StringBuilder();

		for (int area = 0; area < DatosFestival.getNumAreas(); area++) {
			Integer aforoMaximo = DatosFestival.getArea(area).aforoMaximo();
			
			if (sumaAreas.get(area) > 0){
				s.append("Aforo área A" + area).append(": ").append(sumaAreas.get(area) + "/" + aforoMaximo).append("\n");

				for (int tipo = 0; tipo < DatosFestival.getNumTiposEntrada(); tipo++) {
					int unidades = ls.get(tipo * DatosFestival.getNumAreas() + area);
					if (unidades > 0) {
						s.append("Tipo de entrada T" + tipo).append(" asignadas: ").append(unidades).append(" unidades\n");

					}
				}
			}
	
		}
		
		s.append("\nTipos de entradas\n");
		
		for (int tipo = 0; tipo < DatosFestival.getNumTiposEntrada(); tipo++) {
			s.append("Unidades T").append(tipo).append(" : ").append(sumaProductos.get(tipo)).append(" ("+DatosFestival.getCuotaMinima(tipo)+")").append("\n");
		}

		return s.toString();
	}

	@Override
	public String toString() {
		return mostrarAsignaciones(solucion) + "\nCosteTotal: " + costeTotal + "\nUnidadesTotales: " + unidadesTotales;
	}

	public Integer getNumAsignaciones() {
		return numAsignaciones;
	}

	public List<Integer> getSolucion() {
		return solucion;
	}

	public Double getCosteTotal() {
		return costeTotal;
	}

	public Integer getUnidadesTotales() {
		return unidadesTotales;
	}
}
