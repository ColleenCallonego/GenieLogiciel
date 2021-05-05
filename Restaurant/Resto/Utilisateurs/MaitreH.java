package Restaurant.Resto.Utilisateurs;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MaitreH extends Utilisateur{

    public MaitreH(String id){
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
            if (!verifMaitre(rep)) {
                System.out.println("Entrée non valide");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        scan.close();
        return rep;
    }

    /**
     * Méthode pour vérifier que le choix du maitre d'hotel est possible
     *
     * @param entree choix du cuisinier
     * @return true si le choix existe et false sinon
     */
    public static Boolean verifMaitre(int entree) {
        ArrayList<Integer> possibilite = new ArrayList<Integer>();
        possibilite.add(0);
        possibilite.add(1);
        return possibilite.contains(entree);
    }

}
