package models;

public class Livreur {
    private int id;
    private String nom, prenom , region;

    public Livreur(int id, String nom, String prenom, String region) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.region = region;
    }

    public Livreur( String nom, String prenom , String region ) {
        this.nom = nom;
        this.prenom = prenom;
        this.region = region;
    }

    public Livreur() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Livreur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
