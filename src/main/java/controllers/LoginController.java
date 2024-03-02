package controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import models.Role;
import models.Utilisateur;
import org.w3c.dom.Text;
import services.UtilisateurService;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Random;
import javafx.application.Platform;

public class LoginController {

    @FXML
    private TextField Field1;

    @FXML
    private PasswordField Field2;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label messageLabel;

    @FXML
    private CheckBox captcha;

    @FXML
    private TextField captchaField;

    @FXML
    private CheckBox ShowPassword;

    private UtilisateurService us = new UtilisateurService();

    public void initialize() {
        // Listener for ShowPassword checkbox
        ShowPassword.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                passwordTextField.setText(Field2.getText());
                passwordTextField.setVisible(true);
                Field2.setVisible(false);
            } else {
                Field2.setText(passwordTextField.getText());
                Field2.setVisible(true);
                passwordTextField.setVisible(false);
            }
        });
    }
    public void connecter(ActionEvent actionEvent) {
        String email = Field1.getText();
        String password = Field2.getText();

        try {
            Utilisateur utilisateur = us.login(email, password);
            if (utilisateur != null) {
                // Check the role of the logged-in user
                Role role = utilisateur.getRole();
                if ("Admin".equals(role.getRoleName())) { // Ensure case-sensitive comparison
                    // Admin user, load the Dashboard interface
                    loadInterface("/Dashboard.fxml");
                } else {
                    // Other user, load the Interface interface
                    loadInterface("/Interface.fxml");
                }
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

    private void loadInterface(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Field1.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
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
