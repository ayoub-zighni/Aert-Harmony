package test;

import models.Evenement;
import services.EvenementService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.SQLException;
public class Main {


    public static void main(String[] args) {
        EvenementService ps = new EvenementService();

        try {
            ps.ajouter(new Evenement(13,"foulen",LocalDate.now(), LocalTime.now(), "Description de l'événement",1));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        try {
            ps.modifier(new Evenement("foulen", "LocalDate.now()", "LocalTime.now()", "Nouvelle description de l'événement",1));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            System.out.println(ps.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}
