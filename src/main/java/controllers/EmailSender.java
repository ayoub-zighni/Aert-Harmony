package controllers;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class EmailSender {
    public void sendEmail(String email, String newPassword, String title, String contenu) {
        String from = "scarrr66@gmail.com";
        String pass = "tjps foqv ngez vibp";
        // Configuration de la session SMTP pour l'envoi d'e-mails
        Properties props = System.getProperties();
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // Création d'une nouvelle session SMTP
        Session session = Session.getDefaultInstance(props);

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Adresse e-mail du destinataire
            message.setSubject(title);
            message.setText(contenu);

            // Envoi du message
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "scarrr66@gmail.com", "tjps foqv ngez vibp");
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();

            System.out.println("E-mail envoyé avec succès à " + email + " avec le nouveau mot de passe.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'e-mail à " + email + " : " + e.getMessage());
        }
    }
}