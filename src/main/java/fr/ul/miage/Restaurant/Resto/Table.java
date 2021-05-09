package fr.ul.miage.Restaurant.Resto;

public class Table {
    private int numero;
    private int nbplace;
    private String etattable;
    private String etatrepas;
    private String serveurId;

    public Table(int numero, int nbplace, String etattable, String etatrepas, String serveurId){
        this.numero = numero;
        this.nbplace = nbplace;
        this.etattable = etattable;
        this.etatrepas = etatrepas;
        this.serveurId = serveurId;
    }

    public int getNumero() {
        return numero;
    }

    public int getNbplace() {
        return nbplace;
    }

    public String getEtattable(){
        return etattable;
    }

    public String getEtatrepas(){
        return etatrepas;
    }

    public String getServer() {
        return serveurId;
    }

}