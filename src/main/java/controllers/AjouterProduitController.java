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

public class AjouterProduitController  implements Initializable {

    @FXML
    private Button btnAjouter;

    @FXML
    private ComboBox<String> categorieList;

    @FXML
    private Label file_path;

    @FXML
    private TextField fx_stock;
    @FXML
    private TextArea fx_description;

    @FXML
    private TextField fx_nom;

    @FXML
    private TextField fx_prix;

    @FXML
    private ImageView imagev;

    @FXML
    private AnchorPane nh;

    int MAX_FILE_PATH_LENGTH = 255;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list = FXCollections.observableArrayList();
        ServicesCategorie sc = new ServicesCategorie();
        ObservableList<Categorie> obList = FXCollections.observableArrayList();
        obList = sc.afficherCategory2();

        categorieList.getItems().clear();

        for (Categorie nameCat : obList) {
            list.add(nameCat.getNom());
        }

        categorieList.setItems(list);
    }
    @FXML
    void ajouterProduit(ActionEvent event) {
        String nom = fx_nom.getText();
        String description = fx_description.getText();
        String stock = fx_stock.getText();
        String email = "ayoub.zighni@esprit.tn";

        int prix = 0;



        if (nom.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un nom de produit.");
            alert.show();
            return;
        }

        if (description.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer une description de produit.");
            alert.show();
            return;
        }



        try {
            prix = Integer.parseInt(fx_prix.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un nombre valide pour le prix.");
            alert.show();
            return;
        }
        if (prix < 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Le prix du produit doit être positif.");
            alert.show();
            return;
        }
        if (stock.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un nom de stock.");
            alert.show();
            return;
        }
        ServicesCategorie sc = new ServicesCategorie();
        int idCategory = sc.getIdCategorieByName(categorieList.getValue());

        if (idCategory == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Catégorie non trouvée.");
            alert.show();
            return;
        }
        String filePath = file_path.getText();
        if (filePath.length() > MAX_FILE_PATH_LENGTH) {
            filePath = filePath.substring(0, MAX_FILE_PATH_LENGTH);
        }

        ServiceProduit ss = new ServiceProduit();
        ss.ajouterProduit(new Produits(nom, description, filePath, prix, stock, idCategory));
        // If product added successfully, send an email
        //SendEmail.sendEmail(email, "New Product Added", "A new product has been added Check out the new product.");


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success Message");
        alert.setHeaderText(null);
        alert.setContentText("Produit ajouté avec succès !");
        alert.showAndWait();
    }

    @FXML
    void uploadimageHandler(MouseEvent event) {
        FileChooser open = new FileChooser();
        Stage stage = (Stage) nh.getScene().getWindow();
        File file = open.showOpenDialog(stage);
        if (file != null) {
            String path = file.getAbsolutePath(); // Utilisez getAbsolutePath() au lieu de getName()
            file_path.setText(path);
            Image image = new Image(file.toURI().toString(), 500, 500, false, true);
            imagev.setImage(image);
        } else {
            System.out.println("NO DATA EXIST!");
        }
    }

    private FXMLLoader createFXMLLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader;
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
}
