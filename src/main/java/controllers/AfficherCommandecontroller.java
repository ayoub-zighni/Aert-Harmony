package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Commande;

import javafx.scene.control.*;
import services.CommandeService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class AfficherCommandecontroller {

    @FXML
    private TableColumn<Commande, String> adrcolumn;



    @FXML
    private TableColumn<Commande, Integer> idcolumn;



    @FXML
    private TextField rechercheCommande;

    @FXML
    private ResourceBundle resources;



    @FXML
    private Button modifbtn;

    @FXML
    private Button supprimbtn;

    @FXML
    private Button pdf;


    @FXML
    private TableColumn<Commande , Integer> numcolumn;

    @FXML
    private TableView<Commande> tableC;


    private final CommandeService cs = new CommandeService();


    public void initialize() {
        // Effet au survol pour chaque bouton
        DropShadow shadow = new DropShadow();

        modifbtn.setOnMouseEntered((e) -> {
            modifbtn.setEffect(shadow);
        });
        modifbtn.setOnMouseExited((e) -> {
            modifbtn.setEffect(null);
        });

        supprimbtn.setOnMouseEntered((e) -> {
            supprimbtn.setEffect(shadow);
        });
        supprimbtn.setOnMouseExited((e) -> {
            supprimbtn.setEffect(null);
        });
    }

    private void filterCo(int id) {
        try {
            List<Commande> commandes = cs.rechercherC(id);

            ObservableList<Commande> filteredList = FXCollections.observableArrayList(commandes);
            tableC.setItems(filteredList);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer cette exception correctement
        }
    }

    private void refreshCommandeList() {
        try {
            List<Commande> categories = cs.recuperer();
            ObservableList<Commande> categoryList = FXCollections.observableArrayList(categories);
            tableC.setItems(categoryList);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle this exception properly
        }
    }

/*

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<Commande,>() {
            private final Button editButton = new Button("Modify");
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Commande commande = getItem();
                    ModifierCommande(commande);
                });
                deleteButton.setOnAction(event -> {
                    Commande commande = getItem();
                    supprimerCommande(commande);
                });
            }


            protected void updateItem(Commande item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    } */

    @FXML
    void RechercherCommande(ActionEvent event) {

        int id1 = Integer.parseInt(rechercheCommande.getText()); // Récupérer le nom du livreur à rechercher

        try {
            List<Commande> commandes = cs.recuperer(); // Récupérer la liste complète des livreurs
            List<Commande> commandestrouves = new ArrayList<>(); // Liste pour stocker les livreurs trouvés

            // Parcourir la liste des livreurs et vérifier si le nom correspond à la recherche
            for (Commande c : commandes) {
                if (c.getIdC()==id1) {
                    commandestrouves.add(c);
                }
            }

            ObservableList<Commande> observableList = FXCollections.observableList(commandestrouves);

            tableC.setItems(observableList);

            idcolumn.setCellValueFactory(new PropertyValueFactory<>("idC"));
          ;
            adrcolumn.setCellValueFactory(new PropertyValueFactory<>("adresseLivraison"));
            numcolumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void ModifierCommande(ActionEvent actionEvent) {

        // Supposant que 'Nom' est le champ texte où le nom du livreur est saisi
        CommandeService cs = new CommandeService();
        // Récupérer les données de l'article à mettre à jour depuis l'interface utilisateur
        int id1 = Integer.parseInt(rechercheCommande.getText());
        Commande commmandeToupdate = null;
        List<Commande> commandes; // Replace with your code to retrieve the list of Livreur objects
        // Replace with your code to retrieve the list of Livreur objects
        try {
            commandes = cs.rechercherC(id1);
            commmandeToupdate = commandes.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (commmandeToupdate != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifiercommande.fxml"));
            Parent root = null;
            try {
                root = loader.load();
                modifiercommandecontroller modifiercommandecontroller = loader.getController();
                modifiercommandecontroller.initData(commmandeToupdate);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Le livreur avec le nom spécifié n'existe pas.");
        }

    }
    void AfficherCommande(ActionEvent event) {
        try {
            List<Commande> commandes = cs.recuperer();
            ObservableList<Commande> observableList = FXCollections.observableList(commandes);

            tableC.setItems(observableList);

            idcolumn.setCellValueFactory(new PropertyValueFactory<>("idC"));

            adrcolumn.setCellValueFactory(new PropertyValueFactory<>("adresseLivraison"));
            numcolumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    @FXML
    void supprimerCommande(ActionEvent event) {
        try {
            int id1 = Integer.parseInt(rechercheCommande.getText()); // Supposant que 'Nom' est le champ texte où le nom du livreur est saisi
            cs.supprimerC(String.valueOf(id1)); // Supposant que ls est un service ou un objet d'accès aux données
            Alert alerteSucces = new Alert(Alert.AlertType.INFORMATION);
            alerteSucces.setTitle("Succès");
            alerteSucces.setContentText("Le livreur a été supprimé");
            alerteSucces.showAndWait();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initializeData(ObservableList<Commande> commandes) {
        try {
            List<Commande> commandes1 = cs.recuperer();
            ObservableList<Commande> observableList = FXCollections.observableList(commandes1);
            CommandeService cs=new CommandeService();
            tableC.setItems(observableList);
            idcolumn.setCellValueFactory(new PropertyValueFactory<>("idC"));

            adrcolumn.setCellValueFactory(new PropertyValueFactory<>("adresseLivraison"));
            numcolumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));
            rechercheCommande.textProperty().addListener((obs, oldText, newText) -> {
                List<Commande> ae = null;
                try {
                    int searchId = Integer.parseInt(newText);
                    ae = cs.Search(searchId);
                } catch (NumberFormatException | SQLException e) {
                    throw new RuntimeException(e);
                }
                tableC.getItems().setAll(ae);});
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleGeneratePDF(ActionEvent event) {
        try {
            // Créer une instance de ReservationServices
            CommandeService cs = new CommandeService();

            // Récupérer la liste des réservations depuis la base de données
            List<Commande> commandes = cs.recuperer();

            // Utiliser un FileChooser pour permettre à l'utilisateur de choisir l'emplacement du fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                String filePath = selectedFile.getAbsolutePath();
                // Générer le fichier PDF avec la liste des réservations
                PdfGenerator.generatePDF(filePath, commandes);
                showErrorAlert("PDF généré avec succès", "Le fichier PDF a été créé avec succès : " + filePath);
            } else {
                showErrorAlert("Annulé", "La génération du PDF a été annulée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Une erreur s'est produite lors de la récupération des commandes : " + e.getMessage());
        }
    }


}




