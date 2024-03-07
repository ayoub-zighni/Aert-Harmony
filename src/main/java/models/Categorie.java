package models;

public class Categorie {

    private int idcat;
    private String nom;

    public Categorie(String nom) {
        this.nom = nom;
    }

    public Categorie() {
    }

    public Categorie(int idcat, String nom) {
        this.idcat = idcat;
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "categorie{" +
                "idcat=" + idcat +
                ", nom='" + nom + '\'' +
                '}';
    }

    public int getIdcat() {
        return idcat;
    }

    public void setIdcat(int idcat) {
        this.idcat = idcat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}