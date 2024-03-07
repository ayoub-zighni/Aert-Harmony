package controllers;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import utils.MyDatabase;

public class LivreurStatistics implements Initializable {

    @FXML
    private PieChart piechart;

    @FXML
    private Button statistics;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try {
            Connection conn = MyDatabase.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nom, COUNT(*) AS total FROM livreur GROUP BY nom");

            while (rs.next()) {
                String nomL = rs.getString("nom");
                int total = rs.getInt("total");
                pieChartData.add(new PieChart.Data(nomL, total));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " : ", data.pieValueProperty().intValue(), " livreur"
                        )
                )
        );

        piechart.getData().addAll(pieChartData);
    }
}