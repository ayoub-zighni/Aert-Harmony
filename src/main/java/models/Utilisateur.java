package models;

import java.time.LocalDate;

public class Utilisateur {
    int id;
    String prenom,nom,username, email, mdp, description,numtel;

    private String verificationCode;


    //String , img;
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
        return role != null ? role : Role.BROWSER;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    /*public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }*/
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    // Method to get the verification code
    public String getVerificationCode() {
        return verificationCode;
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

    public Utilisateur(String prenom, String nom, String username, String email, String mdp, LocalDate dateNaissance, Role role) {
        this.prenom = prenom;
        this.nom = nom;
        this.username = username;
        this.email = email;
        this.mdp = mdp;
        DateNaissance = dateNaissance;
        this.role = role;
    }

    public Utilisateur(String prenom, String nom, String username, String email, String mdp, String description, LocalDate dateNaissance, Role role) {
        this.prenom = prenom;
        this.nom = nom;
        this.username = username;
        this.email = email;
        this.mdp = mdp;
        this.description = description;
        DateNaissance = dateNaissance;
        this.role = role;
    }

    public Utilisateur(String prenom, String nom, String username, String email, String mdp, String description, String numtel, LocalDate dateNaissance, Role role) {
        this.prenom = prenom;
        this.nom = nom;
        this.username = username;
        this.email = email;
        this.mdp = mdp;
        this.description = description;
        this.numtel = numtel;
        DateNaissance = dateNaissance;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                ", description='" + description + '\'' +
                ", numtel='" + numtel + '\'' +
                ", DateNaissance=" + DateNaissance +
                ", role=" + role +
                '}';
    }
}
