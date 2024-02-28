package test;

import models.categorie;
import services.ServicesCategorie;

public class Main {
    public static void main(String[] args) {
        ServicesCategorie s1 = new ServicesCategorie();
        //categorie cat = new categorie(1,"fogjfg");
  //   System.out.println(s1.afficherCategorie());
        s1.supprimerCategorie(1);
  //      s1.ajouterCategorie(cat);
    }

}