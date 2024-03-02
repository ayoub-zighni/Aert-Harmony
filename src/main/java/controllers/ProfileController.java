package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.UtilisateurService;

import java.sql.SQLException;


public class ProfileController {
    @FXML
    private PasswordField ProfilePwd;

    @FXML
    private PasswordField ProfileConf;

    @FXML
    private PasswordField ProfileNew;

    private UtilisateurService utilisateurService = new UtilisateurService();

    public void ChangePwd(ActionEvent actionEvent) {
        String ancienMotDePasse = ProfilePwd.getText();
        String nouveauMotDePasse = ProfileNew.getText();
        String confirmationMotDePasse = ProfileConf.getText();

        // Check if the new password matches the confirmation password
        if (!nouveauMotDePasse.equals(confirmationMotDePasse)) {
            // Handle the case where the new password and confirmation password do not match
            System.out.println("Le nouveau mot de passe et la confirmation du mot de passe ne correspondent pas.");
            return;
        }

        // Get the logged-in user's email (You need to implement this logic according to your application)
        String userEmail = "amindaboussi78@gmail.com"; // Replace this with the actual logic to get the logged-in user's email

        try {
            // Call the UtilisateurService method to change the password
            utilisateurService.changerMotDePasse(userEmail, ancienMotDePasse, nouveauMotDePasse);
            // Optionally, you can show a success message to the user
        } catch (SQLException e) {
            // Handle the SQLException appropriately (e.g., show an error message to the user)
            e.printStackTrace();
        }
    }
}
