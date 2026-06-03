package proyectocompiladores.sintactico;

import java.util.List;

/**
 * Nodo base del Árbol de Sintaxis Abstracta (AST).
 *
 * <p>Cada nodo sabe describirse con una {@link #etiqueta()} legible y exponer
 * sus {@link #getHijos()}, lo que permite recorrer el árbol de forma genérica
 * (por ejemplo, para construir un {@code TreeView} en la interfaz).</p>
 *
 * <p><b>Complejidad (Big-O):</b> el recorrido completo del árbol es <i>O(n)</i>
 * respecto al número de nodos, ya que cada nodo se visita una sola vez.</p>
 */
public abstract class ASTNode {

    /**
     * @return texto descriptivo del nodo para mostrarlo en la UI.
     */
    public abstract String etiqueta();

    /**
     * @return lista de nodos hijos (vacía si es una hoja).
     */
    public abstract List<ASTNode> getHijos();
}
