package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Commande;
import models.Livreur;
import services.CommandeService;
import services.LivreurService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ajoutercommandecontroller {

    CommandeService cs = new CommandeService();

    @FXML
    private TextField adrLivraison;

    @FXML
    private Button btnconfirmer;

    @FXML
    private DatePicker dateC;

    @FXML
    private TextField statutC;

    @FXML
    private TextField idL;


    @FXML
    void AjouterCommande(ActionEvent event) {
        try {
            //  Livreur livselectionne = new Livreur();
            Commande nouvelleCommande = new Commande();
            LocalDate selectedDate = dateC.getValue();
            java.sql.Date date = java.sql.Date.valueOf(selectedDate);

            nouvelleCommande.setDateCommande(date);
            // Supposons que vous avez des champs de saisie pour la date de commande, le statut de la commande et l'adresse de livraison
            // Utilisez la valeur de votre champ de saisie de date
            //   nouvelleCommande.setStatutCommande(statutC.getText()); // Utilisez la valeur de votre champ de saisie de statut
            //   nouvelleCommande.setAdresseLivraison(adrLivraison.getText()); // Utilisez la valeur de votre champ de saisie d'adresse

            //    nouvelleCommande.setId(Integer.parseInt(idL.getText()));

            // Appelez la méthode d'ajout de la commande dans le service de commande
            cs.ajouterC(new Commande(date, statutC.getText(), adrLivraison.getText(), Integer.parseInt(idL.getText())));

            // Affichez une boîte de dialogue de succès
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setContentText("Commande ajoutée avec succès");
            successAlert.showAndWait();
        } catch (SQLException e) {
            // En cas d'erreur, affichez une boîte de dialogue d'erreur avec le message d'erreur
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setContentText("Erreur lors de l'ajout de la commande : " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        // logo.getImageURL()
        dateC.getValue();
        statutC.getCharacters();
        adrLivraison.getCharacters();
        idL.getCharacters();

        // Charger les données dans le tableau
        loadData();
    }

    private void loadData() {
    }

    @FXML
    public void AfficherCommande(ActionEvent actionEvent) {
        try {

            List<Commande> commandes = cs.recuperer();
         //   ObservableList<Commande> observableList = FXCollections.observableList(commandes);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Parent root = loader.load();
            affichercommandecontroller controller = loader.getController();
            controller.initializeData(FXCollections.observableList(commandes));
            Stage stage = new Stage();
            stage.setTitle("Afficher commande");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading AfficherCommande.fxml");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}




