package proyectocompiladores.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Diálogo de error modal y sin decoración nativa, construido para encajar con
 * el tema oscuro (estilo Visual Studio Code) del resto de la aplicación.
 *
 * <p>Sustituye al {@code Alert} estándar de JavaFX, cuya apariencia clara no
 * coincide con la interfaz.</p>
 */
public final class VentanaError {

    private VentanaError() {
    }

    /**
     * Muestra el diálogo de error y bloquea hasta que el usuario lo cierre.
     *
     * @param owner   ventana propietaria (para centrar y modalidad).
     * @param titulo  título mostrado en la barra superior del diálogo.
     * @param mensaje cuerpo del mensaje de error.
     */
    public static void mostrar(Window owner, String titulo, String mensaje) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        if (owner != null) {
            stage.initOwner(owner);
        }

        Label tituloLabel = new Label(titulo);
        tituloLabel.getStyleClass().add("dialog-title");

        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);

        Button cerrarX = new Button("\u2715");
        cerrarX.getStyleClass().add("dialog-close");
        cerrarX.setOnAction(e -> stage.close());

        HBox header = new HBox(tituloLabel, espaciador, cerrarX);
        header.getStyleClass().add("dialog-header");
        header.setAlignment(Pos.CENTER_LEFT);

        habilitarArrastre(stage, header);

        Label icono = new Label("\u2715");
        icono.getStyleClass().add("dialog-icon");

        Label mensajeLabel = new Label(mensaje);
        mensajeLabel.getStyleClass().add("dialog-message");
        mensajeLabel.setWrapText(true);
        HBox.setHgrow(mensajeLabel, Priority.ALWAYS);

        HBox cuerpo = new HBox(12, icono, mensajeLabel);
        cuerpo.getStyleClass().add("dialog-body");
        cuerpo.setAlignment(Pos.CENTER_LEFT);

        Button aceptar = new Button("Aceptar");
        aceptar.getStyleClass().add("dialog-accept");
        aceptar.setDefaultButton(true);
        aceptar.setOnAction(e -> stage.close());

        HBox footer = new HBox(aceptar);
        footer.getStyleClass().add("dialog-footer");
        footer.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(header, cuerpo, footer);
        contenido.getStyleClass().add("dialog-root");

        StackPane raiz = new StackPane(contenido);
        raiz.setPadding(new Insets(0));
        raiz.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(raiz);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(
                VentanaError.class.getResource("/proyectocompiladores/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setMinWidth(380);
        stage.sizeToScene();
        stage.showAndWait();
    }

    /**
     * Permite mover la ventana sin decoración arrastrando la barra superior.
     */
    private static void habilitarArrastre(Stage stage, HBox header) {
        final double[] offset = new double[2];
        header.setOnMousePressed(event -> {
            offset[0] = event.getSceneX();
            offset[1] = event.getSceneY();
        });
        header.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offset[0]);
            stage.setY(event.getScreenY() - offset[1]);
        });
    }
}
