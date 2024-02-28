package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.categorie;
import services.ServicesCategorie;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class AjouterCategorieController {
    @FXML
    private AnchorPane nh;
    @FXML
    private Button ajouter_categorie;

    @FXML
    private TextField fx_categorie;


    private FXMLLoader createFXMLLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader;
    }


    @FXML
    void ajouter_categorie(ActionEvent event) {
        String categoryName=fx_categorie.getText();

        if (categoryName.length()==0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("information Dialog");
            alert.setHeaderText(null);

            alert.setContentText("erreur donner category name");
            alert.show();

        }
        else
        {
            categorie C = new categorie(categoryName);
            ServicesCategorie c1 = new ServicesCategorie();
            c1.ajouterCategorie(C);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert .setTitle("information dialog");
            alert.setHeaderText(null);
            alert.setContentText("Categorie ajouter avec succes !");
            alert.show();
        }
    }

    public void returnToAffiche(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = createFXMLLoader("/AfficherCategorie.fxml");
            Parent root = loader.load();
            nh.getChildren().setAll(root);
        } catch (IOException ex) {
            System.out.println("Erreur lors du chargement de la vue : " + ex.getMessage());
        }
    }
}


