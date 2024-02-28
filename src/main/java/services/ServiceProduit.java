package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.categorie;
import models.produits;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        } catch (SQLException ex) {
            Logger.getLogger(ServiceProduit.class.getName()).log(Level.SEVERE, null, ex);
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
