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
import services.ImageService;

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

    private ImageService imageService = new ImageService();

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
        try {
            if (currentImagePath != null) {
                List<String> imagePaths = imageService.getAllImagePaths();
                if (!imagePaths.contains(currentImagePath)) {
                    imageService.saveImage(currentImagePath);
                    System.out.println("Image path saved successfully!");
                } else {
                    System.out.println("Image path already exists in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately, e.g., show an error message
        }
    }
}