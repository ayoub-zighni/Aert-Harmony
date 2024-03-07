package controllers;


import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Panier;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AfficherPanierController {

    @FXML
    private TableView<Panier> tableView;

    @FXML
    private TableColumn<Panier, String> nomProduitColumn;

    @FXML
    private TableColumn<Panier, String> idPanierColumn;

    @FXML
    private TableColumn<Panier, Double> prixColumn;

    @FXML
    private TableColumn<Panier, String> etatStockColumn;

    @FXML
    private TableColumn<Panier, Integer> quantiteColumn;

    Connection connection = MyDatabase.getInstance().getConnection();

    @FXML
    private void initialize() {
        // Initialize table columns
        idPanierColumn.setCellValueFactory(new PropertyValueFactory<>("id_panier")); // Bind the id column to the id property of Panier
        idPanierColumn.setVisible(false); // Hide the id column
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        etatStockColumn.setCellValueFactory(new PropertyValueFactory<>("etatStock"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Open database connection


        // Load data into the table
        afficherPanier();
    }

    private void afficherPanier() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM panier");
            ResultSet rs = statement.executeQuery();

            // Clear existing items in the table
            tableView.getItems().clear();

            // Populate the table with data from the database
            while (rs.next()) {
                Panier panier = new Panier(
                        rs.getInt("id_panier"),
                        rs.getString("Nom_produit"),
                        rs.getDouble("prix"),
                        rs.getString("etat_stock"),
                        rs.getInt("quantite")
                );
                tableView.getItems().add(panier);
            }

            // Close the resources
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching data from the database: " + e.getMessage());
        }
    }

    @FXML
    private void supprimerPanier() {
        // Get the selected item from the table
        Panier selectedPanier = tableView.getSelectionModel().getSelectedItem();
        if (selectedPanier == null) {
            // No item selected, display an error message or handle it as needed
            System.out.println("No item selected for deletion.");
            return;
        }

        // Delete the selected item from the database
        try {
            int selectedId = selectedPanier.getId(); // Get the id of the selected item
            System.out.println("Deleting item with id: " + selectedId); // Print the id for debugging
            String sql = "DELETE FROM panier WHERE id_panier = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, selectedId); // Set the id parameter in the prepared statement
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                // Item deleted successfully, update the table view
                tableView.getItems().remove(selectedPanier);
                System.out.println("Item deleted successfully.");
            } else {
                // No rows affected, display an error message or handle it as needed
                System.out.println("No rows affected by deletion.");
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error deleting item from panier: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public void onClose() {
        // Close the database connection when the application exits
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    @FXML
    private void incrementQuantity() {
        Panier selectedPanier = tableView.getSelectionModel().getSelectedItem();
        if (selectedPanier != null) {
            int newQuantity = selectedPanier.getQuantite() + 1;
            updateQuantity(selectedPanier, newQuantity);
        }
    }

    @FXML
    private void decrementQuantity() {
        Panier selectedPanier = tableView.getSelectionModel().getSelectedItem();
        if (selectedPanier != null) {
            int newQuantity = selectedPanier.getQuantite() - 1;
            if (newQuantity >= 0) {
                updateQuantity(selectedPanier, newQuantity);
            } else {
                removeItem(selectedPanier);
            }
        }
    }

    private void updateQuantity(Panier panier, int newQuantity) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE panier SET quantite = ? WHERE id_panier = ?");
            statement.setInt(1, newQuantity);
            statement.setInt(2, panier.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                panier.setQuantite(newQuantity);
                tableView.refresh();
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating quantity: " + e.getMessage());
        }
    }

    private void removeItem(Panier panier) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM panier WHERE id_panier = ?");
            statement.setInt(1, panier.getId());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                tableView.getItems().remove(panier);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error removing item: " + e.getMessage());
        }
    }
}
