package proyectocompiladores.sintactico;

/**
 * Error detectado durante el análisis sintáctico (Parser).
 *
 * <p>Guarda la línea y el lexema del token inesperado para poder mostrar
 * mensajes claros en la interfaz, como exigen los casos de error sintáctico.</p>
 */
public class SyntaxException extends Exception {

    private final int linea;
    private final String lexema;

    public SyntaxException(String mensaje, int linea, String lexema) {
        super(mensaje);
        this.linea = linea;
        this.lexema = lexema;
    }

    public int getLinea() {
        return linea;
    }

    public String getLexema() {
        return lexema;
    }
}
