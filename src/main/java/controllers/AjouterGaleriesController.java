package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Galeries;
import services.GaleriesService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
public class AjouterGaleriesController {


    private GaleriesService gs;

    @FXML
    private TextField IdG;

    @FXML
    private TextField nomG;

    @FXML
    private TextField adresseG;

    @FXML
    private TextField villeG;

    @FXML
    private TextField paysG;

    @FXML
    private TextField capaciteMaxG;

    @FXML
    private TableView<Galeries> tableView;

    @FXML
    private TableColumn<Galeries, String> IdGColumn;

    @FXML
    private TableColumn<Galeries, String> nomGColumn;

    @FXML
    private TableColumn<Galeries, String> adresseGColumn;

    @FXML
    private TableColumn<Galeries, String> villeGColumn;

    @FXML
    private TableColumn<Galeries, String> paysGColumn;

    @FXML
    private TableColumn<?, ?> capaciteMaxGColumn;
    @FXML
    private Button ajouterButton;

    @FXML
    private Button modifierButton;

    @FXML
    private Button supprimerButton;

    @FXML
    private Button genererPDFButton;

    @FXML
    private Button statButton;

    @FXML
    private PieChart pieChart;

    public AjouterGaleriesController() {
        this.gs = new GaleriesService();
    }

    @FXML
    public void initialize() {
        IdGColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomGColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseGColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        villeGColumn.setCellValueFactory(new PropertyValueFactory<>("ville"));
        paysGColumn.setCellValueFactory(new PropertyValueFactory<>("pays"));
        capaciteMaxGColumn.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));

        // Charger les données dans le tableau
        loadData();
    }


    private void loadData() {
        try {
            // Récupérer toutes les données de la base de données
            List<Galeries> galeries = gs.recuperer();
            // Ajouter les données au TableView
            tableView.setItems(FXCollections.observableArrayList(galeries));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void ajouterGalerie(ActionEvent event) {
        try {
            String nom = nomG.getText();
            String adresse = adresseG.getText();
            String ville = villeG.getText();
            String pays = paysG.getText();
            String capaciteMaxText = capaciteMaxG.getText();

            // Vérifier que tous les champs sont remplis
            if (nom.isEmpty() || adresse.isEmpty() || ville.isEmpty() || pays.isEmpty() || capaciteMaxText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }

            // Vérifier que la capacité maximale est un nombre valide
            int capaciteMax = 0;
            try {
                capaciteMax = Integer.parseInt(capaciteMaxText);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Galerie ajoutée avec succès.");
                alert.showAndWait();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("La capacité maximale doit être un nombre valide.");
                alert.showAndWait();
                return;
            }

            Galeries newGallery = new Galeries(nom, adresse, ville, pays, capaciteMax);
            GaleriesService galeriesService = new GaleriesService();
            galeriesService.ajouter(newGallery);

            tableView.getItems().add(newGallery);

            // Mettre à jour la colonne de l'ID
            IdGColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }

    }



    @FXML
    void modifierGalerie(ActionEvent event) {
        Galeries selectedGallery = tableView.getSelectionModel().getSelectedItem();
        if (selectedGallery != null) {
            int idGalerie = selectedGallery.getId();

            String newNom = nomG.getText();
            String newAdresse = adresseG.getText();
            String newVille = villeG.getText();
            String newPays = paysG.getText();
            String newCapaciteMaxText = capaciteMaxG.getText();

            // Vérifier que tous les champs sont remplis
            if (newNom.isEmpty() || newAdresse.isEmpty() || newVille.isEmpty() || newPays.isEmpty() || newCapaciteMaxText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }

            // Vérifier que la capacité maximale est un nombre valide
            int newCapaciteMax = 0;
            try {
                newCapaciteMax = Integer.parseInt(newCapaciteMaxText);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("La capacité maximale doit être un nombre valide.");
                alert.showAndWait();
                return;
            }

            try {
                selectedGallery.setNom(newNom);
                selectedGallery.setAdresse(newAdresse);
                selectedGallery.setVille(newVille);
                selectedGallery.setPays(newPays);
                selectedGallery.setCapaciteMax(newCapaciteMax);

                gs.modifier(selectedGallery);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Galerie modifiée avec succès.");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                throw new RuntimeException(e);
            }

            tableView.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une galerie à modifier.");
            alert.showAndWait();
        }
    }


    @FXML
    void supprimerGalerie(ActionEvent event) {
        Galeries selectedGallery = tableView.getSelectionModel().getSelectedItem();
        if (selectedGallery != null) {
            try {
                String idGalerie = String.valueOf(selectedGallery.getId());
                gs.supprimer(Integer.parseInt(idGalerie));
                tableView.getItems().remove(selectedGallery);
                tableView.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Galerie supprimée avec succès.");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner une galerie à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    void genererPDF(ActionEvent event) {
        Document document = new Document();
        try {
            String outputPath = "C:\\Users\\\\Amin Dabbousi\\\\Desktop\\\\galeries.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Ajout d'un titre avec une police différente et une taille de police plus grande
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("Liste des galeries", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Espacement avant le tableau
            document.add(new Paragraph("\n"));

            // Création du tableau
            PdfPTable table = new PdfPTable(5); // 5 colonnes pour chaque propriété de Galeries

            // Entêtes de colonne avec une police différente et une couleur de fond
            Font columnHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            PdfPCell cell = new PdfPCell(new Phrase("ID", columnHeaderFont));
            cell.setBackgroundColor(BaseColor.BLUE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Nom", columnHeaderFont));
            cell.setBackgroundColor(BaseColor.BLUE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Adresse", columnHeaderFont));
            cell.setBackgroundColor(BaseColor.BLUE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Ville", columnHeaderFont));
            cell.setBackgroundColor(BaseColor.BLUE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Pays", columnHeaderFont));
            cell.setBackgroundColor(BaseColor.BLUE);
            table.addCell(cell);

// Ajout des données de la table
            for (Galeries galerie : tableView.getItems()) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(galerie.getId())))); // Convertir l'ID en String
                table.addCell(new PdfPCell(new Phrase(galerie.getNom())));
                table.addCell(new PdfPCell(new Phrase(galerie.getAdresse())));
                table.addCell(new PdfPCell(new Phrase(galerie.getVille())));
                table.addCell(new PdfPCell(new Phrase(galerie.getPays())));
            }



            document.add(table);

            document.close();
            System.out.println("PDF généré avec succès.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("PDF généré avec succès.");
            alert.showAndWait();
        } catch (FileNotFoundException | DocumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur est survenue lors de la génération du PDF : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void afficherStatistiques(ActionEvent event) {
        try {
            List<Galeries> galeries = gs.recuperer();
            int capaciteTotale = galeries.stream().mapToInt(Galeries::getCapaciteMax).sum();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (Galeries galerie : galeries) {
                double pourcentage = ((double) galerie.getCapaciteMax() / (double) capaciteTotale) * 100;
                pieChartData.add(new PieChart.Data("Capacité maximale: " + galerie.getCapaciteMax() + " : " + String.format("%.2f", pourcentage) + "%", pourcentage));
            }

            pieChart.setData(pieChartData);
            pieChart.setStyle("-fx-background-color: white;"); // Définit le fond blanc du graphique circulaire
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur est survenue lors du calcul des statistiques : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void event(ActionEvent event) {

        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ajouterEvenementController.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setTitle("doorstep!");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            System.err.println("IOException: Error loading listrep.fxml");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: An unknown error occurred");
            e.printStackTrace();
        }


    }




}