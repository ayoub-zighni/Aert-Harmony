package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import models.Utilisateur;

import java.io.IOException;

public class InterfaceController {
    @FXML
    private Label messageLabel;

    private Utilisateur utilisateur;

    public void initUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void deconnecter(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            messageLabel.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }

    }

    public void UpdateProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileSettings.fxml"));
            Parent root = loader.load();

            ProfileSettingsController profileSettingsController = loader.getController();
            profileSettingsController.initUtilisateur(utilisateur); // Pass Utilisateur object to ProfileSettingsController

            messageLabel.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
    }

    public void DisplayProducts(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherProduitFront.fxml"));
            messageLabel.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }

    }

    public void importevent(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ImportController.fxml"));
            messageLabel.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
    }

    public void BaheFront(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterLivreur.fxml"));
            messageLabel.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
    }
}
