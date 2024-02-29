package models;

import java.time.LocalDate;

public class Utilisateur {
    int id;
    String prenom,nom, email, mdp;
    LocalDate DateNaissance;
    Role role;

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getDateNaissance() {
        return DateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        DateNaissance = dateNaissance;
    }


    public Utilisateur() {
    }

    public Utilisateur(int id, String prenom, String nom, String email, String mdp, LocalDate dateNaissance, Role role) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.mdp = mdp;
        DateNaissance = dateNaissance;
        this.role = role;
    }



    public Utilisateur(String prenom, String nom, String email, String mdp, LocalDate dateNaissance, Role role) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.mdp = mdp;
        DateNaissance = dateNaissance;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                ", role=" + role +
                ", DateNaissance=" + DateNaissance +
                '}';
    }

}
