package test;

import models.Role;
import models.Utilisateur;
import services.UtilisateurService;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {


    public static void main(String[] args) {
        UtilisateurService ps = new UtilisateurService();

        try {
            ps.ajouter(new Utilisateur("prenom", "nom", "email","mdp", LocalDate.now(), Role.ARTIST));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        try {
            ps.modifier(new Utilisateur("prenom", "nom", "email", "mdp",LocalDate.now(), Role.ARTIST));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            System.out.println(ps.afficher());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}
