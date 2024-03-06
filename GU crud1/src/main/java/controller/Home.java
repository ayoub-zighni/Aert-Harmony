package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {

    @FXML
    private AnchorPane nh;

    @FXML
    void goToCategorie(ActionEvent event) {

        try {
            FXMLLoader loader = createFXMLLoader("/AfficherCategorie.fxml");
            Parent root = loader.load();
            nh.getChildren().setAll(root);
        } catch (IOException ex) {
            System.out.println("Erreur lors du chargement de la vue : " + ex.getMessage());
        }
    }
    private FXMLLoader createFXMLLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader;
    }


    @FXML
    void goToProduit(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherProduit.fxml"));
            Parent root = loader.load();

            // Show the Modif.fxml interface
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("ART");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void goToStat(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = createFXMLLoader("/Chart.fxml");
            Parent root = loader.load();
            nh.getChildren().setAll(root);
        } catch (IOException ex) {
            System.out.println("Erreur lors du chargement de la vue : " + ex.getMessage());
        }
    }
}

