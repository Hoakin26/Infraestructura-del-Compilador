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

    public abstract String etiqueta();

    public abstract List<ASTNode> getHijos();
}
