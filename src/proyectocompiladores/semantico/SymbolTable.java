package proyectocompiladores.semantico;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Tabla de símbolos del compilador MiniExp.
 *
 * <p>Implementada estrictamente sobre una estructura Hash ({@link HashMap}),
 * donde la <b>clave</b> es el nombre del identificador y el <b>valor</b> es un
 * objeto {@link Symbol} con sus metadatos.</p>
 *
 * <p><b>Complejidad (Big-O):</b> inserción, búsqueda y eliminación son
 * <i>O(1)</i> en promedio gracias al hashing; el recorrido completo es
 * <i>O(n)</i>.</p>
 *
 * <p>Nota: por usar {@code HashMap}, el orden de iteración no está garantizado.</p>
 */
public class SymbolTable {

    private final Map<String, Symbol> tabla = new HashMap<>();

    /**
     * Inserta un símbolo si su nombre aún no existe en la tabla.
     *
     * @param simbolo símbolo a registrar.
     * @return {@code true} si se insertó; {@code false} si ya estaba declarado.
     */
    public boolean declarar(Symbol simbolo) {
        if (tabla.containsKey(simbolo.getNombre())) {
            return false;
        }
        tabla.put(simbolo.getNombre(), simbolo);
        return true;
    }

    /**
     * @param nombre identificador a buscar.
     * @return {@code true} si el identificador está declarado.
     */
    public boolean existe(String nombre) {
        return tabla.containsKey(nombre);
    }

    /**
     * @param nombre identificador a recuperar.
     * @return el símbolo asociado o {@code null} si no existe.
     */
    public Symbol obtener(String nombre) {
        return tabla.get(nombre);
    }

    /**
     * @return todos los símbolos registrados.
     */
    public Collection<Symbol> valores() {
        return tabla.values();
    }

    /**
     * @return cantidad de símbolos en la tabla.
     */
    public int tamano() {
        return tabla.size();
    }

    /**
     * Vacía la tabla de símbolos.
     */
    public void limpiar() {
        tabla.clear();
    }
}
