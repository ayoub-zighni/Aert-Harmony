package models;

public class Personne {
    int id;
    String nom, email, mdp, role;

    public Personne() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Personne(int id, String nom, String email, String mdp, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
    }

    public Personne(String nom, String email, String mdp, String role) {
        this.nom = nom;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}


