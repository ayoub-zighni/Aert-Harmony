package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Galeries;
import services.GaleriesService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AjouterGaleriesController {

    private GaleriesService gs;

    @FXML
    private TextField IdG;

    @FXML
    private TextField nomG;

    @FXML
    private TextField adresseG;

    @FXML
    private TextField villeG;

    @FXML
    private TextField paysG;

    @FXML
    private TextField capaciteMaxG;

    @FXML
    private TableView<Galeries> tableView;

    @FXML
    private TableColumn<Galeries, String> IdGColumn;

    @FXML
    private TableColumn<Galeries, String> nomGColumn;

    @FXML
    private TableColumn<Galeries, String> adresseGColumn;

    @FXML
    private TableColumn<Galeries, String> villeGColumn;

    @FXML
    private TableColumn<Galeries, String> paysGColumn;

    @FXML
    private TableColumn<?, ?> capaciteMaxGColumn;
    @FXML
    private Button ajouterButton;

    @FXML
    private Button modifierButton;

    @FXML
    private Button supprimerButton;

    public AjouterGaleriesController() {
        this.gs = new GaleriesService();
    }

    @FXML
    public void initialize() {
        IdGColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomGColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseGColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        villeGColumn.setCellValueFactory(new PropertyValueFactory<>("ville"));
        paysGColumn.setCellValueFactory(new PropertyValueFactory<>("pays"));
        capaciteMaxGColumn.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));

        // Charger les données dans le tableau
        loadData();
    }


    private void loadData() {
        try {
            // Récupérer toutes les données de la base de données
            List<Galeries> galeries = gs.recuperer();
            // Ajouter les données au TableView
            tableView.setItems(FXCollections.observableArrayList(galeries));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ajouterGalerie(ActionEvent event) {
        try {

            String nom = nomG.getText();
            String adresse = adresseG.getText();
            String ville = villeG.getText();
            String pays = paysG.getText();
            int capaciteMax = Integer.parseInt(capaciteMaxG.getText());

            if (nom.isEmpty() || adresse.isEmpty() || ville.isEmpty() || pays.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }

            Galeries newGallery = new Galeries(nom, adresse, ville, pays, capaciteMax);
            GaleriesService galeriesService = new GaleriesService(); // Créez une instance de GaleriesService
            galeriesService.ajouter(newGallery);

            // Ajouter la nouvelle galerie aux données du TableView
            tableView.getItems().add(newGallery);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }


    @FXML
    void modifierGalerie(ActionEvent event) {
        Galeries selectedGallery = tableView.getSelectionModel().getSelectedItem();
        if (selectedGallery != null) {
            // Récupérer l'ID de la galerie sélectionnée
            int idGalerie = selectedGallery.getId();

            // Récupérer les nouvelles valeurs depuis les champs de texte
            String newNom = nomG.getText();
            String newAdresse = adresseG.getText();
            String newVille = villeG.getText();
            String newPays = paysG.getText();
            int newCapaciteMax = Integer.parseInt(capaciteMaxG.getText());

            try {
                // Mettre à jour la galerie sélectionnée
                selectedGallery.setNom(newNom);
                selectedGallery.setAdresse(newAdresse);
                selectedGallery.setVille(newVille);
                selectedGallery.setPays(newPays);
                selectedGallery.setCapaciteMax(newCapaciteMax);

                // Mettre à jour la base de données
                gs.modifier(selectedGallery);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                throw new RuntimeException(e);
            }

            // Rafraîchir les données dans le TableView
            tableView.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une galerie à modifier.");
            alert.showAndWait();
        }
    }


    @FXML
    void supprimerGalerie(ActionEvent event) {
        Galeries selectedGallery = tableView.getSelectionModel().getSelectedItem();
        if (selectedGallery != null) {
            try {
                // Récupérer l'ID de la galerie sélectionnée
                String idGalerie = String.valueOf(selectedGallery.getId());

                // Supprimer la galerie sélectionnée de la base de données
                gs.supprimer(Integer.parseInt(idGalerie));

                // Supprimer la galerie sélectionnée de la liste d'éléments de votre TableView
                tableView.getItems().remove(selectedGallery);

                // Rafraîchir le TableView
                tableView.refresh();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une galerie à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    void event(ActionEvent event) {

        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ajouterEvenementController.fxml"));
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