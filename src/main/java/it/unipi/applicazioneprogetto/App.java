package it.unipi.applicazioneprogetto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    //metodo che cambia il root della scena e cambia anche l'altezza e larghezza
    //si assicura inoltre che la scena sia centrata rispetto allo schermo
    static void setRoot(String fxml, double x, double y) throws IOException {
        scene.setRoot(loadFXML(fxml));
        Window window = scene.getWindow();
        window.setWidth(x);
        window.setHeight(y);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        window.setX((screenBounds.getWidth() - x) / 2);
        window.setY((screenBounds.getHeight() - y) / 2);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}