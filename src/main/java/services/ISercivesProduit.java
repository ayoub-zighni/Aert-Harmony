package services;
        import models.produits;

        import java.util.List;

public interface ISercivesProduit {
    public void ajouterProduit(produits c);
    public List<produits> afficherProduit();
    public void modifierProduit(produits c);

    public void supprimerProduit(int id);
}