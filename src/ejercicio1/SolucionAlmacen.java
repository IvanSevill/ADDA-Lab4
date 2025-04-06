package ejercicio1;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import common.DatosAlmacenes;
import common.DatosAlmacenes.Producto;
import us.lsi.common.List2;
import us.lsi.common.Map2;
import us.lsi.common.String2;
import us.lsi.gurobi.GurobiSolution;

public class SolucionAlmacen {
	
	public static SolucionAlmacen of(GraphPath<AlmacenVertexInterface, AlmacenEdge> graphPath) {
		return SolucionAlmacen.ofEdges(graphPath.getEdgeList());
	}
	
	public static SolucionAlmacen ofEdges(List<AlmacenEdge> ls) {
		List<Integer> alternativas = List2.empty();
		for (AlmacenEdge alternativa : ls) {
			alternativas.add(alternativa.action());
		}
		SolucionAlmacen s = SolucionAlmacen.of(alternativas);
		return s;
	}
	
	public static SolucionAlmacen of(List<Integer> ls) {
		return new SolucionAlmacen(ls);
	}
	
	public static SolucionAlmacen create(GurobiSolution gs) {
		return new SolucionAlmacen(gs.objVal, gs.values);
	}
	
	public static SolucionAlmacen create(List<Integer> ls) {
		return new SolucionAlmacen(ls);
	}

	private Integer numproductos;
	private Map<Producto, Integer> solucion;

	private SolucionAlmacen(Double vo, Map<String, Double> vbles) {
		numproductos=0;
		solucion = Map2.empty();

		for(Map.Entry<String, Double> par: vbles.entrySet()) {
			if(par.getValue()>0) {
				numproductos++;
				solucion.put(DatosAlmacenes.getProducto(Character.getNumericValue(par.getKey().charAt(2))), Character.getNumericValue(par.getKey().charAt(4)));
			}
		}
	}

	private SolucionAlmacen(List<Integer> ls) {
			
		numproductos = 0;
		solucion = Map2.empty();
		Integer m = DatosAlmacenes.getNumAlmacenes();
		for(int i=0; i<ls.size(); i++) {
			if(ls.get(i)>=0 && ls.get(i)<m) {
				numproductos += 1;
				solucion.put(DatosAlmacenes.getProducto(i), ls.get(i));
			}
		}
	}
	
	private SolucionAlmacen() {
		numproductos = 0;
		solucion = Map2.empty();
	}
	
	@Override
	public String toString() {
		return solucion.entrySet().stream()
		.map(p -> p.getKey().producto()+": Almacen "+p.getValue())
		.collect(Collectors.joining("\n", "Reparto de productos y almacen en el que se coloca cada uno de ellos:\n", String.format("\nProductos colocados: %d", numproductos)));
	}
	
	public static void print(GurobiSolution gs) {
		String2.toConsole("%s\n%s\n%s", String2.linea(), create(gs), String2.linea());
	}

}
