package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) {

        Parent root;
        try {
             root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherProduitFront.fxml")));
           // root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
           // root = FXMLLoader.load(getClass().getResource("/Chart.fxml"));

            Scene scene = new Scene(root, 800, 600);

            primaryStage.setTitle("art&harmonie");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace(); // Imprime la trace de la pile complète
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
