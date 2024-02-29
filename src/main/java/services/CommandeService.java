package services;
import models.Commande;
import models.Livreur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CommandeService implements IService<Commande> {

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
    public void ajouterC(Commande commande,int id) throws SQLException {
        String sql = "INSERT INTO commande (dateCommande, statutCommande, adresseLivraison, id) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setDate(1, new java.sql.Date(commande.getDateCommande().getTime()));
        preparedStatement.setString(2, commande.getStatutCommande());
        preparedStatement.setString(3, commande.getAdresseLivraison());
        preparedStatement.setInt(4, commande.getId()); // Supposant que getIdLivreur() retourne l'ID du livreur associé à cette commande

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Échec de l'insertion de la commande.");
        }

        // Récupérer la valeur de l'ID auto-incrémenté
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int idC = generatedKeys.getInt(1);
            commande.setIdC(idC);
        } else {
            throw new SQLException("Échec de récupération de l'ID auto-incrémenté.");
        }

        preparedStatement.close(); // Fermer le preparedStatement
    }

    @Override
    public void ajouterC(Commande commande) throws SQLException {

    }

    @Override
    public void modifierC(Commande commande) throws SQLException {
        String sql = "UPDATE commande SET dateCommande = ?, statutCommande = ?, adresseLivraison = ?, id = ? WHERE idC = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, new java.sql.Date(commande.getDateCommande().getTime()));
        preparedStatement.setString(2, commande.getStatutCommande());
        preparedStatement.setString(3, commande.getAdresseLivraison());
        preparedStatement.setInt(4, commande.getId());
        preparedStatement.setInt(5, commande.getIdC());

        preparedStatement.executeUpdate();
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
            pst.setInt(1, Integer.parseInt("%" + id + "%"));
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Commande c = new Commande();
                    c.setIdC(rs.getInt("idC"));
                    c.setDateCommande(rs.getDate("dateCommande"));
                    c.setStatutCommande(rs.getString("statutCommande"));
                    c.setAdresseLivraison(rs.getString("adresseLivraison"));
                    c.setId(rs.getInt("id"));
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
    public List<Commande> recuperer() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM commande";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Commande c = new Commande();
                c.setIdC(rs.getInt("idC"));
                c.setDateCommande(rs.getDate("dateCommande"));
                c.setStatutCommande(rs.getString("statutCommande"));
                c.setAdresseLivraison(rs.getString("adresseLivraison"));
                c.setId(rs.getInt("id"));
                commandes.add(c);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        System.out.println("Affichage effectué");
        return commandes;
    }
}