package Restaurant.Resto.Utilisateurs;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Directeur extends Utilisateur{

    public Directeur(String id){
        super(id);
    }

    /**
     * Méthode pour afficher l'écran principal du Directeur. Sont proposés toutes
     * les actions possibles du directeur.
     *
     * @return le choix du directeur
     */
    @Override
    public Integer afficherPrincipal() {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "   Bienvenue M. le Directeur   " + n
                + "--------------------------------------" + n + "1. Gestion des employés" + n
                + "2. Gestion de la carte du jour" + n + "3. Gestion des stocks de matières premières" + n
                + "4. Analyser les ventes" + n + "5. Popularité des plats" + n
                + "6. Connaitre le temps de rotation moyen" + n + "7. Connaitre le temps de préparatiion moyen" + n
                + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verifDirPrinc(rep)) {
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
     * Méthode pour vérifier que le choix de directeur est possible
     *
     * @param entree choix du directeur
     * @return true si le choix existe et false sinon
     */
    public static Boolean verifDirPrinc(int entree) {
        ArrayList<Integer> possibilite = new ArrayList<Integer>();
        possibilite.add(0);
        possibilite.add(1);
        possibilite.add(2);
        possibilite.add(3);
        possibilite.add(4);
        possibilite.add(5);
        possibilite.add(6);
        possibilite.add(7);
        return possibilite.contains(entree);
    }

}