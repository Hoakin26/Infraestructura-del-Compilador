package proyectocompiladores.lexico;

/**
 * Unidad léxica producida por el {@link Lexer}.
 *
 * <p>Cada token guarda su categoría ({@link TokenType}), el lexema original tal
 * como apareció en el código fuente, y su posición (línea y columna) para poder
 * reportar errores y depurar con precisión.</p>
 */
public class Token {

    private final TokenType tipo;
    private final String lexema;
    private final int linea;
    private final int columna;

    /**
     * Crea un token.
     *
     * @param tipo    categoría léxica del token.
     * @param lexema  texto exacto encontrado en el código fuente.
     * @param linea   línea (base 1) donde inicia el token.
     * @param columna columna (base 1) donde inicia el token.
     */
    public Token(TokenType tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    public TokenType getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return String.format("[L%d:C%d] %-15s %s",
                linea, columna, tipo, "'" + lexema + "'");
    }
}
