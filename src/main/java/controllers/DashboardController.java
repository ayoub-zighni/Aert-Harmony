package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label messageLabel;
    public void deconnecter(ActionEvent actionEvent) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.initStyle(StageStyle.UNDECORATED);
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to log out?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed logout, navigate back to login screen
                Stage currentStage = (Stage) messageLabel.getScene().getWindow(); // Assuming messageLabel is in Dashboard.fxml
                currentStage.close(); // Close the current stage (dashboard)
                try {
                    // Load the login screen
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                    Parent root = loader.load();
                    Stage loginStage = new Stage();
                    loginStage.setScene(new Scene(root));
                    loginStage.setTitle("Login");
                    loginStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getTheData(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherUtilisateur.fxml"));
            messageLabel.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
        }

    }}
