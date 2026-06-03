package proyectocompiladores.sintactico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Raíz del AST. Representa la regla {@code Program -> Statement*}: contiene la
 * secuencia de sentencias del programa.
 */
public class ProgramNode extends ASTNode {

    private final List<ASTNode> sentencias = new ArrayList<>();

    public void agregar(ASTNode sentencia) {
        sentencias.add(sentencia);
    }

    public List<ASTNode> getSentencias() {
        return Collections.unmodifiableList(sentencias);
    }

    @Override
    public String etiqueta() {
        return "Programa";
    }

    @Override
    public List<ASTNode> getHijos() {
        return getSentencias();
    }
}
