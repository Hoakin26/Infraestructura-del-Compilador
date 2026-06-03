package proyectocompiladores;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

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
    private ListView<String> tokensListView;

    @FXML
    private TreeView<String> astTreeView;

    @FXML
    private TableView<?> symbolsTableView;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        sourceTextArea.setWrapText(false);
        tokensListView.setPlaceholder(new Label("Los tokens se mostrarán aquí en la Fase 2."));
        symbolsTableView.setPlaceholder(new Label("La tabla de símbolos se mostrará aquí en la Fase 3."));

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
            showError("No se pudo leer el archivo seleccionado.", exception.getMessage());
        }
    }

    @FXML
    private void handleAnalyzeSource() {
        statusLabel.setText("Fase 1 completada: la UI está lista. Lexer y parser aún no están implementados.");
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

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error al cargar archivo");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
