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

public class ModifierCategorieController {

    @FXML
    private Button ModifierButton;

    @FXML
    private Button returnToAffiche;
    @FXML
    private TextField fx_categoriename;

    @FXML
    private AnchorPane nh;

    private int categoryId; // Nouvelle variable pour stocker l'ID

    public void initialize() {
        // Ne pas initialiser le champ de texte ici
    }

    public void initData(int categoryId, String oldCoursName) {
        this.categoryId = categoryId;
        fx_categoriename.setText(oldCoursName);
    }
    @FXML
    void Modifier(ActionEvent event) {
        ServicesCategorie inter = new ServicesCategorie();
        String categoryName = fx_categoriename.getText();
        if (categoryName.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : veuillez donner un nom de catégorie.");
            alert.show();
        } else {
            categorie A = new categorie(categoryId, categoryName);
            inter.modifierCategorie(A);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);
            alert.setContentText("Catégorie modifiée avec succès !");
            alert.show();
        }
    }

    @FXML
    private void returnToAffiche(ActionEvent event) {
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


}