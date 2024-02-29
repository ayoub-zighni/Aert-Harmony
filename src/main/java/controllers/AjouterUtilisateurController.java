package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Role;
import models.Utilisateur;
import org.w3c.dom.Text;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import javafx.scene.control.Label;

import javafx.scene.Node;

public class AjouterUtilisateurController {
    private final UtilisateurService Us = new UtilisateurService();
    @FXML
    private TextField Fname;

    @FXML
    private TextField Lname;

    @FXML
    private DatePicker birthP;

    @FXML
    private TextField emailP;

    @FXML
    private TextField mdpP;
    @FXML
    private ChoiceBox<Role> roleP;

    @FXML
    private Label pwdLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label addLabel;

    @FXML
    private Button xx;

    @FXML
    private CheckBox captcha;

    @FXML
    private TextField captchaField;

    @FXML
    private Label captchaLabel;

    @FXML
    private Label messageLabel;



    public void initialize() {
        // Create a list of Role enum values
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());

        // Populate the ChoiceBox with the list of roles
        roleP.setItems(roles);

        // Set a default value for the ChoiceBox if needed

        afficher();
    }

    private void clearFields() {
        Fname.clear();
        Lname.clear();
        emailP.clear();
        mdpP.clear();
        roleP.getSelectionModel().clearSelection();
        birthP.setValue(null);
        captchaField.clear();
    }
    public void addUser(ActionEvent actionEvent) {
        String prenom = Fname.getText();
        String nom = Lname.getText();
        String email = emailP.getText();
        String mdp = mdpP.getText();
        Role role = roleP.getValue();

        // Vérifier si tous les champs sont remplis
        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }
        // Vérifier le format de l'email
        if (!isValidEmail(email)) {
            // Show an error message for invalid email format
            emailLabel.setText("Please enter a valid email address.");
            return;
        } else {
            emailLabel.setText(""); // Clear the error message if email is valid
        }

        // Vérifier le format du mot de passe
        if (!isValidPassword(mdp)) {
            // Show an error message for invalid password format
            pwdLabel.setText("weak password.minimum length is 8 characters,the password should contain Upercase and lowercase");
            return;
        } else {
            pwdLabel.setText(""); // Clear the error message if password is valid
        }
        if (!verifyCaptcha()) {
            messageLabel.setText("Please confirm that you're not a robot.");
            return;
        }

        // If all validations pass, continue with adding the user...
        Utilisateur utilisateur = new Utilisateur(prenom, nom, email, mdp, birthP.getValue(), role);

        try {
            Us.ajouter(utilisateur); // Add the user to the database
            String successMessage = "Utilisateur ajouté avec succès.";

// Display the success message in the label
            addLabel.setText(successMessage);

// Create a PauseTransition to delay the disappearance of the message
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> addLabel.setText("")); // Clear the label text after 4 seconds
            delay.play();

            // Effacer les champs après l'ajout
            clearFields();
        } catch (SQLException e) {
            // En cas d'erreur, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error occurred while adding user: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private boolean verifyCaptcha() {
        String enteredCaptcha = captchaField.getText();
        String actualCaptcha = captchaLabel.getText();

        if (captcha.isSelected() && enteredCaptcha.equals(actualCaptcha)) {
            messageLabel.setText(""); // Clear the captcha error message
            return true;
        } else {
            // Update CAPTCHA
            afficher();
            messageLabel.setText("Please confirm that you're not a robot."); // Display the captcha error message
            return false;
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        // Utiliser une expression régulière pour valider l'email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Utiliser une expression régulière pour valider le mot de passe
        // Il doit contenir au moins une lettre minuscule, une lettre majuscule, un chiffre et avoir une longueur minimale de 8 caractères
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    @FXML
    public void displayuser(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherUtilisateur.fxml"));
            Fname.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
}
    private String generateRandomAlphabets(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = chars.charAt(random.nextInt(chars.length()));
            sb.append(c);
        }
        return sb.toString();
    }

    public void afficher() {
        // Initialize CAPTCHA
        String captchaText = generateRandomAlphabets(6); // Adjust the length as needed
        captchaLabel.setText(captchaText);
    }
}
