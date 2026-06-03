package proyectocompiladores.sintactico;

import java.util.List;

/**
 * Asignación a una variable ya existente. Mapea la regla
 * {@code Assignment -> ID "=" Expression ";"}.
 */
public class AssignNode extends ASTNode {

    private final String nombre;
    private final ASTNode expresion;
    private final int linea;

    public AssignNode(String nombre, ASTNode expresion, int linea) {
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
        return "Asignación: " + nombre + " =";
    }

    @Override
    public List<ASTNode> getHijos() {
        return List.of(expresion);
    }
}
