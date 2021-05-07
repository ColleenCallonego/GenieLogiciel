package fr.ul.miage.Restaurant.Resto;

public class Categorie {
    private Integer idcategorie;
    private String nomcategorie;

    public Categorie(Integer idcategorie, String nomcategorie) {
        this.idcategorie = idcategorie;
        this.nomcategorie = nomcategorie;
    }

    public Integer getIdcategorie() {
        return this.idcategorie;
    }

    public String getNomcategorie() {
        return this.nomcategorie;
    }

    @Override
    public String toString() {
        return "Nom de la catégorie : " + this.nomcategorie;

    }
}
