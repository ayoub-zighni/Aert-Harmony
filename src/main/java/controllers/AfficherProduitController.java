package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import models.Produits;
import services.ServiceProduit;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AfficherProduitController implements Initializable {

    @FXML
    private AnchorPane nh;

    @FXML
    private VBox vbox1;
    @FXML
    private TextField filtre;
    private Set<Pane> uniqueProductPanes = new HashSet<>();

    private final ServiceProduit scom = new ServiceProduit();
    List<Produits> produitsList =new ArrayList<>();
    private List<Pane> productPanes = new ArrayList<>();

    @FXML
    void ReturnToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            nh.getChildren().setAll(root);
        } catch (IOException ex) {
            System.out.println("Erreur lors du chargement de la vue : " + ex.getMessage());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbox1.setFillWidth(true);
        vbox1.setSpacing(5);
        produitsList = scom.afficherProduit();

// Clear existing content in vbox1
        vbox1.getChildren().clear();

        for (Produits produit : produitsList) {
            Pane pane = createProductPane(produit);

            // Check if vbox1 is empty before adding panes
            if (vbox1.getChildren().isEmpty()) {
                vbox1.getChildren().add(pane);
            } else {
                // Optionally, you can check if the pane is not already present to avoid duplicates
                if (!vbox1.getChildren().contains(pane)) {
                    vbox1.getChildren().add(pane);
                }
            }
        }

        vbox1.setSpacing(5);
    }

    @FXML
    public void handleSearch(KeyEvent event) {
         String searchText = filtre.getText().trim().toLowerCase();

        // Clear the existing content in vbox1
        vbox1.getChildren().clear();

        // If the search text is empty, display all items
        if (searchText.isEmpty()) {
            for (Produits product : produitsList) {
                Pane pane = createProductPane(product);

                // Check if the child is already present before adding
                if (!vbox1.getChildren().contains(pane)) {
                    vbox1.getChildren().add(pane);
                }
            }
        } else {
            // Filter the products based on the search text
            List<Produits> filteredList = produitsList.stream()
                    .filter(product -> product.getNom().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());

            // Display the filtered products
            for (Produits product : filteredList) {
                Pane pane = createProductPane(product);

                // Check if the child is already present before adding
                if (!vbox1.getChildren().contains(pane)) {
                    vbox1.getChildren().add(pane);
                }
            }
        }
    }

//        for (produits produit : produitsList) {
//            Pane pane = createProductPane(produit);
//            if (!vbox1.getChildren().contains(pane)) {
//                vbox1.getChildren().add(pane);
//            }
//        }
 //   }

//        for (produits produit : produitsList) {
//            Pane pane = new Pane();
//            pane.setPrefHeight(62.0);
//            pane.setMinHeight(62.0);
//            pane.setPrefWidth(840.0);
//
//            Label label1 = new Label("ID: " + produit.getId());
//            label1.setLayoutX(15.0);
//            label1.setLayoutY(18.0);
//            label1.setFont(new Font(14.0));
//
//            Label label2 = new Label(produit.getNom());
//            label2.setLayoutX(100.0);
//            label2.setLayoutY(18.0);
//            label2.setPrefHeight(25.0);
//            label2.setPrefWidth(200.0);
//            label2.setFont(new Font(16.0));
//
//            Label label3 = new Label(produit.getDescription());
//            label3.setLayoutX(180.0);
//            label3.setLayoutY(18.0);
//            label3.setPrefHeight(25.0);
//            label3.setPrefWidth(200.0);
//            label3.setFont(new Font(16.0));
//
//            Label label6 = new Label(String.valueOf(produit.getPrix()));
//            label6.setLayoutX(280.0);
//            label6.setLayoutY(18.0);
//            label6.setPrefHeight(25.0);
//            label6.setPrefWidth(200.0);
//            label6.setFont(new Font(16.0));
//
//            Label label7 = new Label(produit.getStock());
//            label7.setLayoutX(350.0);
//            label7.setLayoutY(18.0);
//            label7.setPrefHeight(25.0);
//            label7.setPrefWidth(200.0);
//            label7.setFont(new Font(16.0));
//
//            Label label8 = new Label(String.valueOf(produit.getIdcategorie()));
//            label8.setLayoutX(450.0);
//            label8.setLayoutY(18.0);
//            label8.setPrefHeight(25.0);
//            label8.setPrefWidth(200.0);
//            label8.setFont(new Font(16.0));
//
//            ImageView imageView1 = new ImageView();
//            Image image1 = new Image(getClass().getResourceAsStream("/trash.png"));
//            imageView1.setImage(image1);
//            imageView1.setFitHeight(20.0);
//            imageView1.setFitWidth(20.0);
//            imageView1.setLayoutX(550.0);
//            imageView1.setLayoutY(20.0);
//            imageView1.setPickOnBounds(true);
//            imageView1.setPreserveRatio(true);
//            imageView1.setOnMouseClicked((MouseEvent event) -> {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setTitle("Confirmer la suppression");
//                alert.setHeaderText("Êtes-vous sûr de supprimer ce produit ?");
//                Optional<ButtonType> result = alert.showAndWait();
//
//                if (result.isPresent() && result.get() == ButtonType.OK) {
//                    Pane parent = (Pane) pane.getParent();
//                    scom.supprimerProduit(produit.getId());
//                    parent.getChildren().remove(pane);
//                }
//            });
//
//            ImageView imageView2 = new ImageView();
//            Image image2 = new Image(getClass().getResourceAsStream("/Edit.png"));
//            imageView2.setImage(image2);
//            imageView2.setFitHeight(20.0);
//            imageView2.setFitWidth(20.0);
//            imageView2.setLayoutX(580.0);
//            imageView2.setLayoutY(20.0);
//            imageView2.setPickOnBounds(true);
//            imageView2.setPreserveRatio(true);
//            imageView2.setOnMouseClicked((MouseEvent event) -> {
//                try {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduit.fxml"));
//                    Parent root = loader.load();
//                    ModifierProduitController modifierController = loader.getController();
//                    modifierController.initData(produit.getId(), produit.getNom(), produit.getDescription(), produit.getImage(), produit.getPrix(), produit.getStock(), produit.getIdcategorie());
//                    vbox1.getScene().setRoot(root);
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            });
//
//            Line line = new Line();
//            line.setStrokeWidth(0.4);
//            line.setStartX(-411.0);
//            line.setStartY(9.400012969970703);
//            line.setEndX(429.0);
//            line.setEndY(9.400012969970703);
//            line.setLayoutX(250.0);
//            line.setLayoutY(53.0);
//
//            pane.getChildren().addAll(label1, label2, label3, label6, label7, label8, imageView1, imageView2, line);
//            vbox1.getChildren().add(pane);
//        }
//        vbox1.setSpacing(5);
   // }

    private Pane createProductPane(Produits produit) {

        Pane pane = new Pane();
        pane.setPrefHeight(62.0);
        pane.setMinHeight(62.0);
        pane.setPrefWidth(840.0);

        Label label1 = new Label("ID: " + produit.getId());
        label1.setLayoutX(15.0);
        label1.setLayoutY(18.0);
        label1.setFont(new Font(14.0));

        Label label2 = new Label(produit.getNom());
        label2.setLayoutX(100.0);
        label2.setLayoutY(18.0);
        label2.setPrefHeight(25.0);
        label2.setPrefWidth(200.0);
        label2.setFont(new Font(16.0));

        Label label3 = new Label(produit.getDescription());
        label3.setLayoutX(180.0);
        label3.setLayoutY(18.0);
        label3.setPrefHeight(25.0);
        label3.setPrefWidth(200.0);
        label3.setFont(new Font(16.0));

        Label label6 = new Label(String.valueOf(produit.getPrix()));
        label6.setLayoutX(280.0);
        label6.setLayoutY(18.0);
        label6.setPrefHeight(25.0);
        label6.setPrefWidth(200.0);
        label6.setFont(new Font(16.0));

        Label label7 = new Label(produit.getStock());
        label7.setLayoutX(350.0);
        label7.setLayoutY(18.0);
        label7.setPrefHeight(25.0);
        label7.setPrefWidth(200.0);
        label7.setFont(new Font(16.0));

        Label label8 = new Label(String.valueOf(produit.getIdcategorie()));
        label8.setLayoutX(450.0);
        label8.setLayoutY(18.0);
        label8.setPrefHeight(25.0);
        label8.setPrefWidth(200.0);
        label8.setFont(new Font(16.0));

        ImageView imageView1 = new ImageView();
        Image image1 = new Image(getClass().getResourceAsStream("/trash.png"));
        imageView1.setImage(image1);
        imageView1.setFitHeight(20.0);
        imageView1.setFitWidth(20.0);
        imageView1.setLayoutX(550.0);
        imageView1.setLayoutY(20.0);
        imageView1.setPickOnBounds(true);
        imageView1.setPreserveRatio(true);
        imageView1.setOnMouseClicked((MouseEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppression");
            alert.setHeaderText("Êtes-vous sûr de supprimer ce produit ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Pane parent = (Pane) pane.getParent();
                scom.supprimerProduit(produit.getId());
                parent.getChildren().remove(pane);
            }
        });

        ImageView imageView2 = new ImageView();
        Image image2 = new Image(getClass().getResourceAsStream("/Edit.png"));
        imageView2.setImage(image2);
        imageView2.setFitHeight(20.0);
        imageView2.setFitWidth(20.0);
        imageView2.setLayoutX(580.0);
        imageView2.setLayoutY(20.0);
        imageView2.setPickOnBounds(true);
        imageView2.setPreserveRatio(true);
        imageView2.setOnMouseClicked((MouseEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduit.fxml"));
                Parent root = loader.load();
                ModifierProduitController modifierController = loader.getController();
                modifierController.initData(produit.getId(), produit.getNom(), produit.getDescription(), produit.getImage(), produit.getPrix(), produit.getStock(), produit.getIdcategorie());
                vbox1.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Line line = new Line();
        line.setStrokeWidth(0.4);
        line.setStartX(-411.0);
        line.setStartY(9.400012969970703);
        line.setEndX(429.0);
        line.setEndY(9.400012969970703);
        line.setLayoutX(250.0);
        line.setLayoutY(53.0);

        pane.getChildren().addAll(label1, label2, label3, label6, label7, label8, imageView1, imageView2, line);
        vbox1.getChildren().add(pane);
        return pane;
    }



    @FXML
    void returnToAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent root = loader.load();
            nh.getChildren().setAll(root);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
