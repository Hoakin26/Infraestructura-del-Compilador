package proyectocompiladores.sintactico;

import java.util.List;

/**
 * Operación binaria aritmética ({@code + - * /}).
 *
 * <p>Surge de las reglas {@code Expression -> Term (("+"|"-") Term)*} y
 * {@code Term -> Factor (("*"|"/") Factor)*}. La estructura del árbol garantiza
 * la precedencia: las multiplicaciones y divisiones quedan más abajo (se
 * evalúan antes) que las sumas y restas.</p>
 */
public class BinaryOpNode extends ASTNode {

    private final String operador;
    private final ASTNode izquierda;
    private final ASTNode derecha;

    public BinaryOpNode(String operador, ASTNode izquierda, ASTNode derecha) {
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    public String getOperador() {
        return operador;
    }

    public ASTNode getIzquierda() {
        return izquierda;
    }

    public ASTNode getDerecha() {
        return derecha;
    }

    @Override
    public String etiqueta() {
        return "Operador '" + operador + "'";
    }

    @Override
    public List<ASTNode> getHijos() {
        return List.of(izquierda, derecha);
    }
}
