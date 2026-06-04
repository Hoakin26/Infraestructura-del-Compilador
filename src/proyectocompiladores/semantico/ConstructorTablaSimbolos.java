package proyectocompiladores.semantico;

import java.util.List;
import proyectocompiladores.lexico.Token;
import proyectocompiladores.lexico.TokenType;

/**
 * Paso intermedio entre el análisis léxico y la tabla de símbolos.
 *
 * <p>Recorre la lista de tokens producida por el {@code Lexer} y, cada vez que
 * encuentra la palabra reservada {@code var} seguida de un identificador,
 * registra ese identificador en la {@link SymbolTable} con sus metadatos
 * (nombre, tipo de dato y línea de declaración).</p>
 *
 * <p>El recorrido es de una sola pasada: <b>O(n)</b> respecto al número de
 * tokens.</p>
 */
public class ConstructorTablaSimbolos {

    /** Tipo de dato por defecto en MiniExp (solo enteros por ahora). */
    private static final String TIPO_POR_DEFECTO = "entero";
    /** Alcance único soportado por ahora. */
    private static final String ALCANCE_GLOBAL = "global";

    public SymbolTable construir(List<Token> tokens) {
        SymbolTable tabla = new SymbolTable();
        if (tokens == null) {
            return tabla;
        }

        for (int i = 0; i < tokens.size() - 1; i++) {
            Token actual = tokens.get(i);
            Token siguiente = tokens.get(i + 1);

            boolean declaracion = actual.getTipo() == TokenType.VAR
                    && siguiente.getTipo() == TokenType.IDENTIFICADOR;

            if (declaracion) {
                Symbol simbolo = new Symbol(
                        siguiente.getLexema(),
                        TIPO_POR_DEFECTO,
                        siguiente.getLinea(),
                        ALCANCE_GLOBAL);
                tabla.declarar(simbolo);
            }
        }

        return tabla;
    }
}
