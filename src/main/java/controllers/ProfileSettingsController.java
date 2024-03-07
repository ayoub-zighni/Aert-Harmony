package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Utilisateur;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProfileSettingsController {
    @FXML
    private DatePicker Birth;

    @FXML
    private TextField Email;

    @FXML
    private TextField Fname;

    @FXML
    private TextField Lname;

    @FXML
    private TextField Nuser;

    @FXML
    private TextField Num;

    private Utilisateur utilisateur;
    public void initUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        // Populate text fields with utilisateur information
        Fname.setText(utilisateur.getPrenom());
        Lname.setText(utilisateur.getNom());
        Nuser.setText(utilisateur.getUsername());
        Email.setText(utilisateur.getEmail());
        Birth.setValue(utilisateur.getDateNaissance());
        Num.setText(utilisateur.getNumtel());
        // Populate other fields as needed
    }

    public void SaveChanges(ActionEvent actionEvent) {
        // Retrieve values from text fields
        String newFirstName = Fname.getText();
        String newLastName = Lname.getText();
        String newUsername = Nuser.getText();
        String newEmail = Email.getText();
        LocalDate newBirthDate = Birth.getValue();
        String newNum = Num.getText();

        // Create a new Utilisateur object with updated values
        Utilisateur updatedUtilisateur = new Utilisateur();
        updatedUtilisateur.setId(utilisateur.getId()); // Set the user ID
        updatedUtilisateur.setPrenom(newFirstName);
        updatedUtilisateur.setNom(newLastName);
        updatedUtilisateur.setUsername(newUsername);
        updatedUtilisateur.setEmail(newEmail);
        updatedUtilisateur.setDateNaissance(newBirthDate);
        updatedUtilisateur.setNumtel(newNum);

        // Update the user's information in the database
        try {
            UtilisateurService utilisateurService = new UtilisateurService();
            utilisateurService.UpdateProfile(updatedUtilisateur);
            System.out.println("User profile updated successfully.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the SQL exception appropriately (e.g., display an error message)
        }
    }

    public void BackInterface(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Interface.fxml"));
            Fname.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
    }

}
