package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Role;
import models.Utilisateur;
import org.w3c.dom.Node;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficherUtilisateurController {
    private final UtilisateurService us = new UtilisateurService();

    @FXML
    private DatePicker Birth;

    @FXML
    private TextField Email;

    @FXML
    private TextField Fname;

    @FXML
    private TextField Lname;

    @FXML
    private TextField Pwd;

    @FXML
    private ChoiceBox<Role> RoleP;

    @FXML
    private TableColumn<Utilisateur, LocalDate> birthcol;

    @FXML
    private TableColumn<Utilisateur, String> emailcol;

    @FXML
    private TableColumn<Utilisateur, String> fnamecol;

    @FXML
    private TableColumn<Utilisateur, String> idcol;

    @FXML
    private TableColumn<Utilisateur, String> lnamecol;

    @FXML
    private TableColumn<Utilisateur, String> pwdcol;

    @FXML
    private TableColumn<Utilisateur, Role> rolecol;

    @FXML
    private TableView<Utilisateur> tableview;

    @FXML
    private TextField Search;


    @FXML
    void initialize () {
        try {
            // Fetch the list of utilisateurs
            List<Utilisateur> utilisateurs = us.afficher();

            // Group utilisateurs by role
            Map<Role, List<Utilisateur>> utilisateursByRole = utilisateurs.stream()
                    .collect(Collectors.groupingBy(Utilisateur::getRole));

            // Combine all the lists into one
            List<Utilisateur> sortedUtilisateurs = utilisateursByRole.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            ObservableList<Utilisateur> observableList = FXCollections.observableList(sortedUtilisateurs);
            tableview.setItems(observableList);

            // Set cell value factories for each column
            idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            fnamecol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            lnamecol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
            pwdcol.setCellValueFactory(new PropertyValueFactory<>("mdp"));
            birthcol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
            rolecol.setCellValueFactory(new PropertyValueFactory<>("role"));

            // Configure the role column to display the role as a string
            rolecol.setCellFactory(column -> {
                return new TableCell<Utilisateur, Role>() {
                    @Override
                    protected void updateItem(Role item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.toString());
                        }
                    }
                };
            });
        } catch (SQLException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
        }
    }
    @FXML
    void getData(MouseEvent event) {
        Utilisateur utilisateur = tableview.getSelectionModel().getSelectedItem();
        if (utilisateur != null) {
            Fname.setText(utilisateur.getPrenom());
            Lname.setText(utilisateur.getNom());
            Email.setText(utilisateur.getEmail());
            Pwd.setText(utilisateur.getMdp());
            Birth.setValue(utilisateur.getDateNaissance());
            RoleP.setValue(utilisateur.getRole());
        }
    }

    public void deleteUser(javafx.event.ActionEvent actionEvent) {
        Utilisateur utilisateur = tableview.getSelectionModel().getSelectedItem();

        if (utilisateur != null) {
            try {
                us.supprimer(utilisateur.getId());
                initialize();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Veuillez sélectionner un utilisateur à supprimer.");
            alert.showAndWait();
        }
    }

    public void updateUser(ActionEvent actionEvent) {
        Utilisateur selectedUtilisateur = tableview.getSelectionModel().getSelectedItem();

        if (selectedUtilisateur != null) {
            selectedUtilisateur.setPrenom(Fname.getText());
            selectedUtilisateur.setNom(Lname.getText());
            selectedUtilisateur.setEmail(Email.getText());
            selectedUtilisateur.setMdp(Pwd.getText());
            selectedUtilisateur.setDateNaissance(Birth.getValue());
            selectedUtilisateur.setRole(RoleP.getValue());

            try {
                us.modifier(selectedUtilisateur);
                initialize();
                clearFields();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Veuillez sélectionner un utilisateur à mettre à jour.");
            alert.showAndWait();
        }
    }
    private void clearFields() {
        Fname.clear();
        Lname.clear();
        Email.clear();
        Pwd.clear();
        Birth.setValue(null);
        RoleP.getSelectionModel().clearSelection();
    }

    public void clearAll(ActionEvent actionEvent) {
        try {
            us.clearUserData();
            initialize();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Toutes les données ont été supprimées avec succès.");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void getback(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterUtilisateur.fxml"));
            Fname.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
    }

    public void searchUser(KeyEvent keyEvent) {
        String searchText = Search.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            // If search text is empty, display all users
            initialize();
            return;
        }

        ObservableList<Utilisateur> filteredList = FXCollections.observableArrayList();

        for (Utilisateur utilisateur : tableview.getItems()) {
            if (Integer.toString(utilisateur.getId()).contains(searchText)
                    || utilisateur.getPrenom().toLowerCase().contains(searchText)
                    || utilisateur.getNom().toLowerCase().contains(searchText)) {
                filteredList.add(utilisateur);
            }
        }

        tableview.setItems(filteredList);
    }
}



