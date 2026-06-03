package proyectocompiladores;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import proyectocompiladores.lexico.Lexer;
import proyectocompiladores.lexico.LexicalException;
import proyectocompiladores.lexico.Token;
import proyectocompiladores.semantico.ConstructorTablaSimbolos;
import proyectocompiladores.semantico.SemanticException;
import proyectocompiladores.semantico.Symbol;
import proyectocompiladores.semantico.SymbolTable;
import proyectocompiladores.sintactico.ASTNode;
import proyectocompiladores.sintactico.Parser;
import proyectocompiladores.sintactico.ProgramNode;
import proyectocompiladores.sintactico.SyntaxException;
import proyectocompiladores.ui.VentanaError;

/**
 * Controlador principal de la Fase 1.
 *
 * <p>Por ahora solo gestiona la carga del archivo fuente y deja preparados los
 * paneles visuales donde se conectarán el lexer, parser y tabla de símbolos en
 * fases posteriores.</p>
 */
public class MainController {

    @FXML
    private TextArea sourceTextArea;

    @FXML
    private TabPane resultsTabPane;

    @FXML
    private ListView<String> tokensListView;

    @FXML
    private TreeView<String> astTreeView;

    @FXML
    private TableView<Symbol> symbolsTableView;

    @FXML
    private TableColumn<Symbol, String> colIdentificador;

    @FXML
    private TableColumn<Symbol, String> colTipo;

    @FXML
    private TableColumn<Symbol, Integer> colLinea;

    @FXML
    private TableColumn<Symbol, String> colAlcance;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        sourceTextArea.setWrapText(false);
        tokensListView.setPlaceholder(new Label("Los tokens se mostrarán aquí al analizar."));
        symbolsTableView.setPlaceholder(new Label("La tabla de símbolos se mostrará aquí al analizar."));

        colIdentificador.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDato"));
        colLinea.setCellValueFactory(new PropertyValueFactory<>("linea"));
        colAlcance.setCellValueFactory(new PropertyValueFactory<>("alcance"));

        TreeItem<String> pendingAstRoot = new TreeItem<>("AST pendiente de construir");
        pendingAstRoot.setExpanded(true);
        pendingAstRoot.getChildren().add(new TreeItem<>("El parser se integrará en la Fase 4."));
        astTreeView.setRoot(pendingAstRoot);
    }

    @FXML
    private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo fuente MiniExp");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt", "*.miniexp", "*.mxp"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(getWindow());
        if (selectedFile == null) {
            return;
        }

        try {
            String sourceCode = Files.readString(selectedFile.toPath(), StandardCharsets.UTF_8);
            sourceTextArea.setText(sourceCode);
            statusLabel.setText("Archivo cargado: " + selectedFile.getName());
            clearFutureResults();
        } catch (IOException exception) {
            showError("Error al cargar archivo", exception.getMessage());
        }
    }

    /**
     * Ejecuta el pipeline completo de análisis (léxico, tabla de símbolos,
     * sintáctico y semántico) sobre el código del editor.
     *
     * <p>La pestaña de Tokens se llena en cuanto el Lexer tiene éxito. La Tabla
     * de Símbolos y el AST solo se muestran si <b>todo</b> el análisis termina
     * sin errores; si el Parser falla (error sintáctico o semántico), ambas se
     * vacían porque la compilación no se completó.</p>
     */
    @FXML
    private void handleAnalyzeSource() {
        String sourceCode = sourceTextArea.getText();
        if (sourceCode == null || sourceCode.isBlank()) {
            statusLabel.setText("No hay código fuente para analizar.");
            return;
        }

        tokensListView.getItems().clear();
        limpiarTablaYArbol();

        try {
            List<Token> tokens = new Lexer(sourceCode).tokenize();
            mostrarTokens(tokens);

            SymbolTable tabla = new ConstructorTablaSimbolos().construir(tokens);
            ProgramNode ast = new Parser(tokens, tabla).parse();

            mostrarTablaSimbolos(tabla);
            mostrarAst(ast);

            statusLabel.setText("Análisis completado: " + tokens.size() + " tokens, "
                    + tabla.tamano() + " símbolos, AST construido.");
        } catch (LexicalException exception) {
            tokensListView.getItems().clear();
            limpiarTablaYArbol();
            reportarError("Error léxico", exception.getMessage());
        } catch (SyntaxException exception) {
            limpiarTablaYArbol();
            reportarError("Error sintáctico", exception.getMessage());
        } catch (SemanticException exception) {
            limpiarTablaYArbol();
            reportarError("Error semántico", exception.getMessage());
        }
    }

    /**
     * Centraliza el reporte de errores: barra de estado, consola y diálogo.
     */
    private void reportarError(String titulo, String mensaje) {
        statusLabel.setText(mensaje);
        System.err.println(mensaje);
        showError(titulo, mensaje);
    }

    /**
     * Vacía la Tabla de Símbolos y el Árbol Sintáctico (AST).
     */
    private void limpiarTablaYArbol() {
        symbolsTableView.getItems().clear();
        astTreeView.setRoot(null);
    }

    /**
     * Vuelca la lista de tokens en la pestaña de la UI y en la consola.
     */
    private void mostrarTokens(List<Token> tokens) {
        System.out.println("===== TOKENS =====");
        for (Token token : tokens) {
            tokensListView.getItems().add(token.toString());
            System.out.println(token);
        }
        System.out.println("Total: " + tokens.size() + " tokens");

        seleccionarPestania(0);
    }

    /**
     * Vuelca la tabla de símbolos en la {@code TableView} y en la consola.
     */
    private void mostrarTablaSimbolos(SymbolTable tabla) {
        symbolsTableView.setItems(FXCollections.observableArrayList(tabla.valores()));

        System.out.println("===== TABLA DE SÍMBOLOS =====");
        for (Symbol simbolo : tabla.valores()) {
            System.out.println(simbolo);
        }
        System.out.println("Total: " + tabla.tamano() + " símbolos");
    }

    /**
     * Mapea el AST a {@code TreeItem} y lo muestra en la pestaña del árbol.
     */
    private void mostrarAst(ProgramNode ast) {
        TreeItem<String> raiz = construirTreeItem(ast);
        raiz.setExpanded(true);
        astTreeView.setRoot(raiz);

        System.out.println("===== AST =====");
        imprimirAst(ast, 0);
    }

    /**
     * Construye recursivamente un {@code TreeItem} a partir de un nodo del AST.
     */
    private TreeItem<String> construirTreeItem(ASTNode nodo) {
        TreeItem<String> item = new TreeItem<>(nodo.etiqueta());
        item.setExpanded(true);
        for (ASTNode hijo : nodo.getHijos()) {
            item.getChildren().add(construirTreeItem(hijo));
        }
        return item;
    }

    /**
     * Imprime el AST en consola con sangría para inspección rápida.
     */
    private void imprimirAst(ASTNode nodo, int nivel) {
        System.out.println("  ".repeat(nivel) + nodo.etiqueta());
        for (ASTNode hijo : nodo.getHijos()) {
            imprimirAst(hijo, nivel + 1);
        }
    }

    /**
     * Lleva el foco visual a una de las pestañas de resultados.
     */
    private void seleccionarPestania(int indice) {
        if (resultsTabPane == null) {
            return;
        }
        SingleSelectionModel<Tab> modelo = resultsTabPane.getSelectionModel();
        if (indice >= 0 && indice < resultsTabPane.getTabs().size()) {
            modelo.select(indice);
        }
    }

    private void clearFutureResults() {
        tokensListView.getItems().clear();
        symbolsTableView.getItems().clear();

        TreeItem<String> pendingAstRoot = new TreeItem<>("AST pendiente de construir");
        pendingAstRoot.setExpanded(true);
        astTreeView.setRoot(pendingAstRoot);
    }

    private Window getWindow() {
        return sourceTextArea.getScene() == null ? null : sourceTextArea.getScene().getWindow();
    }

    private void showError(String titulo, String mensaje) {
        VentanaError.mostrar(getWindow(), titulo, mensaje);
    }
}
