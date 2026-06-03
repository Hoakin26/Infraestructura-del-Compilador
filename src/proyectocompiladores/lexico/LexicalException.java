package proyectocompiladores.lexico;

/**
 * Error detectado durante el análisis léxico (por ejemplo, un símbolo no
 * reconocido por el lenguaje MiniExp).
 *
 * <p>Incluye la línea y columna del problema para poder mostrar mensajes claros
 * en la interfaz, como exige el caso de prueba de error léxico.</p>
 */
public class LexicalException extends Exception {

    private final int linea;
    private final int columna;

    public LexicalException(String mensaje, int linea, int columna) {
        super(mensaje);
        this.linea = linea;
        this.columna = columna;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }
}
