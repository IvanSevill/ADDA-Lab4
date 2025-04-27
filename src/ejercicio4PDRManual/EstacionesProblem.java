package ejercicio4PDRManual;

import java.util.List;
import java.util.Set;

import common.DatosEstaciones;
import us.lsi.common.List2;


public record EstacionesProblem (Integer indice,List<Integer> camino)  {

	
	public static EstacionesProblem initial() {
	    List <Integer> camino = List2.empty();
	    camino.add(0); // Nodo donde se inicia el camnino
		return  EstacionesProblem.of(0,camino);
	}
	
	public static EstacionesProblem of(Integer indice,List<Integer> camino) {
	     return new EstacionesProblem(indice,camino);
	}
	
	
	public Boolean goal() {
		//System.out.println(this.indice == DatosAlmacenes.getNumProductos());
		return  this.indice == DatosEstaciones.itemsNumber();
	}
	
	public Boolean goalHasSolution() {
		return esSolucion();
	}
	
	public List<Integer> actions() {
		List<Integer> alternativas = List2.empty();

		if (indice == DatosEstaciones.itemsNumber()-1) {
			Integer vin = camino.get(indice);
			Integer vout = camino.get(0);
			if (DatosEstaciones.existeTramo(vin,vout)) {
				alternativas.add(vout);
			}
		} else if (indice < DatosEstaciones.itemsNumber()) {	
			Integer v = camino.get(indice);
			Set<Integer> vecinos = DatosEstaciones.vecinos(v);
			for (int i=0;i<indice;i++) {
				vecinos.remove(camino.get(i));
			}
			alternativas = List2.ofCollection(vecinos);
			
		}
		
//		System.out.println(indice + " -> " + alternativas );
//		StringBuilder result = new StringBuilder("* ");
//		Integer vx=camino.get(indice);
//        result.append(alternativas.stream()
//                .map(x -> DatosEstaciones.grafoTiempo.getVertex(x).nombre() + "(" +DatosEstaciones.existeTramo(vx, x) + ","  +  
//                DatosEstaciones.calculaTiempoMedioTramo(vx, x)+ ")")
//                .collect(Collectors.joining(", "))).append("\n");
//
//		System.out.println(DatosEstaciones.grafoTiempo.getVertex(vx).nombre() + "->" + result );

		return alternativas;
	}

	private boolean esSolucion()  {
		
		if (!goal()) { //Esto es un parche para AStart
			return false;
		}
		Double costeTrayectoCerrado = DatosEstaciones.calcularCosteTrayectoCerrado(camino);
		Double costeTrayectoCompleto = DatosEstaciones.getCosteTrayectoCompleto();
		
		// System.out.println( costeTrayectoCompleto);
		// System.out.println( costeTrayectoCerrado);

		if (costeTrayectoCerrado > 0.75*costeTrayectoCompleto) {

			return false;
		}
		
		Integer numEstacionesConsecSatisf = DatosEstaciones.calculaEstacionesConsecSatisf(camino);
		// System.out.println( numEstacionesConsecSatisf);
		if (numEstacionesConsecSatisf == 0) {
           return false;
		}
		 
		return true;
	}
	
	public EstacionesProblem neighbor(Integer a) {
		List<Integer> caminoAux = List2.copy(camino);
		caminoAux.add(a);
		return EstacionesProblem.of(this.indice + 1,caminoAux);
	}

	public Double heuristic() {
		Double h = 0.;
		 
		return h;
	}



	@Override
	public String toString() {
		return "EstacionesVertexI [indice=" + indice + ", camino=" + camino + "]";
	}
    

}
