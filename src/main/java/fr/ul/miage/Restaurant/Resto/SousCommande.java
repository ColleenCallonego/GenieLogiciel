package fr.ul.miage.Restaurant.Resto;

public class SousCommande {
    private Integer numeroSousCommande;
    private String nomplat;

    public SousCommande(Integer numeroSousCommande, String nomplat) {
        this.numeroSousCommande = numeroSousCommande;
        this.nomplat = nomplat;
    }

    public Integer getNumeroSousCommande() {
        return numeroSousCommande;
    }

    public String getNomplat() {
        return nomplat;
    }

    @Override
    public String toString() {
        return "Numéro de la commande : " + numeroSousCommande +
                " | Plat : " + nomplat;
    }

    public boolean sameNum(Integer rep) {
        return (this.numeroSousCommande == rep);
    }
}
