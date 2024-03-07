package services;

import models.Galeries;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GaleriesService implements IServicegaleries<Galeries> {

    private Connection connection;

    public GaleriesService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Galeries galeries) throws SQLException {
        String sql = "insert into galeries (nom, adresse, ville, pays, capacite_max) " +
                "values('" + galeries.getNom() + "','" + galeries.getAdresse() + "','" +
                galeries.getVille() + "','" + galeries.getPays() + "'," + galeries.getCapaciteMax() + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void modifier(Galeries galeries) throws SQLException {
        String sql = "update galeries set nom = ?, adresse = ?, ville = ?, pays = ?, capacite_max = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, galeries.getNom());
        preparedStatement.setString(2, galeries.getAdresse());
        preparedStatement.setString(3, galeries.getVille());
        preparedStatement.setString(4, galeries.getPays());
        preparedStatement.setInt(5, galeries.getCapaciteMax());
        preparedStatement.setInt(6, galeries.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from galeries where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Galeries> afficher() throws SQLException {
        return null;
    }


    public List<Galeries> recuperer() throws SQLException {
        String sql = "select * from galeries";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Galeries> galeriesList = new ArrayList<>();
        while (rs.next()) {
            Galeries g = new Galeries();
            g.setId(rs.getInt("id")); // Changer "ID" en "id"
            g.setNom(rs.getString("Nom"));
            g.setAdresse(rs.getString("Adresse"));
            g.setVille(rs.getString("Ville"));
            g.setPays(rs.getString("Pays"));
            g.setCapaciteMax(rs.getInt("Capacite_Max")); // Convertir la valeur en entier
            galeriesList.add(g);
        }
        return galeriesList;
    }


}
