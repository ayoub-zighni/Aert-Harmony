package services;

import models.Evenement;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IService<Evenement> {

    private Connection connection;

    public EvenementService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Evenement evenement) throws SQLException {
        String sql = "insert into evenements (nom, date, heure, description) " +
                "values('" + evenement.getNom() + "','" + evenement.getDate() + "','" +
                evenement.getHeure() + "','" + evenement.getDescription() + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void modifier(Evenement evenement) throws SQLException {
        String sql = "update evenements set date = ?, heure = ?, description = ? where nom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, Date.valueOf(evenement.getDate()));
        preparedStatement.setTime(2, Time.valueOf(evenement.getHeure()));
        preparedStatement.setString(3, evenement.getDescription());
        preparedStatement.setString(4, evenement.getNom());
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Aucun événement trouvé avec le nom " + evenement.getNom());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from evenements where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Aucun événement trouvé avec l'id " + id);
        }
    }

    @Override
    public List<Evenement> recuperer() throws SQLException {
        String sql = "select * from evenements";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Evenement> evenements = new ArrayList<>();
        while (rs.next()) {
            Evenement e = new Evenement();
            e.setId(rs.getInt("id"));
            e.setNom(rs.getString("nom"));
            e.setDate(rs.getDate("date").toLocalDate());
            e.setHeure(rs.getTime("heure").toLocalTime());
            e.setDescription(rs.getString("description"));
            e.setGalerieID(rs.getInt("GalerieID")); // Utilisez setGalerieID pour définir la valeur de la propriété GalerieID
            evenements.add(e);
        }
        return evenements;
    }


    public Evenement rechercherEvenementParNom(String nom) throws SQLException {
        String sql = "SELECT * FROM evenements WHERE nom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, nom);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Evenement evenement = new Evenement();
            evenement.setId(resultSet.getInt("id"));
            evenement.setNom(resultSet.getString("nom"));
            evenement.setDate(resultSet.getDate("date").toLocalDate());
            evenement.setHeure(resultSet.getTime("heure").toLocalTime());
            evenement.setDescription(resultSet.getString("description"));
            return evenement;
        } else {
            return null; // Aucun événement trouvé avec ce nom
        }
    }

    public Evenement getAll () {
        return null;
    }
}
