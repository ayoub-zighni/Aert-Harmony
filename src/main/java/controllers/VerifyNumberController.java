package controllers;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCreator;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;


public class VerifyNumberController {

    @FXML
    private TextField PhoneField;

    // Twilio account credentials
    public static final String ACCOUNT_SID = "AC025032e778c86b92ea343ab53a10df58";
    public static final String AUTH_TOKEN = "196e98bd7da6cbd22589051b93ca4b96";
    public static final String VERIFY_SID = "VA759979bf90d96e4ab6ed8c2f8dbb1d8d";

    public void SendSms(ActionEvent actionEvent) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Send verification code via SMS
        Verification verification = Verification.creator(VERIFY_SID, PhoneField.getText(), "sms").create();

        showAlert(Alert.AlertType.INFORMATION, "Verification Sent", "A verification code has been sent to your phone number.");

        // Request OTP input from the user
        String otpCode = requestOTP();

        // Verify OTP
        VerificationCheck verificationCheck = VerificationCheck.creator(VERIFY_SID, otpCode).create();

        showAlert(Alert.AlertType.INFORMATION, "Verification Status", "Verification status: " + verificationCheck.getStatus());
    }

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
}
