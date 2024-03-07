package services;
import models.Commande;
import models.Livreur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CommandeService implements IServeceee<Commande> {

    private Connection connection;

    public CommandeService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Commande commande) throws SQLException {

    }

    @Override
    public void modifier(Commande commande) throws SQLException {

    }

    @Override
    public void supprimer(String nom) throws SQLException {
    }

    @Override
    public List<Livreur> rechercher(String Nom) throws SQLException {
        return null;
    }

    @Override
    public void ajouterCo(Commande commande) throws SQLException {
        String sql = "INSERT INTO commande (adresseLivraison,numTel) " + "VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, commande.getAdresseLivraison());
            preparedStatement.setString(2, String.valueOf(commande.getNumTel()));
            preparedStatement.executeUpdate();

            // Retrieve the generated keys (if any)
            // ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            // if (generatedKeys.next()) {
            //     int id = generatedKeys.getInt(1);
            //     commande.setId(id);
            // }
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw e; // Re-throwing the exception for handling at a higher level
        }
    }


    @Override
    public void ajouterC(Commande commande) throws SQLException {

    }

    @Override
    public void modifierC(Commande commande) throws SQLException {
        String sql = "UPDATE commande SET adresseLivraison = ?, numTel = ? WHERE idC = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //    preparedStatement.setInt(1, commande.getIdC());
            preparedStatement.setString(1, commande.getAdresseLivraison());
            preparedStatement.setInt(2, commande.getNumTel());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw e; // Re-throwing the exception for handling at a higher level
        }


    }

    @Override
    public void supprimerC(String id) throws SQLException {
        String sql = "DELETE FROM commande WHERE idC = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(id));
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Commande> rechercherC(int id) throws SQLException {
        String req = "SELECT * FROM commande WHERE idC LIKE ?";
        List<Commande> commandes = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Commande c = new Commande();
                    c.setIdC(rs.getInt("idC"));

                    c.setAdresseLivraison(rs.getString("adresseLivraison"));
                    c.setNumTel(rs.getInt("numTel"));
                    // c.setId(rs.getInt("id"));
                    commandes.add(c);
                }
            }
        }
        return commandes;
    }

    @Override
    public List<Livreur> Search(String t) throws SQLException {
        return null;
    }

    @Override
    public List<Commande> Search(int IdSearch) throws SQLException {
        List<Commande> list1 = new ArrayList<>();
        List<Commande> list2 = recuperer();
        list1 = (list2.stream().filter(c -> Integer.toString(c.getIdC()).startsWith(Integer.toString(IdSearch)))
                .collect(Collectors.toList()));
        return list1;
    }

    @Override
    public List<Commande> recuperer() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM commande";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Commande c = new Commande();
                c.setIdC(rs.getInt("idC"));
                c.setAdresseLivraison(rs.getString("adresseLivraison"));
                c.setNumTel(rs.getInt("numTel"));
                //   c.setId(rs.getInt("id"));
                commandes.add(c);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        System.out.println("Affichage effectué");
        return commandes;
    }

}