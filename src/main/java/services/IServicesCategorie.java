package services;

import models.Categorie;

import java.util.List;

public interface IServicesCategorie {

    public String ajouterCategorie(Categorie c);
    public List<Categorie> afficherCategorie();
    public void modifierCategorie(Categorie c);

    public void supprimerCategorie(int id);
}
