package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.Commande;
import services.CommandeService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class modifiercommandecontroller implements Initializable {




    @FXML
    private Button btnmodifierC;

    @FXML
    private TextField lieuupdate;

    @FXML
    private Text modificationC;

    @FXML
    private TextField numupdate;
   CommandeService cs = new CommandeService();


    private Commande commandeToupdate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize method
    }

    public void initData(Commande commandeToupdate) {
        this.commandeToupdate = commandeToupdate;
        // Mettre à jour les champs de texte avec les informations de la commande
        lieuupdate.setText(commandeToupdate.getAdresseLivraison());
        numupdate.setText(String.valueOf(commandeToupdate.getNumTel()));
      //  idLupdate.setText(String.valueOf(commandeToupdate.getId())); // Convertir l'ID en une chaîne de caractères pour l'afficher
    }

    @FXML
    void modifiercommande(ActionEvent event) {
        // Convertir la valeur de DatePicker en LocalDate

        commandeToupdate.setAdresseLivraison(lieuupdate.getText());
        commandeToupdate.setNumTel(Integer.parseInt(numupdate.getText()));
      //  commandeToupdate.setId(Integer.parseInt(idLupdate.getText()));

        // Call LivreurService to update Livreur in database
        try {
            cs.modifierC(commandeToupdate);
            System.out.println("commande modifiée avec succès !");
            // Fermer la scène actuelle
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();

            // Ouvrir à nouveau l'interface d'affichage des livreurs
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Parent affichercommandeRoot = loader.load();
            Scene affichercommandeScene = new Scene(affichercommandeRoot);
            Stage affichercommandestage = new Stage();
            affichercommandestage.setScene(affichercommandeScene);
            affichercommandestage.show();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur s'est produite lors de la modification de la commande : " + e.getMessage());
        }
    }
}




