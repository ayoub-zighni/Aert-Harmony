package controller;

import io.jsonwebtoken.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.produits;
import services.ServiceProduit;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
 import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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

        VBox imageContainer = new VBox();

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
        ImageView productImageView = createProductImageView(cours);
        // Add all ImageViews to the VBox
        imageContainer.getChildren().addAll(imageAndPriceContainer, reservationImageView, productImageView);

// Set the VBox as the center of the coursePane
        coursePane.setCenter(imageContainer);

// Add margin to the entire coursePane
        VBox.setMargin(coursePane, new Insets(0, 0, 10, 0));

        // Set a click event handler to show the bar chart when the product is clicked
        coursePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Show the bar chart (implement the logic to display the bar chart here)
                showBarChartForProduct(cours);
            }
        });
        return coursePane;
    }
    private ImageView createProductImageView(produits cours) {
        // Replace "path/to/your/image.png" with the actual path to your product image
        Image image =new Image(getClass().getResourceAsStream("/codeabar.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30); // Set the width of the image as needed
        imageView.setFitHeight(30); // Set the height of the image as needed
        return imageView;
    }

    private void showBarChartForProduct(produits cours) {
        // Create a popup stage
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("Product Barcode");

        // Generate barcode image
        Image barcodeImage = generateBarcodeImage(cours.getNom());

        // Create a VBox to hold the content
        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(10));

        // Display product details
        Label productNameLabel = new Label("Product Name: " + cours.getNom());

        // Display barcode image
        ImageView imageView = new ImageView(barcodeImage);
        imageView.setFitWidth(300);
        imageView.setFitHeight(150);

        popupContent.getChildren().addAll(productNameLabel, imageView);

        // Set the scene for the popup
        Scene popupScene = new Scene(popupContent, 400, 250);
        popupStage.setScene(popupScene);

        // Show the popup
        popupStage.showAndWait();
    }

    private Image generateBarcodeImage(String barcodeData) {
        // Use ZXing library to generate barcode image

        if (StringUtils.isNotEmpty(barcodeData)) {
            try {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeData, BarcodeFormat.CODE_128, 300, 150);

                // Convert the matrix to an image
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                // Save the image to a file (you can customize the file path and name)
                String filePath = "src/main/resources/barcode.png";

                saveImageToFile(imageBytes, filePath);

                // Create an ImageView with the barcode image
                Image barcodeImage = new Image(new ByteArrayInputStream(imageBytes));
                ImageView barcodeImageView = new ImageView(barcodeImage);

                return barcodeImageView.getImage();
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Return a default image if barcode generation fails
        return new Image((getClass().getResourceAsStream("bar.png"))); // Provide a path to a default image
    }

    private void saveImageToFile(byte[] imageBytes, String filePath) throws IOException, FileNotFoundException {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(imageBytes);
        } catch (java.io.IOException e) {
            // Handle or log the exception accordingly
            e.printStackTrace();
            throw new FileNotFoundException("File not found: " + filePath);
        }
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

