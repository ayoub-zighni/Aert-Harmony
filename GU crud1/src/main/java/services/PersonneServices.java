package services;

import models.Personne;
import utils.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneServices implements IService<Personne> {
    Connection connection;

    public PersonneServices() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Personne personne) throws SQLException {
        // Check if a user with the same email already exists.
        String checkQuery = "SELECT COUNT(*) AS count FROM user WHERE email='" + personne.getEmail() + "'";
        Statement checkStatement = connection.createStatement();
        ResultSet resultSet = checkStatement.executeQuery(checkQuery);
        resultSet.next();
        int count = resultSet.getInt("count");

        if (count > 0) {
            System.out.println("A user with the same email already exists. Cannot add.");
            return; // Exit the method if a user with the same email exists
        }

        // If no user with the same email exists, proceed with adding the user
        String req = "INSERT INTO user (nom, email, mdp, role) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, personne.getNom());
        ps.setString(2, personne.getEmail());
        ps.setString(3, personne.getMdp());
        ps.setString(4, personne.getRole());

        ps.executeUpdate();
        System.out.println("Personne ajoutée.");
    }

    @Override
    public void modifier(Personne personne) throws SQLException {
       /* String req="update user set nom=? , email=? ,mdp=? ,role=? where id=?";
        PreparedStatement ps= connection.prepareStatement(req);
        ps.setString(1, personne.getNom());
        ps.setString(2, personne.getEmail());
        ps.setString(3, personne.getMdp());
        ps.setString(4, personne.getRole());
        ps.setInt(5, personne.getId());
        ps.executeUpdate();
        System.out.println("Personne modifie");*/

    }

        @Override
        public void supprimer ( int id) throws SQLException {
       /* String req = "DELETE FROM user WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, id);
        int rowsDeleted = ps.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("Personne with ID " + id + " deleted successfully.");
        } else {
            System.out.println("No personne found with ID " + id + ".");
        }*/
    }

            @Override
            public List<Personne> afficher () throws SQLException {
                List<Personne> personnes = new ArrayList<>();
                String req = "select * from user";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(req);
                while (rs.next()) {
                    Personne p = new Personne();
                    p.setId(rs.getInt(1));
                    p.setNom(rs.getString("nom"));
                    p.setEmail(rs.getString("email"));
                    p.setMdp(rs.getString("mdp"));
                    p.setRole(rs.getString("role"));
                    personnes.add(p);
                }
                return personnes;
            }
        }
    }


