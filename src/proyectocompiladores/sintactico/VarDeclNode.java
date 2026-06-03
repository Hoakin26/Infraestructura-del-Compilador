package proyectocompiladores.sintactico;

import java.util.List;

/**
 * Declaración de variable. Mapea la regla
 * {@code Declaration -> "var" ID "=" Expression ";"}.
 */
public class VarDeclNode extends ASTNode {

    private final String nombre;
    private final ASTNode expresion;
    private final int linea;

    public VarDeclNode(String nombre, ASTNode expresion, int linea) {
        this.nombre = nombre;
        this.expresion = expresion;
        this.linea = linea;
    }

    public String getNombre() {
        return nombre;
    }

    public ASTNode getExpresion() {
        return expresion;
    }

    public int getLinea() {
        return linea;
    }

    @Override
    public String etiqueta() {
        return "Declaración: var " + nombre + " =";
    }

    @Override
    public List<ASTNode> getHijos() {
        return List.of(expresion);
    }
}
