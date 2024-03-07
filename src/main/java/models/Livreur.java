package models;

public class Livreur {
    private int id;
    private String nom, prenom , region;

    private String email;

    private int telephone;

    public Livreur(int id, String nom, String prenom, String region, String email, int telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.region = region;
        this.email=email;
        this.telephone=telephone;
    }

    public Livreur( String nom, String prenom , String region ) {
        this.nom = nom;
        this.prenom = prenom;
        this.region = region;
        this.email=email;
        this.telephone=telephone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Livreur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", region='" + region + '\'' +
                ", email='" + email + '\'' +
                ", telephone=" + telephone +
                '}';
    }
}
