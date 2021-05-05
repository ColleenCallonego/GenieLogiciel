package Restaurant.Resto;

public class Table {
    private int numero;
    private int nbplace;
    private String etattable;
    private String etatrepas;
    private int serveurId;

    public Table(int numero, int nbplace, String etattable, String etatrepas, int serveurId){
        this.numero = numero;
        this.nbplace = nbplace;
        this.etattable = etattable;
        this.etatrepas = etatrepas;
        this.serveurId = serveurId;
    }

    public int getNumero() {
        return numero;
    }

    public int getServer() {
        return serveurId;
    }

    public String getEtattable(){
        return etattable;
    }
}