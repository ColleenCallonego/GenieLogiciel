package Restaurant.Resto;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    /**
     * Méthode pour afficher l'écran principal du Directeur. Sont proposés toutes
     * les actions possibles du directeur.
     * 
     * @return le choix du directeur
     */
    public static Integer EcranPrincipalDirecteur() {
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

    /**
     * Méthode pour afficher l'écran d'une table au fois que le serveur l'a choisie.
     * 
     * @param numTable  numéro de la table concernée
     * @param nbPlace   nombre de personne possible sur la table
     * @param etatTable etat de la table (réservée, libre...)
     * @param etatRepas etat du repas (entrée, plat...)
     * @return le choix du serveur
     */
    public static Integer EcranTableServeur(Integer numTable, Integer nbPlace, String etatTable, String etatRepas) {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue à la table " + numTable + n
                + "Nombre de couvert : " + nbPlace + n + "Etat de la table : " + etatTable + n + "Etat du repas : "
                + etatRepas + n + n + "--------------------------------------" + n + "1. Changer le status de la table"
                + n + "2. Ajouter un plat" + n + "3. Obtenir la facture" + n + "0. Retourner à l'écran principal" + n
                + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verifServTable(rep)) {
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
     * Méthode pour vérifier que le choix de serveur est possible
     * 
     * @param entree choix du serveur
     * @return true si le choix existe et false sinon
     */
    public static Boolean verifServTable(int entree) {
        ArrayList<Integer> possibilite = new ArrayList<Integer>();
        possibilite.add(0);
        possibilite.add(1);
        possibilite.add(2);
        possibilite.add(3);
        return possibilite.contains(entree);
    }

    /**
     * Méthode pour afficher l'écran pricipal du cuisinier
     * 
     * @return choix du cuisinier
     */
    public static Integer EcranPrincipalCuisinier() {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue dans la cuisine " + n
                + "--------------------------------------" + n + "1. Définir un nouveau plat" + n
                + "2. Finir une commande" + n + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verifServTable(rep)) {
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
     * Méthode pour vérifier que le choix du cuisinier est possible
     * 
     * @param entree choix du cuisinier
     * @return true si le choix existe et false sinon
     */
    public static Boolean verifCuisinier(int entree) {
        ArrayList<Integer> possibilite = new ArrayList<Integer>();
        possibilite.add(0);
        possibilite.add(1);
        possibilite.add(2);
        return possibilite.contains(entree);
    }

    /**
     * Méthode pour afficher l'écran pricipal du maitre d'hotel
     * 
     * @return choix du maitre d'hotel
     */
    public static Integer EcranPrincipalMaitreHotel() {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue M. le Maitre d'Hôtel " + n
                + "--------------------------------------" + n + "1. Affecter un serveur à une table" + n
                + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verifServTable(rep)) {
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

    /**
     * Méthode pour afficher l'écran d'une table au fois que l'assitant l'a choisie.
     * 
     * @param numero numéro de la table concernée
     * @return choix de l'assistant
     */
    public static Integer EcranTableAssitant(Integer numero) {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue à la table numéro " + numero + n
                + "--------------------------------------" + n + "1. Changer le status de la table" + n
                + "0. Retourner à l'écran principal" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verifServTable(rep)) {
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
     * Méthode pour vérifier que le choix de l'assistant est possible
     * 
     * @param entree choix de l'assitant
     * @return true si le choix existe et false sinon
     */
    public static Boolean verifAssitant(int entree) {
        ArrayList<Integer> possibilite = new ArrayList<Integer>();
        possibilite.add(0);
        possibilite.add(1);
        return possibilite.contains(entree);
    }

}
