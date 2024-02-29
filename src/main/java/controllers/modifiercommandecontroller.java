package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import models.Commande;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class modifiercommandecontroller {




        @FXML
        private TextField adrlivraisonupdate;

        @FXML
        private Button btnmodifierC;

        @FXML
        private DatePicker datecommandeupdate;

        @FXML
        private TextField idLupdate;

        @FXML
        private Text modificationC;

        @FXML
        private TextField statutcommandeupdate;




    private Commande commandeToupdate;

    public void initData(Commande commandeToupdate) {
        this.commandeToupdate = commandeToupdate;
        // Mettre à jour les champs de texte avec les informations de la commande
        LocalDate dateCommande = LocalDate.parse(commandeToupdate.getDateCommande().toLocaleString()); // Conversion de la Date en LocalDate
        datecommandeupdate.setValue(dateCommande); // Attribuer la valeur de la date au DatePicker

        statutcommandeupdate.setText(commandeToupdate.getStatutCommande());
        adrlivraisonupdate.setText(commandeToupdate.getAdresseLivraison());
        idLupdate.setText(String.valueOf(commandeToupdate.getId())); // Convertir l'ID en une chaîne de caractères pour l'afficher
    }

}
