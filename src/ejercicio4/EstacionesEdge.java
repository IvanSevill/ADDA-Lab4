package ejercicio4;

import common.DatosEstaciones;
import us.lsi.graphs.virtual.SimpleEdgeAction;

public record EstacionesEdge(EstacionesVertexInterface source, EstacionesVertexInterface target, Integer action,
		Double weight) implements SimpleEdgeAction<EstacionesVertexInterface, Integer> {

	public static EstacionesEdge of(EstacionesVertexInterface c1, EstacionesVertexInterface c2, Integer action) {
	    Double tiempoMedioTramo = 0.0;
	    Integer vin, vout;

	    if (c1.indice() == 0) {
	        vin = c1.camino().get(0);
	    } else {
	        vin = c1.camino().get(c1.indice()-1);
	    }

	    if (c2.indice() < DatosEstaciones.itemsNumber()) {
	        vout = c2.camino().get(c2.indice()-1);
	    } else {
	        vout = c2.camino().get(0);
	    }

	    tiempoMedioTramo = DatosEstaciones.calculaTiempoMedioTramo(vin, vout);

	    return new EstacionesEdge(c1, c2, action, tiempoMedioTramo);
	}

}
