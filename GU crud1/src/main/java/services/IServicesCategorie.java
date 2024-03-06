package services;

import models.categorie;

import java.util.List;

public interface IServicesCategorie {

    public String ajouterCategorie(categorie c);
    public List<categorie> afficherCategorie();
    public void modifierCategorie(categorie c);

    public void supprimerCategorie(int id);
}
