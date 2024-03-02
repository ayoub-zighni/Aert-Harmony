package test;

import controllers.AfficherUtilisateurController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayPhoto.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // Créer une instance de AfficherPersonneController et la définir comme données utilisateur de la scène
        AfficherUtilisateurController afficherUtilisateurController = new AfficherUtilisateurController();
        scene.setUserData(afficherUtilisateurController);

        primaryStage.setTitle("Gérer personnes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}