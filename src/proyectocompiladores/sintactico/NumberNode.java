package proyectocompiladores.sintactico;

import java.util.Collections;
import java.util.List;

/**
 * Literal numérico (hoja del AST). Mapea la alternativa {@code Factor -> NUM}.
 */
public class NumberNode extends ASTNode {

    private final String valor;

    public NumberNode(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String etiqueta() {
        return "Número: " + valor;
    }

    @Override
    public List<ASTNode> getHijos() {
        return Collections.emptyList();
    }
}
