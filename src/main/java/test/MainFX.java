package test;
//import models.Livreur;
//import models.Livreur;
import models.Commande;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
    public MainFX() throws IOException {
    }

    @Override
    public void start(Stage stage) throws Exception {
     FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterLivreur.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Ajout Livreur");
        stage.setScene(scene);
        stage.show();
      /* FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/AjouterCommande.fxml"));
        Parent rootC = loader1.load();
        Scene sceneC = new Scene(rootC);
        stagee.setTitle("Ajout commande");
        stagee.setScene(sceneC);
        stagee.show();*/

/*
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
        Parent root1 = loader2.load();
        Scene scene1 = new Scene(root1);
        stage.setTitle("Afficher Livreur");
        stage.setScene(scene1);
        stage.show();
*/
    }

    public static void main (String[] args){
        launch(args);
    }
    }

