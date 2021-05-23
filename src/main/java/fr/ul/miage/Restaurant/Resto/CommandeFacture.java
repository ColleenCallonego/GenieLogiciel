package fr.ul.miage.Restaurant.Resto;

import java.util.Date;

public class CommandeFacture {
    private int idCommande;
    private Date dateCommande;
    private String nomPlatCommande;
    private int prixPlatCommande;

    public CommandeFacture(int idCommande, Date dateCommande, String nomPlatCommande, int prixPlatCommande){
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.nomPlatCommande = nomPlatCommande;
        this.prixPlatCommande = prixPlatCommande;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public String getNomPlatCommande() {
        return nomPlatCommande;
    }

    public int getPrixPlatCommande() {
        return prixPlatCommande;
    }
}
