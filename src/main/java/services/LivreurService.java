package services;

import models.Commande;
import models.Livreur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class LivreurService implements IService<Livreur> {

    private Connection connection;

    public LivreurService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Livreur livreur) throws SQLException {
        String sql = "INSERT INTO livreur (nom, prenom, region) " +
                "VALUES ('" + livreur.getNom() + "', '" + livreur.getPrenom() + "', '" + livreur.getRegion() + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    public void modifier(Livreur livreur) throws SQLException {
        String sql = "UPDATE livreur SET nom = ?, prenom = ?, region = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, livreur.getNom());
        preparedStatement.setString(2, livreur.getPrenom());
        preparedStatement.setString(3, livreur.getRegion());
        preparedStatement.setInt(4, livreur.getId()); // Utilisez le nom actuel du livreur comme critère de mise à jour

        preparedStatement.executeUpdate();
    }
    @Override
    public void supprimer(String nom) throws SQLException {
        String sql = "delete from livreur where nom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, nom);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Livreur> rechercher(String Nom) throws SQLException {
        String req = "SELECT * FROM livreur WHERE nom LIKE ?";
        List<Livreur> livreurs = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, "%" + Nom + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Livreur l = new Livreur();
                    l.setId(rs.getInt("id"));
                    l.setNom(rs.getString("nom"));
                    l.setPrenom(rs.getString("prenom"));
                    l.setRegion(rs.getString("region"));
                    livreurs.add(l);
                }
            }
        }
        return livreurs;
    }

    @Override
    public void ajouterC(Commande commande, int id) throws SQLException {

    }

    @Override
    public void ajouterC(Commande commande) throws SQLException {

    }

    @Override
    public void modifierC(Commande commande) throws SQLException {

    }

    @Override
    public void supprimerC(String nom) throws SQLException {

    }

    @Override
    public List<Commande> rechercherC(int id) throws SQLException {
        return null;
    }


@Override
    public List<Livreur> Search(String t) throws SQLException {
        List<Livreur> list1 = new ArrayList<>();
        List<Livreur> list2 = recuperer();
        list1 = (list2.stream().filter(c -> c.getNom().startsWith(t)).collect(Collectors.toList()));

        return list1;
    }

    @Override
    public List<Livreur> recuperer() throws SQLException {
        List<Livreur> livreurs = new ArrayList<>();
        try {
            String sql = "select * from livreur";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Livreur p = new Livreur();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setRegion(rs.getString("region"));

                livreurs.add(p);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        System.out.println("affichage effectue");
        return livreurs;
    }

}