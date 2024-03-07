package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Categorie;
import models.Produits;
import services.ServiceProduit;
import services.ServicesCategorie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierProduitController implements Initializable {

    @FXML
    private Button btnAjouter1;

    @FXML
    private ComboBox<String> categorieList;

    @FXML
    private Label file_path;

    @FXML
    private TextArea fx_description;

    @FXML
    private TextField fx_nom;

    @FXML
    private TextField fx_prix;

    @FXML
    private TextField fx_stock;

    @FXML
    private ImageView imagev;

    @FXML
    private AnchorPane nh;

    private int id; // Nouvelle variable pour stocker l'ID

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialiser la liste des catégories
        initCategories();
    }

    private void initCategories() {
        ObservableList<String> list = FXCollections.observableArrayList();
        ServicesCategorie sc = new ServicesCategorie();
        ObservableList<Categorie> obList = sc.afficherCategory2();

        categorieList.getItems().clear();

        for (Categorie nameCat : obList) {
            list.add(nameCat.getNom());
        }

        categorieList.setItems(list);
    }

    // Méthode pour initialiser l'ID
    public void initData(int id, String nom, String description, String image, int prix,String stock, int idCategory) {
        this.id = id;
        fx_nom.setText(nom);
        fx_description.setText(description);
        file_path.setText(image);
        fx_prix.setText(String.valueOf(prix));
        fx_stock.setText(stock);
        categorieList.setValue(String.valueOf(idCategory));
    }

    @FXML
    void uploadimageHandler(MouseEvent event) {
        FileChooser open = new FileChooser();
        Stage stage = (Stage) nh.getScene().getWindow();
        File file = open.showOpenDialog(stage);
        if (file != null) {
            String path = file.getAbsolutePath(); // Utilisez getAbsolutePath() au lieu de getName()
            file_path.setText(path);
            try {
                Image image = new Image(file.toURI().toString(), 500, 500, false, true);
                imagev.setImage(image);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        } else {
            System.out.println("NO DATA EXIST!");
        }
    }

    @FXML
    void Modifier(ActionEvent event) {
        ServiceProduit inter = new ServiceProduit();
        String nom = fx_nom.getText();
        String description = fx_description.getText();
        String image = file_path.getText();
        String prix = fx_prix.getText();
        String stock = fx_stock.getText();

        if (nom.isEmpty()) {
            showErrorAlert("Erreur : veuillez donner un nom de cours.");
            return;
        }
        if (description.isEmpty()) {
            showErrorAlert("Erreur : Veuillez entrer une description de cours.");
            return;
        }
        if (stock.isEmpty()) {
            showErrorAlert("Erreur : Veuillez entrer un nom de stock.");
            return;
        }
        try {
            int prixValue = Integer.parseInt(prix);
            if (prixValue < 0) {
                showErrorAlert("Erreur : Le prix du cours doit être positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur : Veuillez entrer un nombre valide pour le prix.");
            return;
        }

        // Trouver l'ID de la catégorie sélectionnée
        ServicesCategorie sc = new ServicesCategorie();
        ObservableList<Categorie> obList = sc.afficherCategory2();
        int selectedCategoryId = -1;
        for (Categorie cat : obList) {
            if (cat.getNom().equals(categorieList.getValue())) {
                selectedCategoryId = cat.getIdcat();
                break;
            }
        }

        if (selectedCategoryId == -1) {
            showErrorAlert("Erreur : Catégorie non trouvée.");
            return;
        }

        Produits prod = new Produits(id, nom, description, image, Integer.parseInt(prix), stock, selectedCategoryId);
        inter.modifierProduit(prod);

        showConfirmationAlert("Cours modifiée avec succès !");
    }


    @FXML
    private void ReturnToAfficher(ActionEvent event) {
        try {
            FXMLLoader loader = createFXMLLoader("/AfficherProduit.fxml");
            Parent root = loader.load();
            nh.getChildren().setAll(root);
        } catch (IOException ex) {
            System.out.println("Erreur lors du chargement de la vue : " + ex.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private FXMLLoader createFXMLLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader;
    }
}
