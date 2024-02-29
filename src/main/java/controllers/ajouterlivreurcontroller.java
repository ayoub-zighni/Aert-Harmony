package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    void AjouterLivreur(ActionEvent event) {

        String Nom = nom.getText();
        String Prenom = prenom.getText();
        String Region = region.getText();

        if (Nom.isEmpty() || Prenom.isEmpty() || Region.isEmpty()) {
            fill.setText("Please fill in all the fields");
        } else {

            try {
                Livreur livreur = new Livreur();
                livreur.setNom(Nom);
                livreur.setPrenom(Prenom);
                livreur.setRegion(Region);

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

    @FXML
    public void initialize() {
           // logo.getImageURL()
            nom.getCharacters();
            prenom.getCharacters();
            region.getCharacters();


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
