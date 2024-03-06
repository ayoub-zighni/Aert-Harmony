package models;

public class produits {

 private int   id ;
 private String nom;
 private String description ;
 private String image ;
 private int prix ;
 private String stock;
 private int idcategorie;

 public produits() {
 }

 public produits(int id, String nom, String description, String image, int prix, String stock, int idcategorie) {
  this.id = id;
  this.nom = nom;
  this.description = description;
  this.image = image;
  this.prix = prix;
  this.stock = stock;
  this.idcategorie = idcategorie;
 }

 public produits(String nom, String description, String image, int prix, String stock, int idcategorie) {
  this.nom = nom;
  this.description = description;
  this.image = image;
  this.prix = prix;
  this.stock = stock;
  this.idcategorie = idcategorie;
 }

 @Override
 public String toString() {
  return "produits{" +
          "id=" + id +
          ", nom='" + nom + '\'' +
          ", description='" + description + '\'' +
          ", image='" + image + '\'' +
          ", prix=" + prix +
          ", stock='" + stock + '\'' +
          ", idcategorie=" + idcategorie +
          '}';
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

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public String getImage() {
  return image;
 }

 public void setImage(String image) {
  this.image = image;
 }

 public int getPrix() {
  return prix;
 }

 public void setPrix(int prix) {
  this.prix = prix;
 }

 public String getStock() {
  return stock;
 }

 public void setStock(String stock) {
  this.stock = stock;
 }

 public int getIdcategorie() {
  return idcategorie;
 }

 public void setIdcategorie(int idcategorie) {
  this.idcategorie = idcategorie;
 }
}
