package controllers;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Produits;
import models.Utilisateur;
import test.Main;
import utils.MyDatabase;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;


public class FactureController {

    @FXML
    private TextField clientField;

    @FXML
    private TextField caissierField;

    @FXML
    private Label montantLabel;

    @FXML
    private TextField percuField;

    @FXML
    private TextField renduField;
    @FXML
    private ImageView promotion;
    private String promo;
    private double montantinteret;

    @FXML
    private TextField montant9;
    @FXML
    private TextField montant12;
    @FXML
    private TextField montant24;
    Utilisateur utilisateurconnect = Main.userconn;
    public void initialize() {


        clientField.setText(utilisateurconnect.getNom());



    }
    @FXML
    private void ajouterFacture() {

        String caissier = caissierField.getText();
        double percu = Double.parseDouble(percuField.getText());
        double montant = 0;

        // Calculate the montant
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            String sql = "SELECT SUM(quantite * prix) AS total FROM panier";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                montant = resultSet.getDouble("total");
                montantinteret=montant;
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error calculating montant: " + e.getMessage());
        }

        // Set the montant in the label
        montantLabel.setText(Double.toString(montant));

        // Check if percu is greater than or equal to montant
        if (percu < montant) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("----");
            alert.setHeaderText(null);
            alert.setContentText("percu should be greater than or equal to montant !");
            alert.showAndWait();
            return;
        }

        // Calculate rendu
        double rendu = percu - montant;

        try {
            Connection connection = MyDatabase.getInstance().getConnection();

            // Prepare the SQL statement
            String insertSql = "INSERT INTO facture (client, caissier, montant, percu, rendu,id_userfact) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, utilisateurconnect.getNom());
            insertStatement.setString(2, caissier);
            insertStatement.setDouble(3, montant);
            insertStatement.setDouble(4, percu);
            insertStatement.setDouble(5, rendu);
            insertStatement.setDouble(6, utilisateurconnect.getId());

            // Execute the insert statement
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new facture has been added.");


                try {
                    QRCodeWriter qrCodeWriter = new QRCodeWriter();
                    String Information = "code promo  : : " + "promo24";
                    int width = 300;
                    int height = 300;

                    BufferedImage bufferedImage = null;
                    BitMatrix byteMatrix = qrCodeWriter.encode(Information, BarcodeFormat.QR_CODE, width, height);
                    bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    bufferedImage.createGraphics();

                    Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(0, 0, width, height);
                    graphics.setColor(Color.BLACK);

                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            if (byteMatrix.get(i, j)) {
                                graphics.fillRect(i, j, 1, 1);
                            }
                        }
                    }

                    promotion.setImage(SwingFXUtils.toFXImage(bufferedImage, null));


                } catch (WriterException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            // Close the statement
            insertStatement.close();
        } catch (SQLException e) {
            System.err.println("Error inserting data into the database: " + e.getMessage());
        }
    }

    @FXML
    private void voirFacture() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FactureView.fxml"));
            Parent root = loader.load();

            // Create the scene
            Scene scene = new Scene(root);

            // Get the stage from the event source
            Stage stage = (Stage) (clientField.getScene().getWindow());

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void neufmois() {
        double montantInteret = montantinteret;

        // Add 10% to montantInteret
        double updatedMontant = montantInteret * 1.10;

        // Divide the updated value by 9
        double montant9Mois = Math.round(updatedMontant / 9);
        ;
        montant9.setText(String.valueOf(montant9Mois));
    }
    @FXML
    private void douzemoins() {
        double montantInteret = montantinteret;

        // Add 10% to montantInteret
        double updatedMontant = montantInteret * 1.15;

        // Divide the updated value by 9
        double montant9Mois = Math.round(updatedMontant / 12);
        ;
        montant12.setText(String.valueOf(montant9Mois));
    }
    @FXML
    private void quatremois() {
        double montantInteret = montantinteret;

        // Add 10% to montantInteret
        double updatedMontant = montantInteret * 1.2;

        // Divide the updated value by 9
        double montant9Mois = Math.round(updatedMontant / 24);
        ;
        montant24.setText(String.valueOf(montant9Mois));
    }


    public void AddCommande(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterCommande.fxml"));
            montant9.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }
    }
}
