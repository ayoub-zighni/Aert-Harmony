package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Livreur;
import services.LivreurService;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.sql.SQLException;


public class ajouterlivreurcontroller {


    public Button ajouter;
    LivreurService ls = new LivreurService();


    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField region;


    @FXML
    private Label fill;

    @FXML
    private ImageView logo;

    @FXML
    private TextField email;

    @FXML
    private TextField telephone;




    @FXML
    void AjouterLivreur(ActionEvent event) {

        String Nom = nom.getText();
        String Prenom = prenom.getText();
        String Region = region.getText();
        String Email = email.getText();
        String Tel = telephone.getText();

        if (Nom.isEmpty() || Prenom.isEmpty() || Region.isEmpty() || Email.isEmpty() || Tel.isEmpty()) {
            fill.setText("Veuillez remplir tous les champs ");
        }
         else  if (!isValidEmail(email.getText())) {
            fill.setText("Veuillez saisir une adresse e-mail valide  ");
        }
        else  if (!isValidPhoneNumber(telephone.getText())) {
            fill.setText("Veuillez saisir un numéro de télephone valide ");
        }
        else {

            try {
                Livreur livreur = new Livreur();
                livreur.setNom(Nom);
                livreur.setPrenom(Prenom);
                livreur.setRegion(Region);
                livreur.setEmail(Email);
                livreur.setTelephone(Integer.parseInt(Tel));

                ls.ajouter(livreur);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setContentText("Livreur ajouté");
                successAlert.showAndWait();


            } catch (SQLException e) {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Afficherlivreur.fxml"));
                nom.getScene().setRoot(root);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private boolean isValidEmail(String email) {
        // Utiliser une expression régulière simple pour valider l'email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Vérifier si le numéro de téléphone a exactement 8 chiffres
        return phoneNumber.matches("\\d{8}");
    }

    @FXML
    public void initialize() {
           // logo.getImageURL()
            nom.getCharacters();
            prenom.getCharacters();
            region.getCharacters();
            email.getCharacters();
            telephone.getCharacters();


            // Charger les données dans le tableau
            loadData();
        }

    private void loadData() {
    }



    @FXML
    void AfficherLivreur(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Afficherlivreur.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


}