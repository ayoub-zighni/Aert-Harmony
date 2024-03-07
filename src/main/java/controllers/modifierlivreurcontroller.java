package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Livreur;
import services.LivreurService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.stage.Stage;
public  class modifierlivreurcontroller implements Initializable {

    @FXML
    private Button modif;

    @FXML
    private Text modificationL;

    @FXML
    private TextField nomupdate;

    @FXML
    private TextField prenomupdate;

    @FXML
    private TextField regionupdate;

    @FXML
    private TextField emailupdate;

    @FXML
    private TextField telupdate;

    LivreurService ls = new LivreurService();

    private Livreur livreurupdate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize method
    }
public void initData(Livreur livreurupdate) {
    this.livreurupdate = livreurupdate;
    // Mettre à jour les champs de texte avec les informations du livreur
    nomupdate.setText(livreurupdate.getNom());
    prenomupdate.setText(livreurupdate.getPrenom());
    regionupdate.setText(livreurupdate.getRegion());
    emailupdate.setText(livreurupdate.getEmail());
    telupdate.setText(String.valueOf(livreurupdate.getTelephone()));
}
    @FXML
    void modifierLivreur(ActionEvent event) {

        livreurupdate.setNom(nomupdate.getText());
        livreurupdate.setPrenom(prenomupdate.getText());
        livreurupdate.setRegion(regionupdate.getText());
        livreurupdate.setEmail(emailupdate.getText());
        livreurupdate.setTelephone(Integer.parseInt(telupdate.getText()));

        // Call LivreurService to update Livreur in database
        try {
            ls.modifier(livreurupdate);
            System.out.println("Livreur modifié avec succès !");
            // Fermer la scène actuelle
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();

            // Ouvrir à nouveau l'interface d'affichage des livreurs
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherLivreur.fxml"));
            Parent afficherLivreurRoot = loader.load();
            Scene afficherLivreurScene = new Scene(afficherLivreurRoot);
            Stage afficherLivreurStage = new Stage();
            afficherLivreurStage.setScene(afficherLivreurScene);
            afficherLivreurStage.show();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur s'est produite lors de la modification du livreur : " + e.getMessage());
        }
    }
}



