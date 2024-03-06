package controller;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 import utils.DataSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


 
public class ChartController implements Initializable {
	   Connection conn;

	    public ChartController() throws SQLException, ClassNotFoundException {
	        this.conn= DataSource.getInstance().getConnection();

        }
    @FXML
    private AnchorPane chartNode;
    @FXML
    private HBox chartHBox;
    public static int numeroPDF = 0;
    Document doc = new Document();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        detaillePrix();
    }
 


    public ObservableList buildDataNbPB(int limitValue) {
        List<PieChart.Data> myList = new ArrayList<>();
        ResultSet rs = null;
        ObservableList<PieChart.Data> observableList = null;

        try {
            String requete = "SELECT idcategorie, SUM(prix * stock) AS prix_total FROM produits GROUP BY idcategorie";
            if (limitValue > 0) {
                requete += " ORDER BY prix_total DESC LIMIT " + limitValue;
            }

            PreparedStatement pst = conn.prepareStatement(requete);
            rs = pst.executeQuery();
            while (rs.next()) {
                String categorie = rs.getString("idcategorie");
                double prixTotal = rs.getDouble("prix_total");
                PieChart.Data d = new PieChart.Data(categorie, prixTotal);
                myList.add(d);
            }
            observableList = FXCollections.observableArrayList(myList);
        } catch (Exception e) {
            System.out.println("Error on DB connection BuildData");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return observableList;
    }
    
    
    public ObservableList buildDataNbEP() {
//     public  ObservableList<PieChart.Data> buildData() {
        List<PieChart.Data> myList = new ArrayList<PieChart.Data>();
        ResultSet rs = null;
        PieChart.Data d;
        ObservableList observableList = null;

        try {
 //nbr  Prix moyen des produits par cat�gorie :

            String requete  = "SELECT c.nom, AVG(p.prix) AS prix_moyen FROM produits p JOIN categorie c ON p.idcategorie = c.idcat GROUP BY c.nom;";

            Statement pst = conn.prepareStatement(requete); // import java.sql.Statement
            rs = pst.executeQuery(requete);
            while (rs.next()) {

                if (rs.getObject(1) == null) {
                    System.out.println(rs.getString(1));
                    d = new PieChart.Data("Autre ", rs.getInt(2));
                } else {
                    d = new PieChart.Data(rs.getString(1), rs.getInt(2));
                }
//                System.out.println(rs.getString(1));
//                System.out.println(rs.getInt(2));
                myList.add(d);

            }
            observableList = FXCollections.observableArrayList(myList);

            return observableList;

        } catch (Exception e) {

            System.out.println("Error on DB connection BuildDataBonPlan");
            System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());

        }
        return observableList;
    }


    public XYChart.Series<String, Number> buildDataLineChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("total des produits par catégorie");

        try {
            String query = "SELECT c.nom, COUNT(*) AS total_produits FROM produits p JOIN categorie c ON p.idcategorie = c.idcat GROUP BY c.nom;";

                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    series.getData().add(new XYChart.Data<>(resultSet.getString("nom"), resultSet.getInt("total_produits")));
                }


        } catch (SQLException e) {
            System.out.println("Error on DB connection or query execution");
            e.printStackTrace();
        }

        return series;
    }

      
      
     
    @FXML
    private void detaillePrix() {
        double total = 0;
        DecimalFormat df2 = new DecimalFormat(".##");
        Stage stage = new Stage();
        Scene scene = new Scene(new Group());
        stage.setTitle("Top N produits les plus chers ");
        stage.setWidth(600);
        stage.setHeight(600);

        final PieChart chart = new PieChart(buildDataNbPB(7));
        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");

        if (chart.getData() != null) {
            for (final PieChart.Data data : chart.getData()) {
                total = total + data.getPieValue();
            }
        final double totalFinal = total;
             // Rest of your code here

        for (final PieChart.Data data : chart.getData()) {
            data.setName(((data.getName() + " " + df2.format((data.getPieValue() / totalFinal) * 100))) + "%");
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    caption.setTranslateX(e.getSceneX());
                    caption.setTranslateY(e.getSceneY());
                    caption.setText(String.valueOf(df2.format((data.getPieValue() / totalFinal) * 100)) + "%");
                    if (!((Group) scene.getRoot()).getChildren().contains(caption)) {
                        ((Group) scene.getRoot()).getChildren().add(caption);
                    }
                }
            });
        }
        } else {
            // Handle the case where chart data is null
            System.out.println("No data in the PieChart.");
        }
        chart.setTitle("Top N produits les plus chers ");
        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        chartNode.getChildren().clear();
        chartNode.getChildren().setAll(chart);

    }

 
    @FXML
    private void globalChart(ActionEvent event) {

    		detaillePrix();
        

    }

    @FXML
    private void detailleEtat(ActionEvent event) {
                 double total = 0;
        DecimalFormat df2 = new DecimalFormat(".##");
        Stage stage = new Stage();
        Scene scene = new Scene(new Group());
        stage.setTitle(" moyenne des prix pour chaque catégorie");
        stage.setWidth(600);
        stage.setHeight(600);
        
        System.out.println(buildDataNbEP());
        final PieChart chart = new PieChart(buildDataNbEP());
        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");

        for (final PieChart.Data data : chart.getData()) {
            total = total + data.getPieValue();
        }
        final double totalFinalCompte = total;

        for (final PieChart.Data data : chart.getData()) {
            data.setName(((data.getName() + " " + df2.format((data.getPieValue() / totalFinalCompte) * 100))) + "%");
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    caption.setTranslateX(e.getSceneX());
                    caption.setTranslateY(e.getSceneY());
                    caption.setText(String.valueOf(df2.format((data.getPieValue() / totalFinalCompte) * 100)) + "%");
                    if (!((Group) scene.getRoot()).getChildren().contains(caption)) {
                        ((Group) scene.getRoot()).getChildren().add(caption);
                    }
                }
            });
        }

        chart.setTitle("Moyenne des prix pour chaque catégorie  ");
        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        chartNode.getChildren().clear();
        chartNode.getChildren().setAll(chart);
    }

    @FXML
    private void lineChart(ActionEvent event) {
        
       
      double total = 0;
        DecimalFormat df2 = new DecimalFormat(".##");
        Stage stage = new Stage();
        Scene scene = new Scene(new Group());
        stage.setTitle("le total des produits par catégorie");
        stage.setWidth(600);
        stage.setHeight(600);


        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Produits");
        //creating the chart
        final LineChart<String,Number> lineChart = 
                new LineChart<String,Number>(xAxis,yAxis);
                
 
        
        lineChart.getData().add(buildDataLineChart());
       ((Group) scene.getRoot()).getChildren().add(lineChart);
        stage.setScene(scene);
        chartNode.getChildren().clear();
        chartNode.getChildren().setAll(lineChart);
        
    }

    @FXML
    private void convertirPDF(ActionEvent event) throws FileNotFoundException, DocumentException, BadElementException, IOException {
        numeroPDF = numeroPDF + 1;
        String nom = "Graph statistique " + numeroPDF + ".pdf";

        try {
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat Heure = new SimpleDateFormat("hh:mm:ss");

            WritableImage wimg = chartNode.snapshot(new SnapshotParameters(), null);

            // Convert JavaFX WritableImage to BufferedImage
            int width = (int) wimg.getWidth();
            int height = (int) wimg.getHeight();
            BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            PixelReader pixelReader = wimg.getPixelReader();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    bimg.setRGB(x, y, pixelReader.getArgb(x, y));
                }
            }

            // Create a temporary file to store the image
            File tempFile = File.createTempFile("ChartSnapshot", ".png");
            ImageIO.write(bimg, "png", tempFile);

            // Create a PDF document
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(System.getProperty("user.home") + "\\Desktop\\" + nom));

            doc.open();

            // Add the image snapshot to the PDF document
            Image img = Image.getInstance(tempFile.getAbsolutePath());
            doc.add(img);

            doc.close();

            // Open the generated PDF file
            Desktop.getDesktop().open(new File(System.getProperty("user.home") + "\\Desktop\\" + nom));

            // Close the writer
            writer.close();

            // Delete the temporary file
            tempFile.delete();

        } catch (Exception e) {
            System.out.println("Error PDF");
            e.printStackTrace();
        }
    }


}
