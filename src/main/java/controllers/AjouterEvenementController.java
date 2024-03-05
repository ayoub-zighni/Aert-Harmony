package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import models.Evenement;
import services.EvenementService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjouterEvenementController {
    private EvenementService evenementService;
    private int GalerieID; // Declare GalerieID as a class property


    @FXML
    private TextField nomE;

    @FXML
    private TextField GalerieidG;

    @FXML
    private TableColumn<?, ?> Galerieidcol;

    @FXML
    private DatePicker dateE;

    @FXML
    private TextField heureE;

    @FXML
    private TextArea descriptionE;

    @FXML
    private TableView<Evenement> tableView;

    @FXML
    private TableColumn<Evenement, String> nomEColumn;

    @FXML
    private TableColumn<Evenement, LocalDate> dateEColumn;

    @FXML
    private TableColumn<Evenement, LocalTime> heureEColumn;

    @FXML
    private TableColumn<Evenement, String> descriptionEColumn;

    @FXML
    private Button ajouterButton;

    @FXML
    private Button modifierButton;

    @FXML
    private Button supprimerButton;

    @FXML
    private Button genererQRCodeButton;


    public AjouterEvenementController() {

        this.evenementService = new EvenementService();
        this.GalerieID = GalerieID; // Initialize GalerieID in the constructor
    }




    @FXML
    public void initialize() {
        nomEColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        dateEColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureEColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
        descriptionEColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        Galerieidcol.setCellValueFactory(new PropertyValueFactory<>("GalerieID"));


        // Charger les données dans le tableau
        loadData();
    }

    private void loadData() {
        try {
            // Récupérer toutes les données de la base de données
            List<Evenement> evenements = evenementService.recuperer();
            // Ajouter les données au TableView
            tableView.setItems(FXCollections.observableArrayList(evenements));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ajouterEvenement(ActionEvent event) {
        try {
            String nom = nomE.getText();
            LocalDate date = dateE.getValue();
            String heure = heureE.getText();
            String description = descriptionE.getText();
            int galerieID = Integer.parseInt(GalerieidG.getText()); // Récupérer la valeur de GalerieidG

            if (nom.isEmpty() || date == null || heure.isEmpty() || description.isEmpty() ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }

            LocalTime localTime = LocalTime.parse(heure);
            Evenement newEvent = new Evenement(0, nom, date, localTime, description, galerieID); // Utiliser galerieID
            evenementService.ajouter(newEvent);

            // Ajouter le nouvel événement aux données du TableView
            tableView.getItems().add(newEvent);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Format d'heure invalide. Utilisez le format HH:mm.");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez entrer un nombre valide pour l'identifiant de la galerie.");
            alert.showAndWait();
        }
    }

    @FXML
    void modifierEvenement(ActionEvent event) {
        Evenement selectedEvent = tableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            // Récupérer les nouvelles valeurs depuis les champs de texte
            String newNom = nomE.getText();
            LocalDate newDate = dateE.getValue();
            String newHeure = heureE.getText();
            String newDescription = descriptionE.getText();

            if (newNom.isEmpty() || newDate == null || newHeure.isEmpty() || newDescription.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }

            try {
                LocalTime newLocalTime = LocalTime.parse(newHeure);
                // Mettre à jour l'événement sélectionné
                selectedEvent.setNom(newNom);
                selectedEvent.setDate(newDate);
                selectedEvent.setHeure(newLocalTime);
                selectedEvent.setDescription(newDescription);

                // Mettre à jour la base de données
                evenementService.modifier(selectedEvent);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                throw new RuntimeException(e);
            } catch (DateTimeParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Format d'heure invalide. Utilisez le format HH:mm.");
                alert.showAndWait();
            }

            // Rafraîchir les données dans le TableView
            tableView.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner un événement à modifier.");
            alert.showAndWait();
        }
    }

    @FXML
    void supprimerEvenement(ActionEvent event) {
        String nomEvenement = nomE.getText(); // Récupérer le nom de l'événement sélectionné
        if (!nomEvenement.isEmpty()) {
            try {
                Evenement evenementASupprimer = evenementService.rechercherEvenementParNom(nomEvenement);
                if (evenementASupprimer != null) {
                    // Supprimer l'événement sélectionné de la base de données
                    evenementService.supprimer(evenementASupprimer.getId());

                    // Supprimer l'événement sélectionné de la liste d'éléments de votre TableView
                    tableView.getItems().remove(evenementASupprimer);

                    // Rafraîchir le TableView
                    tableView.setItems(FXCollections.observableArrayList(evenementService.recuperer()));

                    // Rafraîchir le TableView
                    tableView.refresh();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Supprimer ");
                    alert.setContentText("Aucun événement trouvé avec le nom " + nomEvenement);
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Supprimer");
            alert.setContentText("Veuillez saisir un nom d'événement.");
            alert.showAndWait();
        }
    }
    public void setGalerieID(int galerieID) {
        this.GalerieID = galerieID;
    }



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

    @FXML
    void importer(ActionEvent importer) {
        StringBuilder contenu = new StringBuilder();

        // Parcourir les éléments de votre TableView
        for (Evenement evenement : tableView.getItems()) {
            contenu.append("Nom: ").append(evenement.getNom()).append("\n");
            contenu.append("Date: ").append(evenement.getDate()).append("\n");
            contenu.append("Heure: ").append(evenement.getHeure()).append("\n");
            contenu.append("Description: ").append(evenement.getDescription()).append("\n");
            contenu.append("Galerie ID: ").append(evenement.getGalerieID()).append("\n");
            contenu.append("\n");
        }

        // Créer un TextArea pour afficher le contenu
        TextArea textArea = new TextArea(contenu.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Créer une boîte de dialogue personnalisée
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contenu du TableView");

        // Créer un VBox pour contenir le TextArea
        VBox vbox = new VBox();
        vbox.getChildren().add(textArea);

        // Définir le contenu de la boîte de dialogue
        alert.getDialogPane().setContent(vbox);

        // Afficher la boîte de dialogue
        alert.showAndWait();
    }

    }
