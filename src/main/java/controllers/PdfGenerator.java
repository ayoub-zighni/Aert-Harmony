package controllers;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import models.Commande;

public class PdfGenerator {
    public static void generatePDF(String filePath, List<Commande> commandes) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Ajouter le titre
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 19, Font.BOLD);
            Paragraph title = new Paragraph("Liste des commandes ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Ajouter la photo
            Image image = Image.getInstance("C://Users//bahah//Desktop//logo.png"); // Remplacez "path/vers/votre/photo.jpg" par le chemin de votre photo
            image.setAlignment(Element.ALIGN_CENTER);
            image.scaleToFit(2, 2); // Ajustez la taille de l'image selon vos besoins
            document.add(image);

            // Ajouter une table avec les réservations
            PdfPTable table = new PdfPTable(4);
            // Ajouter les en-têtes de colonnes
            table.addCell("ID");
            table.addCell("Date de la commande");
            table.addCell("Statut de la commande ");
            table.addCell("Adresse de livraison");


            // Ajouter les données de chaque réservation dans la table
            for (Commande c : commandes) {
                table.addCell(String.valueOf((c.getIdC())));

                table.addCell(String.valueOf(c.getAdresseLivraison()));
                table.addCell(String.valueOf(c.getNumTel()));

            }

            // Ajouter la table au document
            document.add(table);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

}
