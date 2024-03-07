package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.ImageService;

import java.sql.SQLException;
import java.util.List;


public class DisplayPhotoController {
    @FXML
    private ImageView img;

    private ImageService imageService = new ImageService();
    private List<String> imagePaths;
    private int currentImageIndex = -1;

    public void initialize() {
        try {
            imagePaths = imageService.getAllImagePaths();
            if (!imagePaths.isEmpty()) {
                currentImageIndex = 0;
                displayImage(currentImageIndex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately, e.g., show an error message
        }
    }
    public void NextPhoto(ActionEvent actionEvent) {
        if (imagePaths != null && !imagePaths.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % imagePaths.size();
            displayImage(currentImageIndex);
        }
    }
    private void displayImage(int index) {
        try {
            String imagePath = imagePaths.get(index);
            Image image = new Image(imagePath);
            img.setImage(image);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            // Handle index out of bounds error appropriately
        }
    }
}
