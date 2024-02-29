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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Commande;
import models.Livreur;
import services.CommandeService;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class affichercommandecontroller {

    @FXML
    private TableColumn<Commande, String> adrcolumn;

    @FXML
    private TableColumn<Commande, Date> datecolumn;

    @FXML
    private TableColumn<Commande, Integer> idcolumn;

    @FXML
    private TableColumn<Commande, String> statutcolumn;

    @FXML
    private TableColumn<Commande, ?> actionColumn;

    @FXML
    private TextField rechercheCommande;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button modifbtn;

    @FXML
    private Button supprimbtn;


    @FXML
    private TableView<Commande> tableC;


    private final CommandeService cs = new CommandeService();

    private void filterCommandeList(int id) {
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

    void ModifierCommande(Commande Commande ) {

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCommande.fxml"));
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
    @FXML
    void AfficherCommande(ActionEvent event) {
        try {
            List<Commande> commandes = cs.recuperer();
            ObservableList<Commande> observableList = FXCollections.observableList(commandes);

            tableC.setItems(observableList);

            idcolumn.setCellValueFactory(new PropertyValueFactory<>("idC"));
            datecolumn.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
            statutcolumn.setCellValueFactory(new PropertyValueFactory<>("statutCommande"));
            adrcolumn.setCellValueFactory(new PropertyValueFactory<>("adresseLivraison"));


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void supprimerCommande(Commande commande) {
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

    public void initializeData(ObservableList<Commande> commandes) {
        // Assurez-vous que la TableView et les colonnes sont correctement initialisées
        // puis ajoutez les données des commandes à la TableView
        tableC.setItems(commandes);

        // Assurez-vous que les propriétés des colonnes sont correctement liées aux attributs de la classe Commande
        idcolumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        datecolumn.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        statutcolumn.setCellValueFactory(new PropertyValueFactory<>("statutCommande"));
        adrcolumn.setCellValueFactory(new PropertyValueFactory<>("adresseLivraison"));
    }
}




