package Restaurant.Resto.Utilisateurs;

public abstract class Utilisateur {
    protected String id;

    public Utilisateur(String id){
        this.id = id;
    }

    public abstract Integer afficherPrincipal();


}
