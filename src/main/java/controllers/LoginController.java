package controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import models.Utilisateur;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class LoginController {

    @FXML
    private TextField Field1;

    @FXML
    private TextField Field2;

    @FXML
    private Label messageLabel;

    @FXML
    private CheckBox captcha;

    @FXML
    private TextField captchaField;

    private UtilisateurService us = new UtilisateurService();

    public void connecter(ActionEvent actionEvent) {
        String email = Field1.getText();
        String password = Field2.getText();

        try {
            Utilisateur utilisateur = us.login(email, password);
            if (utilisateur != null) {
                // Login successful, navigate to the display interface after 2 seconds
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/AfficherUtilisateur.fxml"));
                        Field1.getScene().setRoot(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                pause.play();
                messageLabel.setText("Login successful. Welcome, " + utilisateur.getPrenom());
            } else {
                // Login failed, display error message or handle accordingly
                messageLabel.setText("Invalid email or password. Please try again.");
            }
        } catch (SQLException ex) {
            // Handle SQL exception
            ex.printStackTrace();
            // Display error message or log the exception
        }
    }

    public void addInterface(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterUtilisateur.fxml"));
            Field1.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
    }

    public void ForgotPassword(MouseEvent mouseEvent) {
    }
}
