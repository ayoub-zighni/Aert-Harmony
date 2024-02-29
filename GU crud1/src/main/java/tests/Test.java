package tests;
import models.Personne;
import services.PersonneServices;
import utils.MyDataBase;

import java.sql.SQLException;

public class Test {
    public static void main (String[] args) {
        PersonneServices PersonneServices= new PersonneServices();
        Personne p1 = new Personne("ahmed","ahmed.mezni@esprit.tn","client","ghhh");
        Personne p2= new Personne("nourhene","belhouane","hguyfuy","hhffff");
        Personne p3= new Personne("hiuigig","hhhhhhhhh","zeiri","hhhhhh");
        try {
           /*PersonneServices.ajouter(p1);
           PersonneServices.ajouter(p2);*/
           PersonneServices.ajouter(p3);
            p3.setNom("Updated Name");
            p3.setEmail("update1d@example.com");
            p3.setMdp("newpassword");
            p3.setRole("updated role");
            p3.setId(11);

            PersonneServices.modifier(p3);
           //PersonneServices.supprimer(7);
           System.out.println(PersonneServices.afficher());
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}

