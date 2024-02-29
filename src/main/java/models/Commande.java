package models;

import java.time.LocalDate;
import java.util.Date;

public class Commande {

    private int idC;

    private Date dateCommande;

    private String statutCommande;

    private String adresseLivraison;

    int id;


    // Constructeur par défaut
    public Commande() {
    }

    // Constructeur avec tous les attributs
    public Commande(int idC, Date dateCommande, String statutCommande, String adresseLivraison, int id) {
        this.idC = idC;
        this.dateCommande = dateCommande;
        this.statutCommande = statutCommande;
        this.adresseLivraison = adresseLivraison;
        this.id = id;
    }

    public Commande( Date dateCommande, String statutCommande, String adresseLivraison, int id) {

        this.dateCommande = dateCommande;
        this.statutCommande = statutCommande;
        this.adresseLivraison = adresseLivraison;
        this.id = id;
    }

    public Commande(LocalDate value, String text, String text1, int id) { 
    }

    // Getters et setters
    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatutCommande() {
        return statutCommande;
    }

    public void setStatutCommande(String statutCommande) {
        this.statutCommande = statutCommande;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}




