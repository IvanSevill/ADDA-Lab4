package ejercicio1;

import java.util.function.Predicate;
import common.DatosAlmacenes;

public class AlmacenHeuristic {

    // Firma ajustada a lo que espera el algoritmo A*.
    public static double heuristic(AlmacenVertexInterface v1, Predicate<AlmacenVertexInterface> goal, AlmacenVertexInterface v2) {
        // Lógica de la heurística.
        return v1.indice() < DatosAlmacenes.getNumProductos() 
            ? (double) DatosAlmacenes.getNumProductos() - v1.indice() 
            : 0.0;
    }
}
