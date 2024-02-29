package controllers;

import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Livreur;
import services.LivreurService;

import java.awt.*;
//import java.awt.Dialog;
import java.awt.Label;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class afficherlivreurcontroller {

    @FXML
    private TableColumn<Livreur, String> Nom;

    @FXML
    private TableColumn<Livreur, String> Prenom;

    @FXML
    private TableColumn<Livreur, String> Region;

    @FXML
    private TableView<Livreur> tableL;

    @FXML
    private Button supprimer;

    @FXML
    private Button afficher;

    @FXML
    private Button rechercher;


    @FXML
    private Button modifier;
    @FXML
    private ButtonType confirmerButtonType;

    @FXML
    private TextField rechercherparnom;

    public void initialize1() {
        // Effet au survol pour chaque bouton
        DropShadow shadow = new DropShadow();
        afficher.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, (e) -> {
            afficher.setEffect(shadow);
        });
        afficher.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, (e) -> {
            afficher.setEffect(null);
        });

        supprimer.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, (e) -> {
            supprimer.setEffect(shadow);
        });
        supprimer.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, (e) -> {
            supprimer.setEffect(null);
        });

        rechercher.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, (e) -> {
            rechercher.setEffect(shadow);
        });
        rechercher.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, (e) -> {
            rechercher.setEffect(null);
        });

        modifier.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, (e) -> {
            modifier.setEffect(shadow);
        });
        modifier.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, (e) -> {
            modifier.setEffect(null);
        });
    }
    LivreurService ls = new LivreurService();

    private Livreur livreurmodifier;

    @FXML
    void initialize() {
        try {
            List<Livreur> livreurs = ls.recuperer();
            ObservableList<Livreur> observableList = FXCollections.observableList(livreurs);
   LivreurService livreur=new LivreurService();
            tableL.setItems(observableList);

            Nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            Prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            Region.setCellValueFactory(new PropertyValueFactory<>("region"));
            tableL.setItems(observableList);
            rechercherparnom.textProperty().addListener((obs, oldText, newText) -> {
                List<Livreur> ae = null;
                try {
                    ae = livreur.Search(newText);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                tableL.getItems().setAll(ae);});
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void AfficherLivreur(ActionEvent event) {
        try {
            List<Livreur> livreurs = ls.recuperer();
            ObservableList<Livreur> observableList = FXCollections.observableList(livreurs);

            tableL.setItems(observableList);

            Nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            Prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            Region.setCellValueFactory(new PropertyValueFactory<>("region"));

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /*
    @FXML
    void ModifierLivreur(ActionEvent event) {
        Livreur livreurSelectionne = tableL.getSelectionModel().getSelectedItem(); // Récupérer le livreur sélectionné dans la table

        if (livreurSelectionne != null) {
            // Créer une boîte de dialogue pour saisir les nouvelles données du livreur
            Dialog<Livreur> dialog = new Dialog<>();
            dialog.setTitle("Modifier Livreur");
            dialog.setHeaderText("Modifier les données du livreur");

            // Créer les champs de saisie pour les nouvelles données du livreur
            TextField nomField = new TextField(livreurSelectionne.getNom());
            TextField prenomField = new TextField(livreurSelectionne.getPrenom());
            TextField regionField = new TextField(livreurSelectionne.getRegion());

            // Ajouter les champs de saisie à la boîte de dialogue
            GridPane grid = new GridPane();
            grid.add(new Label("Nom: "), 0, 0);
            grid.add(nomField, 1, 0);
            grid.add(new Label("Prénom: "), 0, 1);
            grid.add(prenomField, 1, 1);
            grid.add(new Label("Région: "), 0, 2);
            grid.add(regionField, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Ajouter les boutons de confirmation et d'annulation
            ButtonType confirmerButtonType = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmerButtonType, ButtonType.CANCEL);

            // Récupérer les résultats de la boîte de dialogue
            Optional<Livreur> resultat = dialog.showAndWait();

            // Si les nouvelles données ont été validées, mettre à jour le livreur dans la liste
            resultat.ifPresent(nouveauLivreur -> {
                try {
                    ls.modifier(livreurSelectionne, nouveauLivreur);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setContentText("Livreur modifié");
                    successAlert.showAndWait();

                    // Rafraîchir la liste des livreurs affichés
                    AfficherLivreur(event);

                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Aucun livreur sélectionné");
            alert.showAndWait();
        }
    } */
    /*
    @FXML
    void ModifierLivreur(ActionEvent event) {
        try {
            // Récupérer les informations du livreur depuis l'interface utilisateur
            String ancienNom = // récupérer l'ancien nom du livreur à modifier depuis votre interface utilisateur ;
                    String nouveauNom = // récupérer le nouveau nom du livreur depuis votre interface utilisateur ;
                    String prenom = // récupérer le prénom du livreur depuis votre interface utilisateur ;
                    String region = // récupérer la région du livreur depuis votre interface utilisateur ;

                    // Créer un objet Livreur avec les nouvelles informations
                    Livreur livreurModifie = new Livreur(nouveauNom, prenom, region);

            // Appeler la méthode modifier du LivreurService
            ls.modifier(livreurModifie, ancienNom);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setContentText("Livreur modifié");
            successAlert.showAndWait();

            // Redirection vers la page d'affichage des livreurs après la modification
            Parent root = FXMLLoader.load(getClass().getResource("/Afficherlivreur.fxml"));
            nom.getScene().setRoot(root);

        } catch (SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    @FXML
    void RechercherLivreur(ActionEvent event) {

        String nomLivreur = rechercherparnom.getText(); // Récupérer le nom du livreur à rechercher

        try {
            List<Livreur> livreurs = ls.recuperer(); // Récupérer la liste complète des livreurs
            List<Livreur> livreursTrouves = new ArrayList<>(); // Liste pour stocker les livreurs trouvés

            // Parcourir la liste des livreurs et vérifier si le nom correspond à la recherche
            for (Livreur livreur : livreurs) {
                if (livreur.getNom().equalsIgnoreCase(nomLivreur)) {
                    livreursTrouves.add(livreur);
                }
            }

            ObservableList<Livreur> observableList = FXCollections.observableList(livreursTrouves);

            tableL.setItems(observableList);

            Nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            Prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            Region.setCellValueFactory(new PropertyValueFactory<>("region"));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void filterCategoryList(String keyword) {
        try {
            List<Livreur> categories = ls.rechercher(keyword);
            ObservableList<Livreur> filteredList = FXCollections.observableArrayList(categories);
            tableL.setItems(filteredList);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer cette exception correctement
        }
    }

    @FXML
    void ModifierLivreur(ActionEvent event) {

         //   String nom = rechercherparnom.getText(); // Supposant que 'Nom' est le champ texte où le nom du livreur est saisi

            LivreurService ls = new LivreurService();  // Récupérer les données de l'article à mettre à jour depuis l'interface utilisateur
            String nom1 = rechercherparnom.getText();
        Livreur livreurToUpdate = null;
        List<Livreur> livreurs ; // Replace with your code to retrieve the list of Livreur objects
        // Replace with your code to retrieve the list of Livreur objects
        try {
            livreurs = ls.rechercher(nom1);
             livreurToUpdate = livreurs.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (livreurToUpdate != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifierlivreur.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                    modifierlivreurcontroller modifierlivreurController = loader.getController();
                    modifierlivreurController.initData(livreurToUpdate);
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
    /*
    void modifierReservation(ActionEvent event) {
        try {
          this.livreurmodifier.setNom(this.Nom.getText());
            this.rs.modifier(this.reservationToModify.getId(), this.reservationToModify);
            Stage stage = (Stage)this.nvidEventTF.getScene().getWindow();
            stage.close();
            this.showAlert("Modification réussie", "La réservation a été modifiée avec succès.");
        } catch (Exception var3) {
            this.showAlert("Erreur", "Une erreur s'est produite lors de la modification de la réservation.");
        }

    }*/
    @FXML
    void supprimerLivreur(ActionEvent event) {
        try {
            String nom = rechercherparnom.getText(); // Supposant que 'Nom' est le champ texte où le nom du livreur est saisi
            ls.supprimer(nom); // Supposant que ls est un service ou un objet d'accès aux données
            Alert alerteSucces = new Alert(Alert.AlertType.INFORMATION);
            alerteSucces.setTitle("Succès");
            alerteSucces.setContentText("Le livreur a été supprimé");
            alerteSucces.showAndWait();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}



