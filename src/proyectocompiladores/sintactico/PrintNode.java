package proyectocompiladores.sintactico;

import java.util.List;

/**
 * Sentencia de impresión. Mapea la regla
 * {@code Print -> "imprimir" Expression ";"}.
 */
public class PrintNode extends ASTNode {

    private final ASTNode expresion;
    private final int linea;

    public PrintNode(ASTNode expresion, int linea) {
        this.expresion = expresion;
        this.linea = linea;
    }

    public ASTNode getExpresion() {
        return expresion;
    }

    public int getLinea() {
        return linea;
    }

    @Override
    public String etiqueta() {
        return "imprimir";
    }

    @Override
    public List<ASTNode> getHijos() {
        return List.of(expresion);
    }
}
