package controllers;
import com.twilio.Twilio;
import com.twilio.rest.lookups.v1.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.Properties;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
import test.Main;

public class LoginController {
    @FXML
    private Button sendButton;

    @FXML
    private TextField Field1;

    @FXML
    private PasswordField Field2;

    @FXML
    private TextField passwordTextField;

    private EmailSender emailSender = new EmailSender();

    @FXML
    private Label messageLabel;

    @FXML
    private CheckBox ShowPassword;

    @FXML
    private Button Vbutton;

    private String currentVerificationCode;

    private final SecureRandom random = new SecureRandom();

    private UtilisateurService us = new UtilisateurService();

    private InterfaceController interfaceController;

    private Utilisateur utilisateur;

    private int wrongPasswordAttempts = 0;
    private long banStartTime = 0;
    private final long BAN_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds


    public void setInterfaceController(InterfaceController interfaceController) {
        this.interfaceController = interfaceController;
    }

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
                Vbutton.setVisible(false);
            }
        });
    }

    public void connecter(ActionEvent actionEvent) {
        String email = Field1.getText();
        String password = Field2.getText();

        // Check if the user is currently banned
        if (isBanned()) {
            messageLabel.setText("Your account is temporarily banned. Please try again later.");
            return;
        }

        try {
            Utilisateur utilisateur = us.login(email, password);
            Main.userconn =utilisateur;
            if (utilisateur != null) {
                // Reset the wrong password attempts if login is successful
                wrongPasswordAttempts = 0;

                // Check the role of the logged-in user
                Role role = utilisateur.getRole();
                if ("Admin".equals(role.getRoleName())) { // Ensure case-sensitive comparison
                    // Admin user, load the Dashboard interface
                    loadInterface("/Dashboard.fxml", utilisateur);
                } else {
                    // Other user, load the Interface interface
                    loadInterface("/Interface.fxml", utilisateur);
                }
                messageLabel.setText("Login successful. Welcome, " + utilisateur.getPrenom());
            } else {
                // Increment the wrong password attempts count
                wrongPasswordAttempts++;

                if (wrongPasswordAttempts >= 3) {
                    // Ban the user for 5 minutes
                    banStartTime = System.currentTimeMillis();
                }

                // Login failed, display error message or handle accordingly
                messageLabel.setText("Invalid email or password. Please try again.");
            }
        } catch (SQLException ex) {
            // Handle SQL exception
            ex.printStackTrace();
            // Display error message or log the exception
        }
    }

    private boolean isBanned() {
        if (banStartTime == 0) {
            // User is not banned
            return false;
        } else {
            // Calculate the ban duration
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - banStartTime;
            return elapsedTime < BAN_DURATION;
        }
    }


    private void loadInterface(String fxmlPath, Utilisateur utilisateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            if (fxmlPath.equals("/Interface.fxml")) {
                InterfaceController interfaceController = loader.getController();
                interfaceController.initUtilisateur(utilisateur);
            }
           /* if (fxmlPath.equals("/Dashboard.fxml")) {
                DashboardController dashboardController = loader.getController();
                dashboardController.initUtilisateur(utilisateur);
            }*/
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
        String email = Field1.getText().trim(); // Trim whitespace from the input email
        try {
            utilisateur = us.getUserByEmail(email);

            // Update the utilisateur object
            if (utilisateur != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChoseMethode.fxml"));
                Parent root = loader.load();
                ChoseMethodeController choseMethodeController = loader.getController();
                choseMethodeController.setUtilisateur(utilisateur);
                messageLabel.getScene().setRoot(root);
            } else {
                // If email doesn't exist in the database, show an error message
                messageLabel.setText("Email does not exist.");
            }
        } catch (SQLException | IOException ex) {
            // Handle exceptions
            ex.printStackTrace();
            messageLabel.setText("Error: Please try again later.");
        }
    }

    public void LoginWithFacebook(ActionEvent actionEvent) {
    }

    }
