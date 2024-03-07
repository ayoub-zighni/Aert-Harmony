package services;
        import models.Produits;

        import java.util.List;

public interface ISercivesProduit {
    public void ajouterProduit(Produits c);
    public List<Produits> afficherProduit();
    public void modifierProduit(Produits c);

    public void supprimerProduit(int id);
}