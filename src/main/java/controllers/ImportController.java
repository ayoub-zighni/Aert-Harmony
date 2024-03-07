package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Evenement;
import services.EvenementService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportController {

    @FXML
    private Button importButton;

    @FXML
    private Button QRCodeButton;


    @FXML
    void importer(ActionEvent event) {
        EvenementService evenementService = new EvenementService();
        List<Evenement> evenements;

        try {
            evenements = evenementService.recuperer();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException appropriately, maybe show an error message
            return;
        }

        updateDialogContent(evenements);
    }


    private void updateDialogContent(List<Evenement> evenements) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        gridPane.add(new Label("ID Galerie"), 0, 0);
        gridPane.add(new Label("ID"), 1, 0);
        gridPane.add(new Label("Date"), 2, 0);
        gridPane.add(new Label("Heure"), 3, 0);
        gridPane.add(new Label("Description"), 4, 0);

        int row = 1;
        for (Evenement evenement : evenements) {
            gridPane.add(new Label(String.valueOf(evenement.getGalerieID())), 0, row);
            gridPane.add(new Label(String.valueOf(evenement.getId())), 1, row);
            gridPane.add(new Label(evenement.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), 2, row);
            gridPane.add(new Label(evenement.getHeure().format(DateTimeFormatter.ofPattern("HH:mm"))), 3, row);
            Label descriptionLabel = new Label(evenement.getDescription());
            descriptionLabel.getStyleClass().add("data-value");
            gridPane.add(descriptionLabel, 4, row);
            row++;
        }

        VBox vbox = new VBox();
        vbox.getChildren().addAll(gridPane);

        alert.getDialogPane().setContent(vbox);
        alert.showAndWait();
    }


    @FXML
    private Button genererQRCodeButton;

    @FXML
    void genererQRCode(ActionEvent event) {
        String content = "\"Hello, world!\" "; // Replace this with your content
        int width = 300;
        int height = 300;
        String format = "png";

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();
            Image qrCodeImage = new Image(new ByteArrayInputStream(qrCodeBytes));

            // Display the generated QR code image
            // For example, you can use an ImageView to display the image
            ImageView qrCodeImageView = new ImageView();
            qrCodeImageView.setImage(qrCodeImage);

            // Add the ImageView to your layout or show it in a dialog, etc.
            // For example, you can add it to a Pane or a StackPane
            // pane.getChildren().add(qrCodeImageView);

            // If you want to save the QR code to a file, you can do it like this:
            // Files.write(Paths.get("qrcode.png"), qrCodeBytes);

            System.out.println("QR Code generated successfully.");
        } catch (WriterException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("An error occurred while generating the QR code: " + e.getMessage());
            alert.showAndWait();
        }
    }
}