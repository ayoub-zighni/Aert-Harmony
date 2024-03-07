package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Utilisateur;
import services.ImageService;
import services.UtilisateurService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

public class ChoseYourPhotoController {

    @FXML
    private ImageView PhotoPlace;

    private String currentImagePath;

    private UtilisateurService utilisateurService = new UtilisateurService();

    public void ImportPhoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        Stage stage = (Stage) PhotoPlace.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            currentImagePath = file.toURI().toString();
            PhotoPlace.setImage(new Image(currentImagePath));
        }
    }
    public void SavePhoto(ActionEvent actionEvent) {
    }
}