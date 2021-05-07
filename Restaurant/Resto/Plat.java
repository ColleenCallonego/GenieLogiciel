package Restaurant.Resto;

public class Plat {
    private Integer idplat;
    private String nomplat;
    private Integer prixplat;
    private String cat;

    public Plat(Integer idplat, String nomplat, Integer prixplat, String cat) {
        this.idplat = idplat;
        this.nomplat = nomplat;
        this.prixplat = prixplat;
        this.cat = cat;
    }

    public Integer getIdplat() {
        return this.idplat;
    }

    public String getNomplat() {
        return this.nomplat;
    }

    public Integer getPrixplat() {
        return this.prixplat;
    }

    public String getCat() {
        return this.cat;
    }

    @Override
    public String toString() {
        return "Nom du plat : " + this.nomplat + " | Prix du plat : " + this.prixplat + " | Catégorie du plat : "
                + this.cat;

    }

    public Boolean compareNom(String nom){
        return this.nomplat.equals(nom);
    }
}
