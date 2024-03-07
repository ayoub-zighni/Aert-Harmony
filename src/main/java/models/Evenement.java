package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
public class Evenement {
    private int id;
    private String nom;
    private LocalDate date;
    private LocalTime heure;
    private String description;
    private int GalerieID;

    public Evenement(int id, String nom, LocalDate date, LocalTime heure, String description, int GalerieID) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.heure = heure;
        this.description = description;
        this.GalerieID = GalerieID;
    }

    public Evenement(String nom, String date, String heure, String description, int GalerieID) {
        this.nom = nom;
        this.date = LocalDate.parse(date);
        this.heure = LocalTime.parse(heure);
        this.description = description;
        this.GalerieID = GalerieID;
    }

    public Evenement(String nom, LocalDateTime date, LocalDateTime heure, String description, int GalerieID) {
        this.nom = nom;
        this.date = date.toLocalDate();
        this.heure = heure.toLocalTime();
        this.description = description;
        this.GalerieID = GalerieID;
    }

    public Evenement() {

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGalerieID() {
        return GalerieID;
    }

    public void setGalerieID(int GalerieID) {
        this.GalerieID = GalerieID;
    }

    @Override
    public String toString() {
        return "Evenements{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", date=" + date +
                ", heure=" + heure +
                ", description='" + description + '\'' +
                ", GalerieID=" + GalerieID +
                '}';
    }


}
