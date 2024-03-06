package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.categorie;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicesCategorie  implements  IServicesCategorie{
    Connection cnx = DataSource.getInstance().getConnection();
    ObservableList<categorie> obList = FXCollections.observableArrayList();
    @Override
    public String ajouterCategorie(categorie c) {
        try {
            String req = "INSERT INTO `categorie`( `nom`) VALUES ('" + c.getNom() + "')";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Category Added successfully!");
        } catch (SQLException ex) {
            System.out.println("failed!");
        }
        return c.getNom();
    }

    @Override
    public List<categorie> afficherCategorie() {


        List<categorie> categorie = new ArrayList<>();
        //1
        String req = "SELECT * FROM categorie";
        try {
            //2
            Statement st = cnx.createStatement();
            //3
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                categorie E = new categorie();
                E.setIdcat(rs.getInt("idcat"));
                E.setNom(rs.getString("nom"));
                categorie.add(E);
            }


        } catch (SQLException ex) {
            Logger.getLogger(ServicesCategorie.class.getName()).log(Level.SEVERE, null, ex);
        }

        return categorie;
        }

    @Override
    public void modifierCategorie(categorie c) {
        try {
            String req = "UPDATE `categorie` SET `nom`=?  WHERE idcat=?";
            PreparedStatement st = cnx.prepareStatement(req);

            st.setString(1, c.getNom());
            st.setInt(2, c.getIdcat());

            System.out.println("Requête SQL pour la modification : " + st.toString()); // Ajoutez cette ligne pour le débogage

            st.executeUpdate();

            System.out.println("Catégorie modifiée avec succès");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void supprimerCategorie(int id) {

        try {
            String req = "DELETE FROM `categorie` WHERE idcat=?";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, id);
            st.executeUpdate();
            System.out.println("categorie supprimer avec succès");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public ObservableList<categorie> afficherCategory2() {
        String sql = "SELECT * FROM categorie";
        try {
            Statement statement = cnx.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt(1);
                String categoryName = result.getString(2);
                categorie c = new categorie(id, categoryName);
                obList.add(c);
            }
            result.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return obList;
    }

    public int getIdCategorieByName(String categoryName) {
        int id = -1;
        String sql = "SELECT idcat FROM categorie WHERE nom = ?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, categoryName);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    id = result.getInt("idcat");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id;
    }

}
