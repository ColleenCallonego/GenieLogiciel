package Restaurant.Resto.Utilisateurs;

import java.util.ArrayList;

public abstract class Utilisateur {
    protected String id;

    public Utilisateur(String id) {
        this.id = id;
    }

    public abstract Integer afficherPrincipal();

    public static Boolean verif(int entree, int nbChoix) {
        ArrayList<Integer> possibilite = new ArrayList<Integer>();
        for (int i = 0; i < nbChoix; i++) {
            possibilite.add(i);
        }
        return possibilite.contains(entree);
    }

}
