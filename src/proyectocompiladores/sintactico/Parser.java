package proyectocompiladores.sintactico;

import java.util.List;
import proyectocompiladores.lexico.Token;
import proyectocompiladores.lexico.TokenType;
import proyectocompiladores.semantico.SemanticException;
import proyectocompiladores.semantico.SymbolTable;

/**
 * Analizador sintáctico (Parser) del lenguaje MiniExp.
 *
 * <p>Construye el AST mediante <b>descenso recursivo</b> (Recursive Descent
 * Parsing): cada regla de la gramática BNF se implementa como un método, y la
 * jerarquía de llamadas reproduce la precedencia de operadores.</p>
 *
 * <pre>
 *   Program     -> Statement*
 *   Statement   -> Declaration | Assignment | Print
 *   Declaration -> "var" ID "=" Expression ";"
 *   Assignment  -> ID "=" Expression ";"
 *   Print       -> "imprimir" Expression ";"
 *   Expression  -> Term ( ("+" | "-") Term )*
 *   Term        -> Factor ( ("*" | "/") Factor )*
 *   Factor      -> NUM | ID | "(" Expression ")"
 * </pre>
 *
 * <p><b>Complejidad (Big-O):</b> cada token se consume una sola vez, por lo que
 * el análisis es <i>O(n)</i> respecto al número de tokens.</p>
 */
public class Parser {

    private final List<Token> tokens;
    private final SymbolTable tablaSimbolos;
    private int posicion;

    /**
     * @param tokens        lista de tokens producida por el Lexer (debe terminar en EOF).
     * @param tablaSimbolos tabla de símbolos para validar el uso de variables;
     *                      si es {@code null}, no se realiza validación semántica.
     */
    public Parser(List<Token> tokens, SymbolTable tablaSimbolos) {
        this.tokens = tokens;
        this.tablaSimbolos = tablaSimbolos;
        this.posicion = 0;
    }

    /**
     * Punto de entrada. Implementa {@code Program -> Statement*}.
     *
     * @return raíz del AST.
     * @throws SyntaxException   si la secuencia de tokens viola la gramática.
     * @throws SemanticException si se usa una variable no declarada.
     */
    public ProgramNode parse() throws SyntaxException, SemanticException {
        ProgramNode programa = new ProgramNode();
        while (!finTokens()) {
            programa.agregar(statement());
        }
        return programa;
    }

    /* ===================== Sentencias ===================== */

    /** {@code Statement -> Declaration | Assignment | Print}. */
    private ASTNode statement() throws SyntaxException, SemanticException {
        if (verificar(TokenType.VAR)) {
            return declaration();
        }
        if (verificar(TokenType.IMPRIMIR)) {
            return print();
        }
        if (verificar(TokenType.IDENTIFICADOR)) {
            return assignment();
        }

        Token t = actual();
        throw error("Sentencia inesperada '" + t.getLexema()
                + "' (se esperaba 'var', 'imprimir' o un identificador)", t);
    }

    /** {@code Declaration -> "var" ID "=" Expression ";"}. */
    private ASTNode declaration() throws SyntaxException, SemanticException {
        Token var = consumir(TokenType.VAR, "Se esperaba 'var'");
        Token id = consumir(TokenType.IDENTIFICADOR, "Se esperaba un identificador después de 'var'");
        consumir(TokenType.ASIGNACION, "Se esperaba '=' en la declaración");
        ASTNode expresion = expression();
        consumir(TokenType.PUNTO_COMA, "Se esperaba ';' al final de la declaración");
        return new VarDeclNode(id.getLexema(), expresion, var.getLinea());
    }

    /** {@code Assignment -> ID "=" Expression ";"}. */
    private ASTNode assignment() throws SyntaxException, SemanticException {
        Token id = consumir(TokenType.IDENTIFICADOR, "Se esperaba un identificador");
        consumir(TokenType.ASIGNACION, "Se esperaba '=' en la asignación");
        ASTNode expresion = expression();
        consumir(TokenType.PUNTO_COMA, "Se esperaba ';' al final de la asignación");
        return new AssignNode(id.getLexema(), expresion, id.getLinea());
    }

    /** {@code Print -> "imprimir" Expression ";"}. */
    private ASTNode print() throws SyntaxException, SemanticException {
        Token imp = consumir(TokenType.IMPRIMIR, "Se esperaba 'imprimir'");
        ASTNode expresion = expression();
        consumir(TokenType.PUNTO_COMA, "Se esperaba ';' al final de 'imprimir'");
        return new PrintNode(expresion, imp.getLinea());
    }

    /* ===================== Expresiones ===================== */

    /** {@code Expression -> Term ( ("+" | "-") Term )*}. */
    private ASTNode expression() throws SyntaxException, SemanticException {
        ASTNode nodo = term();
        while (verificar(TokenType.MAS) || verificar(TokenType.MENOS)) {
            Token operador = avanzar();
            ASTNode derecha = term();
            nodo = new BinaryOpNode(operador.getLexema(), nodo, derecha);
        }
        return nodo;
    }

    /** {@code Term -> Factor ( ("*" | "/") Factor )*}. */
    private ASTNode term() throws SyntaxException, SemanticException {
        ASTNode nodo = factor();
        while (verificar(TokenType.MULTIPLICACION) || verificar(TokenType.DIVISION)) {
            Token operador = avanzar();
            ASTNode derecha = factor();
            nodo = new BinaryOpNode(operador.getLexema(), nodo, derecha);
        }
        return nodo;
    }

    /** {@code Factor -> NUM | ID | "(" Expression ")"}. */
    private ASTNode factor() throws SyntaxException, SemanticException {
        if (verificar(TokenType.NUMERO)) {
            return new NumberNode(avanzar().getLexema());
        }
        if (verificar(TokenType.IDENTIFICADOR)) {
            Token id = avanzar();
            validarDeclaracion(id);
            return new IdentifierNode(id.getLexema());
        }
        if (verificar(TokenType.PARENTESIS_IZQ)) {
            avanzar();
            ASTNode interna = expression();
            consumir(TokenType.PARENTESIS_DER, "Se esperaba ')'");
            return interna;
        }

        Token t = actual();
        throw error("Operando inesperado '" + t.getLexema() + "'", t);
    }

    /* ===================== Validación semántica ===================== */

    /**
     * Verifica que un identificador usado dentro de una expresión ({@code Factor})
     * haya sido declarado previamente en la tabla de símbolos.
     *
     * @param id token identificador a validar.
     * @throws SemanticException si la variable no fue declarada.
     */
    private void validarDeclaracion(Token id) throws SemanticException {
        if (tablaSimbolos == null) {
            return;
        }
        if (!tablaSimbolos.existe(id.getLexema())) {
            throw new SemanticException(
                    "Error Semántico: La variable '" + id.getLexema() + "' no ha sido declarada",
                    id.getLinea(), id.getLexema());
        }
    }

    /* ===================== Utilidades del Parser ===================== */

    private boolean finTokens() {
        return actual().getTipo() == TokenType.EOF;
    }

    private Token actual() {
        return tokens.get(posicion);
    }

    private boolean verificar(TokenType tipo) {
        return actual().getTipo() == tipo;
    }

    private Token avanzar() {
        Token t = actual();
        if (!finTokens()) {
            posicion++;
        }
        return t;
    }

    /**
     * Consume el token actual si coincide con {@code tipo}; de lo contrario,
     * lanza un error sintáctico con la línea y el token encontrado.
     */
    private Token consumir(TokenType tipo, String mensaje) throws SyntaxException {
        if (verificar(tipo)) {
            return avanzar();
        }
        throw error(mensaje, actual());
    }

    /**
     * Construye una excepción sintáctica con el formato estándar del proyecto.
     */
    private SyntaxException error(String detalle, Token token) {
        String lexema = token.getTipo() == TokenType.EOF ? "fin de archivo" : token.getLexema();
        String mensaje = "Error Sintáctico en la línea " + token.getLinea()
                + ": " + detalle + " (se encontró '" + lexema + "')";
        return new SyntaxException(mensaje, token.getLinea(), lexema);
    }
}
