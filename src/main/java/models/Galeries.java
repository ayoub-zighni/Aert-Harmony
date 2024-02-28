package models;

public class Galeries {
    private static int nextId = 1; // Variable statique pour stocker le prochain identifiant à utiliser
    private int id;
    private String nom;
    private String adresse;
    private String ville;
    private String pays;
    private int capaciteMax;

    public Galeries(int id, String nom, String adresse, String ville, String pays, int capaciteMax) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.pays = pays;
        this.capaciteMax = capaciteMax;
    }

    public Galeries() {
        this.id = generateId(); // Utilisez la méthode generateId pour obtenir un nouvel identifiant unique
    }

    public Galeries(String nom, String adresse, String ville, String pays, int capaciteMax) {

        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.pays = pays;
        this.capaciteMax = capaciteMax;
    }

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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    private static int generateId() {
        return nextId++; // Incrémente le prochain identifiant et retourne l'ancienne valeur
    }

    @Override
    public String toString() {
        return "Galeries{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", pays='" + pays + '\'' +
                ", capaciteMax=" + capaciteMax +
                '}';
    }
}
