package proyectocompiladores.sintactico;

import java.util.Collections;
import java.util.List;

/**
 * Referencia a una variable (hoja del AST). Mapea la alternativa
 * {@code Factor -> ID}.
 */
public class IdentifierNode extends ASTNode {

    private final String nombre;

    public IdentifierNode(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String etiqueta() {
        return "Identificador: " + nombre;
    }

    @Override
    public List<ASTNode> getHijos() {
        return Collections.emptyList();
    }
}
