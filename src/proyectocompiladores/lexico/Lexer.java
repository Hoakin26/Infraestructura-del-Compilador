package proyectocompiladores.lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analizador léxico (scanner) del lenguaje MiniExp.
 *
 * <p>Recorre el código fuente carácter por carácter y produce una lista de
 * {@link Token}. El recorrido es de una sola pasada, por lo que el costo es
 * lineal respecto al tamaño de la entrada: <b>O(n)</b>.</p>
 *
 * <p>Categorías reconocidas (según la gramática):</p>
 * <ul>
 *   <li>Palabras reservadas: {@code var}, {@code imprimir}.</li>
 *   <li>Identificadores: {@code [a-zA-Z][a-zA-Z0-9]*}.</li>
 *   <li>Números enteros: {@code [0-9]+}.</li>
 *   <li>Operadores aritméticos: {@code + - * /}.</li>
 *   <li>Asignación: {@code =}.</li>
 *   <li>Agrupación / puntuación: {@code ( ) ;}.</li>
 * </ul>
 */
public class Lexer {

    /** Tabla de palabras reservadas del lenguaje. */
    private static final Map<String, TokenType> PALABRAS_RESERVADAS = new HashMap<>();

    static {
        PALABRAS_RESERVADAS.put("var", TokenType.VAR);
        PALABRAS_RESERVADAS.put("imprimir", TokenType.IMPRIMIR);
    }

    private final String fuente;
    private int posicion;
    private int linea;
    private int columna;

    /**
     * @param fuente código fuente a tokenizar (no nulo).
     */
    public Lexer(String fuente) {
        this.fuente = fuente == null ? "" : fuente;
        this.posicion = 0;
        this.linea = 1;
        this.columna = 1;
    }

    /**
     * Convierte todo el código fuente en una lista de tokens.
     *
     * @return lista de tokens, terminada con un token {@link TokenType#EOF}.
     * @throws LexicalException si encuentra un símbolo no reconocido.
     */
    public List<Token> tokenize() throws LexicalException {
        List<Token> tokens = new ArrayList<>();

        while (!finArchivo()) {
            char actual = caracterActual();

            if (esEspacio(actual)) {
                consumirEspacio();
            } else if (esLetra(actual)) {
                tokens.add(leerIdentificadorOReservada());
            } else if (esDigito(actual)) {
                tokens.add(leerNumero());
            } else {
                tokens.add(leerSimbolo());
            }
        }

        tokens.add(new Token(TokenType.EOF, "<EOF>", linea, columna));
        return tokens;
    }

    /* ===================== Reconocedores ===================== */

    /**
     * Lee un identificador y decide si corresponde a una palabra reservada.
     * Patrón: {@code [a-zA-Z][a-zA-Z0-9]*}.
     */
    private Token leerIdentificadorOReservada() {
        int lineaInicio = linea;
        int columnaInicio = columna;
        StringBuilder sb = new StringBuilder();

        while (!finArchivo() && (esLetra(caracterActual()) || esDigito(caracterActual()))) {
            sb.append(caracterActual());
            avanzar();
        }

        String lexema = sb.toString();
        TokenType tipo = PALABRAS_RESERVADAS.getOrDefault(lexema, TokenType.IDENTIFICADOR);
        return new Token(tipo, lexema, lineaInicio, columnaInicio);
    }

    /**
     * Lee una secuencia de dígitos. Patrón: {@code [0-9]+}.
     */
    private Token leerNumero() {
        int lineaInicio = linea;
        int columnaInicio = columna;
        StringBuilder sb = new StringBuilder();

        while (!finArchivo() && esDigito(caracterActual())) {
            sb.append(caracterActual());
            avanzar();
        }

        return new Token(TokenType.NUMERO, sb.toString(), lineaInicio, columnaInicio);
    }

    /**
     * Lee operadores y símbolos de un solo carácter.
     *
     * @throws LexicalException si el símbolo no pertenece al lenguaje.
     */
    private Token leerSimbolo() throws LexicalException {
        int lineaInicio = linea;
        int columnaInicio = columna;
        char c = caracterActual();

        TokenType tipo;
        switch (c) {
            case '+': tipo = TokenType.MAS; break;
            case '-': tipo = TokenType.MENOS; break;
            case '*': tipo = TokenType.MULTIPLICACION; break;
            case '/': tipo = TokenType.DIVISION; break;
            case '=': tipo = TokenType.ASIGNACION; break;
            case '(': tipo = TokenType.PARENTESIS_IZQ; break;
            case ')': tipo = TokenType.PARENTESIS_DER; break;
            case ';': tipo = TokenType.PUNTO_COMA; break;
            default:
                throw new LexicalException(
                        "Error Léxico en la línea " + lineaInicio
                                + ": Símbolo '" + c + "' no reconocido",
                        lineaInicio, columnaInicio);
        }

        avanzar();
        return new Token(tipo, String.valueOf(c), lineaInicio, columnaInicio);
    }

    /* ===================== Utilidades de recorrido ===================== */

    private void consumirEspacio() {
        while (!finArchivo() && esEspacio(caracterActual())) {
            avanzar();
        }
    }

    private boolean finArchivo() {
        return posicion >= fuente.length();
    }

    private char caracterActual() {
        return fuente.charAt(posicion);
    }

    /**
     * Avanza una posición actualizando el control de línea y columna.
     */
    private void avanzar() {
        if (caracterActual() == '\n') {
            linea++;
            columna = 1;
        } else {
            columna++;
        }
        posicion++;
    }

    private static boolean esEspacio(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private static boolean esLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private static boolean esDigito(char c) {
        return c >= '0' && c <= '9';
    }
}
