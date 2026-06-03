package proyectocompiladores.semantico;

/**
 * Error detectado durante el análisis semántico, por ejemplo el uso de una
 * variable que no fue declarada previamente con {@code var}.
 *
 * <p>Guarda la línea y el nombre del identificador problemático para poder
 * mostrar mensajes claros en la interfaz.</p>
 */
public class SemanticException extends Exception {

    private final int linea;
    private final String identificador;

    public SemanticException(String mensaje, int linea, String identificador) {
        super(mensaje);
        this.linea = linea;
        this.identificador = identificador;
    }

    public int getLinea() {
        return linea;
    }

    public String getIdentificador() {
        return identificador;
    }
}
