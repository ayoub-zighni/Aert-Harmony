package services;

import models.Role;
import models.Utilisateur;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService implements IService<Utilisateur> {

    Connection connection;

    public UtilisateurService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Utilisateur utilisateur) throws SQLException {

        String sql = "INSERT INTO user (prenom, nom, email, mdp, dateNaissance, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, utilisateur.getPrenom());
        ps.setString(2, utilisateur.getNom());
        ps.setString(3, utilisateur.getEmail());
        ps.setString(4, utilisateur.getMdp());
        ps.setObject(5, utilisateur.getDateNaissance()); // Utiliser setObject pour LocalDate
        ps.setString(6, utilisateur.getRole().toString()); // Récupérer le rôle en tant que chaîne de caractères
        ps.executeUpdate();
    }

    @Override
    public void modifier(Utilisateur utilisateur) throws SQLException {
        String req = "UPDATE user SET prenom=?, nom=?, email=?, mdp=?, role=?, DateNaissance=? WHERE id=?";
        PreparedStatement ut = connection.prepareStatement(req);
        ut.setString(1, utilisateur.getPrenom());
        ut.setString(2, utilisateur.getNom());
        ut.setString(3, utilisateur.getEmail());
        ut.setString(4, utilisateur.getMdp());
        ut.setString(5, utilisateur.getRole().toString());
        ut.setObject(6, utilisateur.getDateNaissance());
        ut.setInt(7, utilisateur.getId());

        ut.executeUpdate();
        System.out.println("Utilisateur modifié.");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM user WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, id);
        int rowsDeleted = ps.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("Utilisateur avec l'ID " + id + " supprimé avec succès.");
        } else {
            System.out.println("Aucun utilisateur trouvé avec l'ID " + id + ".");
        }
    }

    @Override
    public List<Utilisateur> afficher() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setMdp(rs.getString("mdp"));
                utilisateur.setDateNaissance(rs.getObject("dateNaissance", LocalDate.class));
                String roleName = rs.getString("role"); // Retrieve the role name from the result set
                Role role = Role.fromString(roleName != null ? roleName : "BROWSER");
                utilisateur.setRole(role); // Set the parsed role to the utilisateur
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException ex) {
            // Handle or log the exception appropriately
            throw ex; // Re-throwing the exception for now
        }

        return utilisateurs;
    }


    public void clearUserData() throws SQLException {
        String req = "DELETE FROM user";
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate(req);

        System.out.println(rowsAffected + " lignes supprimées de la table utilisateur.");
    }

    public Utilisateur login(String email, String mdp) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ? AND mdp = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, mdp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setMdp(rs.getString("mdp"));
                    utilisateur.setDateNaissance(rs.getObject("dateNaissance", LocalDate.class));
                    String roleName = rs.getString("role");
                    Role role = Role.fromString(roleName != null ? roleName : "BROWSER");
                    utilisateur.setRole(role);
                    return utilisateur; // Return the utilisateur if login is successful
                }
            }
        } catch (SQLException ex) {
            // Handle or log the exception appropriately
            throw ex; // Re-throwing the exception for now
        }
        return null; // Return null if login fails
    }
    // Update the password for the user with the given email
    public void changerMotDePasse(String email, String ancienMotDePasse, String nouveauMotDePasse) throws SQLException {
        // First, check if the user with the given email exists
        Utilisateur utilisateur = getUserByEmail(email);
        if (utilisateur != null) {
            // If the user exists, verify the old password
            if (utilisateur.getMdp().equals(ancienMotDePasse)) {
                // If the old password matches, update the password
                String sql = "UPDATE user SET mdp = ? WHERE email = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, nouveauMotDePasse);
                    ps.setString(2, email);
                    int rowsUpdated = ps.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Mot de passe mis à jour avec succès pour l'utilisateur avec l'email: " + email);
                    } else {
                        System.out.println("Échec de la mise à jour du mot de passe pour l'utilisateur avec l'email: " + email);
                    }
                }
            } else {
                // If the old password is incorrect
                System.out.println("Échec de la mise à jour du mot de passe. Le mot de passe précédent est incorrect.");
            }
        } else {
            // If the user does not exist
            System.out.println("Échec de la mise à jour du mot de passe. L'utilisateur avec l'email " + email + " n'existe pas.");
        }
    }

    // Method to retrieve user by email
    private Utilisateur getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setMdp(rs.getString("mdp"));
                    utilisateur.setDateNaissance(rs.getObject("dateNaissance", LocalDate.class));
                    String roleName = rs.getString("role");
                    Role role = Role.fromString(roleName != null ? roleName : "BROWSER");
                    utilisateur.setRole(role);
                    return utilisateur; // Return the utilisateur if found
                }
            }
        }
        return null; // Return null if user not found
    }}
