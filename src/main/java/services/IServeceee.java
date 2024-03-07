package services;

import models.Commande;
import models.Livreur;

import java.sql.SQLException;
import java.util.List;

public interface IServeceee<T> {
    void ajouter(T t) throws SQLException;


    void modifier(T t) throws SQLException;


    void supprimer(String nom) throws SQLException;

    List<Livreur> rechercher(String Nom) throws SQLException;

    void ajouterCo(Commande commande) throws SQLException;

    void ajouterC(Commande commande) throws SQLException;

    void modifierC(Commande commande) throws SQLException;

    void supprimerC(String nom) throws SQLException;

    List<Commande> rechercherC(int id) throws SQLException;

    List<Livreur> Search(String t) throws SQLException;

    List<Commande> Search(int IdSearch) throws SQLException;

    List<T> recuperer() throws SQLException;

}
