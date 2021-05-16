package fr.ul.miage.Restaurant.Resto;

public class Mp {
    private Integer idmp;
    private String nommp;
    private Integer stockmp;

    public Mp(Integer idmp, String nommp, Integer stockmp) {
        this.idmp = idmp;
        this.nommp = nommp;
        this.stockmp = stockmp;
    }

    public Mp(int idmp, int stockmp) {
        this.idmp = idmp;
        this.stockmp = stockmp;
    }

    public Integer getIdmp() {
        return this.idmp;
    }

    public String getNommp() {
        return this.nommp;
    }

    public Integer getStockmp() {
        return this.stockmp;
    }

    @Override
    public String toString() {
        return "Nom de la matière première : " + this.nommp;

    }
}
