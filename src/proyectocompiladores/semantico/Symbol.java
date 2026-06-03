package proyectocompiladores.semantico;

/**
 * Metadatos de un identificador declarado en el código fuente (una entrada de
 * la {@link SymbolTable}).
 *
 * <p>Los getters siguen la convención JavaBean para poder enlazarse
 * directamente con las columnas de la {@code TableView} mediante
 * {@code PropertyValueFactory}.</p>
 */
public class Symbol {

    private final String nombre;
    private final String tipoDato;
    private final int linea;
    private final String alcance;

    /**
     * @param nombre   nombre del identificador (clave en la tabla).
     * @param tipoDato tipo de dato asociado (por ahora siempre entero/var).
     * @param linea    línea donde fue declarado.
     * @param alcance  alcance del símbolo (por ahora siempre global).
     */
    public Symbol(String nombre, String tipoDato, int linea, String alcance) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.linea = linea;
        this.alcance = alcance;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public int getLinea() {
        return linea;
    }

    public String getAlcance() {
        return alcance;
    }

    @Override
    public String toString() {
        return String.format("%s : %s (línea %d, %s)", nombre, tipoDato, linea, alcance);
    }
}
