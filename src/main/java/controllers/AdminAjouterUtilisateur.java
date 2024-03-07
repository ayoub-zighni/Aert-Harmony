package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.Role;
import models.Utilisateur;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdminAjouterUtilisateur {
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
    private TextField Username;

    @FXML
    private TextArea Des;

    public void initialize() {
        // Create a list of Role enum values excluding the Admin role
        List<Role> roles = Arrays.stream(Role.values())
                .collect(Collectors.toList());

        // Populate the ChoiceBox with the list of roles
        ObservableList<Role> rolesObservableList = FXCollections.observableArrayList(roles);
        roleP.setItems(rolesObservableList);

        // Set a default value for the ChoiceBox if needed
    }

    private void clearFields() {
        Fname.clear();
        Lname.clear();
        emailP.clear();
        mdpP.clear();
        Username.clear();
        Des.clear();
        birthP.setValue(null);
    }
    public void addUser(ActionEvent actionEvent) {
        String prenom = Fname.getText();
        String nom = Lname.getText();
        String email = emailP.getText();
        String mdp = mdpP.getText();
        String username = Username.getText();
        String description = Des.getText();
        Role role = roleP.getValue();

        // Vérifier si tous les champs sont remplis
        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
            showAlert("Warning", "Please fill in all the required fields.");
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
            pwdLabel.setText("Weak password. Minimum length is 8 characters. The password should contain both uppercase and lowercase letters, and at least one digit.");
            return;
        } else {
            pwdLabel.setText(""); // Clear the error message if password is valid
        }

        // Check if the email is already in use
        try {
            if (Us.emailExists(email)) {
                showAlert("Error", "This email is already in use. Please choose another one.");
                return;
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while checking email availability: " + e.getMessage());
            return;
        }

        // If all validations pass and the email is not already in use, continue with adding the user...
        LocalDate birthDate = birthP.getValue();
        Utilisateur utilisateur;
        // If username and description are both empty
        if (description.isEmpty() && username.isEmpty()) {
            utilisateur = new Utilisateur(prenom, nom, email, mdp, birthDate, role);
        }
// If only description is empty
        else if (description.isEmpty()) {
            utilisateur = new Utilisateur(prenom, nom, username, email, mdp, null, birthDate, role);
        }
// If only username is empty
        else if (username.isEmpty()) {
            utilisateur = new Utilisateur(prenom, nom, email, mdp, description, birthDate, role);
        }
// If both username and description are provided
        else {
            utilisateur = new Utilisateur(prenom, nom, username, email, mdp, description, birthDate, role);

        }
        try {
            Us.ajouter(utilisateur);
            showAlert("Success", "User added successfully.");
            clearFields(); // Clear the input fields after successful addition
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while adding the user: " + e.getMessage());
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

    public void Close(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherUtilisateur.fxml"));
            Fname.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
    }
}

