package test;

import models.Livreur;
import models.Livreur;
import services.LivreurService;

import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {
        LivreurService ls = new LivreurService();

        try {
            ls.ajouter(new Livreur("xxxx", "Ben Foulen", "Foulen"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

     //   ls.modifier(new Livreur("imed", "Test", "Test"));


        try {
            System.out.println(ls.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
    }
