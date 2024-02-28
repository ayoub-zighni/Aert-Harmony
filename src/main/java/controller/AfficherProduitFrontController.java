package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import models.produits;
import services.ServiceProduit;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherProduitFrontController  implements Initializable {



    ServiceProduit sa = new ServiceProduit();

    @FXML
    private VBox vbox1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<produits> coursList = sa.afficherProduit();

        vbox1.setSpacing(10);
        vbox1.setAlignment(Pos.CENTER);

        int coursCount = coursList.size();
        int coursesPerRow = 3;

        for (int i = 0; i < coursCount; i += coursesPerRow) {
            HBox row = createRow();

            for (int j = i; j < Math.min(i + coursesPerRow, coursCount); j++) {
                produits p = coursList.get(j);
                BorderPane coursePane = createCoursePane(p);
                row.getChildren().add(coursePane);
            }

            vbox1.getChildren().add(row);
        }
    }

    private HBox createRow() {
        HBox row = new HBox();
        row.setSpacing(10);
        row.setAlignment(Pos.CENTER);
        return row;
    }

    private BorderPane createCoursePane(produits cours) {
        BorderPane coursePane = new BorderPane();
        coursePane.getStyleClass().add("course-pane");
        coursePane.setPrefSize(200, 300);
        coursePane.setPadding(new Insets(10));
        coursePane.setEffect(new DropShadow(5.0, Color.gray(0.5)));

        Label courseNameLabel = new Label(cours.getNom());
        courseNameLabel.getStyleClass().add("course-name-label");
        courseNameLabel.setAlignment(Pos.CENTER); // Centrer le nom du produit

        // Utilisez votre service pour récupérer le nom de la catégorie
        String categoryName = sa.getNomCategorieById(cours.getIdcategorie());
        Label categoryLabel = new Label("Catégorie: " + categoryName);

        // Ajoutez un label pour afficher le stock
        Label stockLabel = new Label("Stock: " + cours.getStock());

        ImageView image = createArticleImage(cours);
        Label price = createArticlePrice(cours);

        VBox imageAndPriceContainer = new VBox(image, price);
        imageAndPriceContainer.setAlignment(Pos.CENTER);

        coursePane.setTop(new VBox(courseNameLabel, categoryLabel));
        coursePane.setAlignment(courseNameLabel, Pos.CENTER);
        coursePane.setCenter(imageAndPriceContainer);

        // Ajoutez le label du stock au bas du BorderPane
        coursePane.setBottom(stockLabel);
        coursePane.setAlignment(stockLabel, Pos.BOTTOM_RIGHT);

        // Ajoutez l'image du panier avec le tooltip
        ImageView reservationImageView = createCartImageView(cours);
        coursePane.setRight(reservationImageView);
        coursePane.setAlignment(reservationImageView, Pos.BOTTOM_RIGHT);

        VBox.setMargin(coursePane, new Insets(0, 0, 10, 0));

        return coursePane;
    }

    private ImageView createArticleImage(produits cours) {
        ImageView image = new ImageView();
        image.setFitHeight(150.0);
        image.setFitWidth(200.0);
        image.setPreserveRatio(true);

        try {
            File uploadedFile = new File(cours.getImage());
            String fileUrl = uploadedFile.toURI().toString();
            Image imageSource = new Image(fileUrl);
            image.setImage(imageSource);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'image pour : " + cours.getImage());
        }

        return image;
    }

    private Label createArticlePrice(produits cours) {
        Label price = new Label();
        price.setText("PRIX : " + (float) cours.getPrix() + " DT");
        price.getStyleClass().add("price-label");
        return price;
    }

    private ImageView createCartImageView(produits cours) {
        ImageView cartImageView = new ImageView();
        cartImageView.setFitHeight(30.0);
        cartImageView.setFitWidth(30.0);
        cartImageView.setPreserveRatio(true);

        Image cartImage = new Image(getClass().getResourceAsStream("/reservations.png"));
        cartImageView.setImage(cartImage);

        Tooltip tooltip = new Tooltip("Réserver le produit");
        Tooltip.install(cartImageView, tooltip);

        cartImageView.setOnMouseClicked(event -> handleCartClick(cours));

        return cartImageView;
    }

    private void handleCartClick(produits cours) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cours réservé");
        alert.setHeaderText(null);
        alert.setContentText("Vous avez ajouter le produit au panier : " + cours.getNom());
        alert.showAndWait();
    }
}

