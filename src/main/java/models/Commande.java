package models;

import java.time.LocalDate;
import java.util.Date;
import java.time.LocalDate;

public class Commande {

    private int idC, numTel;

    private String adresseLivraison;





    // Constructeur par défaut
    public Commande() {
    }

    // Constructeur avec tous les attributs
    public Commande(int idC, String adresseLivraison, int numTel) {
        this.idC = idC;
        this.numTel = numTel;
        this.adresseLivraison = adresseLivraison;

    }

    public Commande(int numTel, String adresseLivraison) {
        this.numTel = numTel;
        this.adresseLivraison = adresseLivraison;

    }

    // Getters et setters


    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public int getNumTel() {
        return numTel;
    }

    public void setNumTel(int numTel) {
        this.numTel = numTel;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }



    @Override
    public String toString() {
        return "Commande{" +
                "idC=" + idC +
                ", numTel=" + numTel +
                ", adresseLivraison='" + adresseLivraison + '\'' +

                '}';
    }
}

