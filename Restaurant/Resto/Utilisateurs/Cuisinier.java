package Restaurant.Resto.Utilisateurs;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cuisinier extends Utilisateur {

    public Cuisinier(String id) {
        super(id);
    }

    /**
     * Méthode pour afficher l'écran pricipal du cuisinier
     *
     * @return choix du cuisinier
     */
    @Override
    public Integer afficherPrincipal() {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue dans la cuisine " + n
                + "--------------------------------------" + n + "1. Définir un nouveau plat" + n
                + "2. Finir une commande" + n + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, 3)) {
                System.out.println("Entrée non valide");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        scan.close();
        return rep;
    }
}
