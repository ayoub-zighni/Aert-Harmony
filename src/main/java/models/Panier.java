package models;

public class Panier {
    private int id_panier;
    private String nomProduit;
    private double prix;
    private String etatStock;
    private int quantite;

    public Panier() {
    }
    public void ajouter(String nomProduit, double prix, String etatStock, int quantite) {
        this.nomProduit = nomProduit;
        this.prix = prix;
        this.etatStock = etatStock;
        this.quantite = quantite;
    }

    public Panier(int id_panier,String nomProduit, double prix, String etatStock, int quantite) {
        this.id_panier=id_panier;
        this.nomProduit = nomProduit;
        this.prix = prix;
        this.etatStock = etatStock;
        this.quantite = quantite;
    }

    public int getId() {
        return id_panier;
    }

    public void setId(int id_panier) {
        this.id_panier = id_panier;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getEtatStock() {
        return etatStock;
    }

    public void setEtatStock(String etatStock) {
        this.etatStock = etatStock;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id_panier=" + id_panier +
                ", nomProduit='" + nomProduit + '\'' +
                ", prix=" + prix +
                ", etatStock='" + etatStock + '\'' +
                ", quantite=" + quantite +
                '}';
    }
}
