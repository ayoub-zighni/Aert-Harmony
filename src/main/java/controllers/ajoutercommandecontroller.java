package controllers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Commande;
import services.CommandeService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ajoutercommandecontroller {

    CommandeService cs = new CommandeService();

    @FXML
    private Button mape;

    @FXML
    private TextField telC;

    @FXML
    private TextField lieuC;


    @FXML
    private TextField idL;

    @FXML
    private Label fillC;


    @FXML
    void AjouterCommande(ActionEvent event) {

        String lieu = lieuC.getText();
        String Tel = telC.getText();
        //  String idliv = idL.getText();

        if (lieu.isEmpty() || Tel.isEmpty()) {
            fillC.setText("Veuillez remplir tous les champs");
        }
            else if (!isValidPhoneNumber(telC.getText())) {
            // Vérifier le format de la date (YYYY-MM-DD)
            fillC.setText("Veuillez saisir un numéro de télephone valide "); }
        else{
            try {
                Commande c = new Commande();
                c.setAdresseLivraison(lieu);
                c.setNumTel(Integer.parseInt(Tel));
                cs.ajouterCo(c);

                // Afficher une boîte de dialogue de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setContentText("Commande ajoutée avec succès");
                successAlert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);

            } catch (NumberFormatException e) {
                // En cas d'erreur de conversion du texte en entier
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setContentText("Veuillez entrer un ID valide.");
                errorAlert.showAndWait();
            }
        }

    }

private boolean isValidPhoneNumber(String phoneNumber) {
    // Vérifier si le numéro de téléphone a exactement 8 chiffres
    return phoneNumber.matches("\\d{8}");
}

        @FXML
        public void initialize () {
            // logo.getImageURL()
            telC.getCharacters();
            lieuC.getCharacters();
          //  idL.getCharacters();

            // Charger les données dans le tableau
            loadData();
        }

        private void loadData () {
        }

        @FXML
        public void AfficherCommande (ActionEvent actionEvent){
            try {

                List<Commande> commandes = cs.recuperer();
                //   ObservableList<Commande> observableList = FXCollections.observableList(commandes);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
                Parent root = loader.load();
                AfficherCommandecontroller controller = loader.getController();
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
        private void showAlert (Alert.AlertType alertType, String title, String message){
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

    @FXML
    void mape(ActionEvent event) {

        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Map.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setTitle("doorstep!");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            System.err.println("IOException: Error loading listrep.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: An unknown error occurred");
            e.printStackTrace();
        }


    }
    }





