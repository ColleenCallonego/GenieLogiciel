package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MaitreH extends Utilisateur {

    public MaitreH(String id) {
        super(id);
    }

    /**
     * Méthode pour afficher l'écran pricipal du maitre d'hotel
     *
     * @return choix du maitre d'hotel
     */
    @Override
    public Integer afficherPrincipal() {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue M. le Maitre d'Hôtel " + n
                + "--------------------------------------" + n + "1. Affecter un serveur à une table" + n
                + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, 2)) {
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
