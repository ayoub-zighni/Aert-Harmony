package models;

public class Facture {
    private int id_fact;
    private String client;
    private String caissier;
    private double montant;
    private double percu;
    private double rendu;

    public Facture() {
    }

    public Facture(int id_fact ,String client, String caissier, double montant, double percu, double rendu) {
        this.id_fact = id_fact;
        this.client = client;
        this.caissier = caissier;
        this.montant = montant;
        this.percu = percu;
        this.rendu = rendu;
    }

    public int getId() {
        return id_fact;
    }

    public void setId(int id_fact) {
        this.id_fact = id_fact;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCaissier() {
        return caissier;
    }

    public void setCaissier(String caissier) {
        this.caissier = caissier;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getPercu() {
        return percu;
    }

    public void setPercu(double percu) {
        this.percu = percu;
    }

    public double getRendu() {
        return rendu;
    }

    public void setRendu(double rendu) {
        this.rendu = rendu;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "id_fact=" + id_fact +
                ", client='" + client + '\'' +
                ", caissier='" + caissier + '\'' +
                ", montant=" + montant +
                ", percu=" + percu +
                ", rendu=" + rendu +
                '}';
    }
}
