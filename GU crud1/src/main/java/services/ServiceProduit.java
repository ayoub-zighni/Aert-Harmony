package services;

 import com.twilio.Twilio;
 import com.twilio.rest.api.v2010.account.Message;
 import com.twilio.type.PhoneNumber;
 import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.categorie;
import models.produits;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 import java.util.Properties;
 import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceProduit  implements ISercivesProduit {

    Connection cnx = DataSource.getInstance().getConnection();
    ObservableList<produits> obList = FXCollections.observableArrayList();

    @Override
    public void ajouterProduit(produits c) {
        String req = "INSERT INTO `produits`(`nom`, `description`, `image`, `prix`,`stock`,idcategorie) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getImage());
            ps.setInt(4, c.getPrix());
            ps.setString(5, c.getStock());
            ps.setInt(6, c.getIdcategorie());

            ps.executeUpdate();

            System.out.println("Produit ajouté avec succès!");
            String to = "ayoub.zighni@esprit.tn"; // Get user email from database

          //  String to = getUserEmail(reponse.getId_reclamation()); // Get user email from database
                 String subject = "New Product";
                 String body = "Bonjour,\n\n Un  nouveaux produit : "+c.getNom()+" avec  prix =" +c.getPrix()+ " TND  est  est ajouteé dans dans notre application\n\nCordialement,\nL'equipe de support";
                sendEmail(to, subject, body);



        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void sendEmail(String to, String subject, String body) {
        String username = "ayoub.zighni@esprit.tn";
        String password = "zizztmwqnfsnnyly";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Change this to your SMTP server host(yahoo...)
        props.put("mail.smtp.port", "587"); // Change this to your SMTP server port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session;
        session = Session.getInstance(props,new Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(username, password);
            }
        });


        try {
            // Create a MimeMessage object

            // Create a new message
            MimeMessage message = new MimeMessage(session);
            // Set the From, To, Subject, and Text fields of the message
            message.setFrom(new InternetAddress(username));
            message.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the message using Transport.send
            Transport.send(message);

            System.out.println("Email sent successfully");
        } catch (MessagingException ex) {
            System.err.println("Failed to send email: " + ex.getMessage());
        }

    }

            public String getNomCategorieById(int idCategorie) {
        String nomCategorie = null;
        String req = "SELECT nom FROM categorie WHERE idcat = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, idCategorie);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nomCategorie = rs.getString("nom");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nomCategorie;
    }
    @Override
    public List<produits> afficherProduit() {
        List<produits> produits = new ArrayList<>();
        String req = "SELECT * FROM produits ";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                produits E = new produits();
                E.setId(rs.getInt("id"));
                E.setNom(rs.getString("nom"));
                E.setDescription(rs.getString("description"));
                E.setImage(rs.getString("image"));
                E.setPrix(rs.getInt("prix"));
                E.setStock(rs.getString("stock"));
                E.setIdcategorie(rs.getInt("idcategorie"));
                produits.add(E);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produits;
    }


    @Override

    public void modifierProduit(produits c) {
        try {
            String req = "UPDATE `produits` SET `nom`=?, `description`=?, `image`=?, `prix`=?, `stock`=?, `idcategorie`=? WHERE id=?";
            try (PreparedStatement st = cnx.prepareStatement(req)) {
                st.setString(1, c.getNom());
                st.setString(2, c.getDescription());
                st.setString(3, c.getImage());
                st.setInt(4, c.getPrix());
                st.setString(5, c.getStock());
                st.setInt(6, c.getIdcategorie());
                st.setInt(7, c.getId());

                int rowsAffected = st.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Produit modifié avec succès");

          // Send SMS to user
          String accountSid = "AC0195a1bce21aa8f71596a55b7f420153";
          String authToken = "a53f10f9ab86c9696ccbae418fd51500";
          Twilio.init(accountSid, authToken);
          String fromPhoneNumber = "+19284409881"; // Change this to your Twilio phone number
          String toPhoneNumber = "+21692492396"; // Change this to the user's phone number
          String smsBody = "Bonjour, Nouvelle Promotion avec prix ="+c.getPrix()+"  TND. Cordialement, L'équipe de support";
          Message message = Message.creator(new PhoneNumber(toPhoneNumber), new PhoneNumber(fromPhoneNumber), smsBody).create();
          System.out.println("SMS sent successfully to " + toPhoneNumber);

                } else {
                    System.out.println("Aucune modification effectuée pour le Produit avec l'ID " + c.getId());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void supprimerProduit(int id) {

        try {
            String req = "DELETE FROM `produits` WHERE id=?";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, id);
            st.executeUpdate();
            System.out.println("produit supprimer avec succès");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
