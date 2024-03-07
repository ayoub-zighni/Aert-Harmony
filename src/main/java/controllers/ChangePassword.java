package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import models.Utilisateur;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;

public class ChangePassword {
    @FXML
    private PasswordField Pfield;

    @FXML
    private PasswordField Pfield1;

    @FXML
    private Label messageLabel;


    private UtilisateurService us = new UtilisateurService();

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void ChangeThePassword(ActionEvent actionEvent) {
        if (utilisateur == null) {
            // Handle the case where utilisateur is null
            messageLabel.setText("Error: Utilisateur object is null.");
            return;
        }

        String newPassword = Pfield.getText();
        String confirmPassword = Pfield1.getText();

        if (newPassword.equals(confirmPassword)) {
            try {
                // Update the password for the current utilisateur
                us.updatePasswordByEmail(utilisateur.getEmail(), newPassword);
                messageLabel.setText("Password changed successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                messageLabel.setText("Error: Failed to change password. Please try again later.");
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
                Pfield1.getScene().setRoot(root);

            } catch (IOException e) {
                e.printStackTrace(); // Print the full stack trace
            }

        } else {
            messageLabel.setText("Passwords do not match. Please enter the same password in both fields.");
        }
    }
}

