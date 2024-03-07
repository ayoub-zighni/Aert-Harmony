package controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Produits;
import test.Main;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PanierController {
    @FXML
    private TextField nomProduitField;

    @FXML
    private TextField prixField;

    @FXML
    private ChoiceBox etatStockChoiceBox;

    @FXML
    private TextField quantiteField;
    Produits produitSelectionne = Main.produitsel;
    public void initialize() {

        nomProduitField.setText(produitSelectionne.getNom());
        prixField.setText("" + produitSelectionne.getPrix());


    }

    // Method to handle the "Ajouter" button action

    @FXML
    private void ajouterPanier() {
        String nomProduit = nomProduitField.getText();
        double prix = Double.parseDouble(prixField.getText());
        String etatStock = (String) etatStockChoiceBox.getValue();
        int quantite = Integer.parseInt(quantiteField.getText());

        // Insert the data into your database using the DataSource class
        try {
            // Get database connection from DataSource
            Connection connection = MyDatabase.getInstance().getConnection();

            // Prepare the SQL statement
            String sql = "INSERT INTO panier (Nom_produit, prix, etat_stock, quantite) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nomProduit);
            statement.setDouble(2, prix);
            statement.setString(3, etatStock);
            statement.setInt(4, quantite);

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new item has been added to the panier.");
            }

            // Close the statement (optional)
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error inserting data into the database: " + e.getMessage());
        }

    }

    public void voirFacture() {
        try {
            // Load the facture.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/facture.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Facture");
            stage.setScene(new Scene(root));

            // Show the stage
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading facture.fxml: " + e.getMessage());
        }


    }
    @FXML
    private void voirPanier() {
        try {
            // Load the FXML file for the "voir panier" interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPanier.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Voir Panier");

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }



}
