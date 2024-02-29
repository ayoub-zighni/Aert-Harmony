package services;

import models.Livreur;
import models.Commande;
import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    void ajouter(T t) throws SQLException;


    void modifier(T t) throws SQLException;


    void supprimer(String nom) throws SQLException;

    List<Livreur> rechercher(String Nom) throws SQLException;

    void ajouterC(Commande commande, int id) throws SQLException;

    void ajouterC(Commande commande) throws SQLException;

    void modifierC(Commande commande) throws SQLException;

    void supprimerC(String nom) throws SQLException;

    List<Commande> rechercherC(int id) throws SQLException;

    List<Livreur> Search(String t) throws SQLException;

    List<T> recuperer() throws SQLException;

}