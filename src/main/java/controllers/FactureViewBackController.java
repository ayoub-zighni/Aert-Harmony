package controllers;


import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Facture;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import utils.MyDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FactureViewBackController {

    @FXML
    private TableView<Facture> factureTableView;

    @FXML
    private TableColumn<Facture, String> clientColumn;

    @FXML
    private TableColumn<Facture, String> caissierColumn;

    @FXML
    private TableColumn<Facture, Double>     idfacture;
    @FXML
    private TableColumn<Facture, Double> montantColumn;

    @FXML
    private TableColumn<Facture, Double> percuColumn;

    @FXML
    private TableColumn<Facture, Double> renduColumn;
    @FXML
    private TextField clientField;
    @FXML
    private TextField caissierField;
    @FXML
    private TextField montantField;
    @FXML
    private TextField percuField;
    @FXML
    private TextField renduField;
    @FXML
    private TextField codepromo;

    private Connection connection;

    @FXML
    private void initialize() {

        idfacture.setCellValueFactory(new PropertyValueFactory<>("id_fact"));
        idfacture.setVisible(false); // Hide the id column
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        caissierColumn.setCellValueFactory(new PropertyValueFactory<>("caissier"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        percuColumn.setCellValueFactory(new PropertyValueFactory<>("percu"));
        renduColumn.setCellValueFactory(new PropertyValueFactory<>("rendu"));

        factureTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clientField.setText(newSelection.getClient());
                caissierField.setText(newSelection.getCaissier());
                montantField.setText(String.valueOf(newSelection.getMontant()));
                percuField.setText(String.valueOf(newSelection.getPercu()));
                renduField.setText(String.valueOf(newSelection.getRendu()));
            }
        });

        // Open database connection
        connection = MyDatabase.getInstance().getConnection();

        // Load data into the table
        afficherFactures();
    }

    private void afficherFactures() {
        System.out.println("fghjkl");
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM facture");
            ResultSet rs = statement.executeQuery();

            // Clear existing items in the table
            factureTableView.getItems().clear();

            // Populate the table with data from the database
            while (rs.next()) {
                Facture facture = new Facture(
                        rs.getInt("id_fact"),
                        rs.getString("client"),
                        rs.getString("caissier"),
                        rs.getDouble("montant"),
                        rs.getDouble("percu"),
                        rs.getDouble("rendu")
                );
                factureTableView.getItems().add(facture);
            }

            // Close the resources
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching data from the database: " + e.getMessage());
        }
    }

    @FXML
    private void supprimerFacture() {
        // Get the selected facture from the table view
        Facture selectedFacture = factureTableView.getSelectionModel().getSelectedItem();
        if (selectedFacture == null) {
            // No facture selected, display an error message or handle it as needed
            System.out.println("No facture selected for deletion.");
            return;
        }

        // Delete the selected facture from the database
        try {
            connection = MyDatabase.getInstance().getConnection();
            String sql = "DELETE FROM facture WHERE id_fact = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, selectedFacture.getId());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                // Facture deleted successfully, update the table view
                factureTableView.getItems().remove(selectedFacture);
                System.out.println("Facture deleted successfully.");
            } else {
                // No rows affected, display an error message or handle it as needed
                System.out.println("No rows affected by deletion.");
            }
            statement.close();

        } catch (SQLException e) {
            System.err.println("Error deleting facture: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
    @FXML
    private void modifierFacture() {
        // Get the selected facture from the table view
        Facture selectedFacture = factureTableView.getSelectionModel().getSelectedItem();
        if (selectedFacture == null) {
            // No facture selected, display an error message or handle it as needed
            System.out.println("No facture selected for modification.");
            return;
        }

        // Update the selected facture's data in the database
        try {
            connection = MyDatabase.getInstance().getConnection();
            String sql = "UPDATE facture SET client = ?, caissier = ?, montant = ?, percu = ?, rendu = ? WHERE id_fact = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, clientField.getText());
            statement.setString(2, caissierField.getText());
            statement.setDouble(3, Double.parseDouble(montantField.getText()));
            statement.setDouble(4, Double.parseDouble(percuField.getText()));
            statement.setDouble(5, Double.parseDouble(renduField.getText()));
            statement.setInt(6, selectedFacture.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                // Facture updated successfully
                System.out.println("Facture updated successfully.");
            } else {
                // No rows affected, display an error message or handle it as needed
                System.out.println("No rows affected by modification.");
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error updating facture: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    @FXML
    private void generatePDF() {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Position for the first line
            float yPosition = page.getMediaBox().getHeight() - 50;

            // Add client information
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Client: " + clientField.getText());
            contentStream.newLine();
            contentStream.showText("Caissier: " + caissierField.getText());
            contentStream.newLine();
            contentStream.newLine(); // Add extra space between sections
            contentStream.endText();

            // Position for the table header
            yPosition -= 20;

            // Add table headers
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Facture:");
            contentStream.newLine();
            contentStream.showText("Client    Caissier    Montant    Percu    Rendu");
            contentStream.newLine();
            contentStream.endText();

            // Position for the table data
            yPosition -= 20;

            // Iterate through table data and add to PDF
            for (Facture facture : factureTableView.getItems()) {
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText(facture.getClient());
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(facture.getCaissier());
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(String.valueOf(facture.getMontant()));
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(String.valueOf(facture.getPercu()));
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(String.valueOf(facture.getRendu()));
                contentStream.endText();
                yPosition -= 20; // Move to the next line
            }

            contentStream.close();

            // Save the PDF file
            File file = new File("Facture.pdf");
            document.save(file);
            document.close();

            System.out.println("PDF generated successfully.");
        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
    @FXML
    private void applypromo() {

        String code =codepromo.getText();
        Facture selectedFacture = factureTableView.getSelectionModel().getSelectedItem();
        if (selectedFacture == null) {
            // No facture selected, display an error message or handle it as needed
            System.out.println("No facture selected for modification.");
            return;

        }
        if("promo24".equals(code)){
            try {   System.out.println("No jhbkihnjlk selected for modification.");
                connection = MyDatabase.getInstance().getConnection();
                double newMontant = selectedFacture.getMontant() * 0.9;
                double newrendu = selectedFacture.getPercu()-newMontant;
                String sql = "UPDATE facture SET  montant = ?,rendu=? WHERE id_fact = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setDouble(1, newMontant);
                statement.setDouble(2, newrendu);
                statement.setInt(3, selectedFacture.getId());
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    // Facture updated successfully
                    System.out.println("Facture updated successfully.");
                    try {
                        PreparedStatement ex = connection.prepareStatement("SELECT * FROM facture");
                        ResultSet rs = ex.executeQuery();

                        // Clear existing items in the table
                        factureTableView.getItems().clear();

                        // Populate the table with data from the database
                        while (rs.next()) {
                            Facture facture = new Facture(
                                    rs.getInt("id_fact"),
                                    rs.getString("client"),
                                    rs.getString("caissier"),
                                    rs.getDouble("montant"),
                                    rs.getDouble("percu"),
                                    rs.getDouble("rendu")
                            );
                            factureTableView.getItems().add(facture);
                        }

                        // Close the resources

                    } catch (SQLException e) {
                        System.err.println("Error fetching data from the database: " + e.getMessage());
                    }
                } else {
                    // No rows affected, display an error message or handle it as needed
                    System.out.println("No rows affected by modification.");
                }


            } catch (SQLException e) {
                System.err.println("Error updating facture: " + e.getMessage());
                e.printStackTrace(); // Print the stack trace for debugging
            }
        }
        }



}

