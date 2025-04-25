package common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.jgrapht.graph.SimpleWeightedGraph;

import ejercicio4.Estacion;
import ejercicio4.Tramo;
import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.Graphs2;
import us.lsi.graphs.GraphsReader;
import us.lsi.graphs.views.IntegerVertexGraphView;

public class DatosEstaciones {
	
	//public static Integer ntest = 4;
	public static Integer ntest = 4;

	public static IntegerVertexGraphView<Estacion,Tramo> grafoTiempo;
	public static IntegerVertexGraphView<Estacion,Tramo> grafoCoste;
	public static SimpleWeightedGraph<Estacion, Tramo> grafoCompleto;
	public static Integer n;
	public static Double costeTrayectoCompleto = 0.0;
	public static Double limiteCoste = 0.0;
	public static Double menorPesoArista = 0.0;
	
	public static void iniDatos(String fichero) {
	    
        SimpleWeightedGraph<Estacion, Tramo> gAux = GraphsReader.newGraph(
        		fichero,
                Estacion::ofFormat,
                Tramo::ofFormat,
                Graphs2::simpleWeightedGraph,
                Tramo::tiempo
        );
        grafoCompleto = gAux;
		grafoTiempo = IntegerVertexGraphView.of(gAux);
		
        gAux = GraphsReader.newGraph(
        		fichero,
                Estacion::ofFormat,
                Tramo::ofFormat,
                Graphs2::simpleWeightedGraph,
                Tramo::costeBillete
        );
        grafoCoste = IntegerVertexGraphView.of(gAux);
        
        n = grafoTiempo.vertexSet().size();
        
        costeTrayectoCompleto = grafoCoste.edgeSet().stream()
				.mapToDouble(e -> e.weight())
				.sum();
        
        limiteCoste = 0.75*DatosEstaciones.costeTrayectoCompleto;
        
        menorPesoArista = grafoTiempo.edgeSet().stream()
                .mapToDouble(e -> e.weight()) // Obtener solo los pesos
                .min() // Encontrar el mínimo
                .orElse(-1); // Valor por defecto si la lista está vacía
        
        
        GraphColors.toDot(grafoCompleto,fichero + ".gv",
				x->x.nombre(), x->x.toString(),
				v->GraphColors.color(Color.black),
				e->GraphColors.color(Color.black));
	}

	public static Integer itemsNumber() {
		return DatosEstaciones.n;
	}
	
	public static Set<Integer> vecinos(Integer i) {
		Set<Integer> vSet = new HashSet<>();
		IntStream.range(0, n).boxed()
		.filter(j->grafoTiempo.containsEdge(i,j)).forEach(k->vSet.add(k));
		return vSet;
	}
	
	public static boolean existeTramo(Integer i, Integer j) {
		return grafoCoste.containsEdge(i, j);
	}
	
	private static Double calculaCosteTramo(Integer i, Integer j) {
		Double costeTramo = 10000.;
		if (grafoCoste.containsEdge(i, j)) {
			costeTramo = grafoCoste.getEdgeWeight(i, j);
		}
		return costeTramo;
	}
	
	public static Double calculaTiempoMedioTramo(Integer i, Integer j) {
		Double tiempoTramo = 10000.;
		if (grafoTiempo.containsEdge(i, j)) {
			tiempoTramo = grafoTiempo.getEdgeWeight(i, j);
		}
		return tiempoTramo;
	}
	
	
	public static Double getCosteTrayectoCompleto () {
		return costeTrayectoCompleto;
	}
	
	public static Double calcularCosteTrayectoCerrado(List<Integer> cr) {
		//System.out.println(cr + " " + n);
		return IntStream.range(0, n).boxed()
				.mapToDouble(i -> calculaCosteTramo(cr.get(i), cr.get((i+1)%n)))
				.sum();
	}
	
	private static Boolean compruebaEstacionesCumplen(Integer i, Integer j) {
		Boolean r = false;
        if (grafoTiempo.containsEdge(i, j)) {
            r = grafoTiempo.getVertex(i).satisfaccionClientes() >= 7 && 
            		grafoTiempo.getVertex(j).satisfaccionClientes() >= 7;
        }
        return r;
	}
    
	    
	public static Integer calculaEstacionesConsecSatisf (List<Integer> cr) {
		return (int) IntStream.range(0, n).boxed()
				.filter(i -> compruebaEstacionesCumplen(cr.get(i), cr.get((i+1)%n)))
				.count();
	}


	public static void toConsole() {

       Double tiempoTrayectoCompleto = grafoTiempo.edgeSet().stream()
				.mapToDouble(e -> e.weight())
				.sum();
		
        System.out.println( grafoCompleto.vertexSet() +
        		    "\n vertexa: " + grafoTiempo.vertexSet()  +
        			"\n size: " + grafoCompleto.vertexSet().size());
    }
}