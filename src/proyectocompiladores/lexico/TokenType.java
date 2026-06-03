package proyectocompiladores.lexico;

/**
 * Categorías de tokens reconocidas por el lenguaje MiniExp.
 *
 * <p>Las categorías se derivan directamente de la sección de Análisis Léxico de
 * la gramática: palabras reservadas, identificadores, números, operadores
 * aritméticos, asignación y símbolos de agrupación/puntuación.</p>
 */
public enum TokenType {

    /** Palabra reservada {@code var} para declarar variables. */
    VAR("Palabra reservada"),
    /** Palabra reservada {@code imprimir} para mostrar valores. */
    IMPRIMIR("Palabra reservada"),

    /** Identificador: {@code [a-zA-Z][a-zA-Z0-9]*}. */
    IDENTIFICADOR("Identificador"),
    /** Número entero: {@code [0-9]+}. */
    NUMERO("Número"),

    /** Operador suma {@code +}. */
    MAS("Operador aritmético"),
    /** Operador resta {@code -}. */
    MENOS("Operador aritmético"),
    /** Operador multiplicación {@code *}. */
    MULTIPLICACION("Operador aritmético"),
    /** Operador división {@code /}. */
    DIVISION("Operador aritmético"),

    /** Operador de asignación {@code =}. */
    ASIGNACION("Asignación"),

    /** Paréntesis de apertura {@code (}. */
    PARENTESIS_IZQ("Símbolo de agrupación"),
    /** Paréntesis de cierre {@code )}. */
    PARENTESIS_DER("Símbolo de agrupación"),
    /** Punto y coma {@code ;}. */
    PUNTO_COMA("Símbolo de puntuación"),

    /** Marca de fin de archivo. */
    EOF("Fin de archivo");

    private final String descripcion;

    TokenType(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return descripción legible de la categoría (útil para la UI).
     */
    public String getDescripcion() {
        return descripcion;
    }
}
