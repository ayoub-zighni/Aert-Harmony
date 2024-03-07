package controllers;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Utilisateur;

import java.io.IOException;
import java.security.SecureRandom;

public class ChoseMethodeController {

    @FXML
    private TextField verifLabel;

    @FXML
    private TextField smsField;

    @FXML
    private Label messageLabel;

    @FXML
    private Label label1;


    @FXML
    private Button xx;
    private String currentVerificationCode;

    private final SecureRandom random = new SecureRandom();

    private EmailSender emailSender = new EmailSender();

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    private String verificationCode;



    public static final String ACCOUNT_SID = "AC025032e778c86b92ea343ab53a10df58";
    public static final String AUTH_TOKEN = "196e98bd7da6cbd22589051b93ca4b96";
    public static final String VERIFY_SID = "VA759979bf90d96e4ab6ed8c2f8dbb1d8d";


    public void initialize(){
        hideVerificationFields();
    }
    public void hideVerificationFields() {
        verifLabel.setVisible(false);
        label1.setVisible(false);
        smsField.setVisible(false);
        xx.setVisible(false);

    }
    public void SendMailVerif(ActionEvent actionEvent) {
        String verificationCode = generateVerificationCode();
        // Set the verification code in the Utilisateur object
        utilisateur.setVerificationCode(verificationCode);
        // Assuming you have access to the user's email address through some means
        String userEmail = utilisateur.getEmail(); // Assuming utilisateur has a getEmail() method

        // Send the verification code to the user's email address
        String emailSubject = "Change Password";
        String emailBody = "Your verification code is: " + verificationCode; // Use the generated verification code

        // Replace "emailSender" with the actual object responsible for sending emails
        // Assuming emailSender has a sendEmail method with appropriate parameters
        emailSender.sendEmail(userEmail, verificationCode, emailSubject, emailBody);

        // Show the verification field and label
        verifLabel.setVisible(true);
        label1.setVisible(true);
        messageLabel.setText("Verification code sent to " + userEmail);
        xx.setVisible(true);
    }

    public void InterChange(ActionEvent actionEvent) {
        String verificationCodeEntered = ""; // Initialize it with an empty string for now

        // Check which verification method was chosen
        if (verifLabel.isVisible()) {
            verificationCodeEntered = verifLabel.getText().trim(); // Use the email verification code
        } else if (smsField.isVisible()) {
            verificationCodeEntered = smsField.getText().trim(); // Use the SMS verification code
        } else {
            // Handle the case where neither field is visible
            messageLabel.setText("No verification method selected. Please try again.");
            return; // Exit the method
        }

        String correctVerificationCode = utilisateur.getVerificationCode(); // Retrieve the verification code from the Utilisateur object

        System.out.println("Entered code: " + verificationCodeEntered); // Debugging statement
        System.out.println("Stored code: " + correctVerificationCode); // Debugging statement

        if (verificationCodeEntered.equals(correctVerificationCode)) {
            // Verification code is correct
            messageLabel.setText("Verification code is correct.");
            utilisateur.setVerificationCode(null); // Clear the verification code
            // Add your code to navigate to ChangePassword.fxml here
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangePassword.fxml"));
            Parent root = loader.load();
            // Assuming you have a method to set the Utilisateur object in the ChangePassword controller
            ChangePassword changePasswordController = loader.getController();
            changePasswordController.setUtilisateur(utilisateur);

            // Get the current scene and set the new root
            Scene currentScene = messageLabel.getScene();
            if (currentScene != null) {
                currentScene.setRoot(root);
            } else {
                // If the scene is not available, you might need to handle this differently
                // For example, by creating a new Stage and setting the root there
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace
            messageLabel.setText("An error occurred while loading ChangePassword.fxml. Please try again.");
        }}
        else {
            // Verification code is incorrect
            messageLabel.setText("Invalid verification code. Please try again.");
        }
    }

    private String getCurrentVerificationCode () {
        // Return the current verification code
        return currentVerificationCode;
    }
    public String generateVerificationCode() {
        // Generate a random 6-digit verification code
        return String.format("%06d", random.nextInt(1000000));
    }

    public void GetSms(ActionEvent actionEvent) {
        Twilio.init(VerifyNumberController.ACCOUNT_SID, VerifyNumberController.AUTH_TOKEN);

        // Generate verification code
        String verificationCode = generateVerificationCode();
        // Set the verification code in the Utilisateur object
        utilisateur.setVerificationCode(verificationCode);

        String phoneNumber = utilisateur.getNumtel();

        // Add the country code if it's missing
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+216" + phoneNumber;
        }

        // Send verification code via SMS
        Verification verification = Verification.creator(VERIFY_SID, phoneNumber, "sms").create();

        showAlert(Alert.AlertType.INFORMATION, "Verification Sent", "A verification code has been sent to your phone number.");

        // Show the SMS field and button
        smsField.setVisible(true);
        messageLabel.setText("Please enter the verification code sent to your phone number.");
        xx.setVisible(true);
    }
        // Show the SMS field

    // Method to display an alert dialog
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to request OTP input from the user
    private String requestOTP() {
        // You can implement this part according to your JavaFX application's UI logic
        // For example, you can open a dialog box or prompt the user to input the OTP
        return "123456"; // Replace with user's input
    }
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void Exit(ActionEvent actionEvent) {
    }
}
