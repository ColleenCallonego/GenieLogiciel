package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.Table;
import fr.ul.miage.Restaurant.Resto.misc.GestionBDD;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import static fr.ul.miage.Restaurant.Resto.Main.scan;

public class MaitreH extends Utilisateur {
    public ArrayList<Table> listTables;
    public ArrayList<String> listServeur;

    public MaitreH(String id) {
        super(id);
        listTables = new ArrayList<>();
        listServeur = new ArrayList<>();
    }

    /**
     * Méthode pour afficher l'écran pricipal du maitre d'hotel
     *
     * @return choix du maitre d'hotel
     */
    @Override
    public Integer afficherPrincipal() {
        int rep = -1;

        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue M. le Maitre d'Hôtel " + n
                + "--------------------------------------" + n + "1. Affecter un serveur à une table" + n
                + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, 2)) {
                System.out.println("Entrée non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }

        return rep;
    }

    @Override
    public void appelMethode(Integer num) {
        switch (num) {
            case 1:
                affectationTable();
                break;
        }
    }

    public void recupTables(Connection conn) {
        listTables = new ArrayList<>();
        try {
            ResultSet rs = GestionBDD.executeSelect(conn,"SELECT * FROM tableresto ORDER BY numero");
            while(rs.next()) {
                listTables.add(new Table(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recupServeur(Connection conn) {
        listServeur = new ArrayList<>();
        try {
            ResultSet rs = GestionBDD.executeSelect(conn,"SELECT idutili FROM utilisateur WHERE typeutili = 'Serveur'");
            while(rs.next()) {
                listServeur.add(rs.getString(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void affectationTable(){
        //recuperer les tables
        recupTables(GestionBDD.connect());
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        //afficher les table
        for (Table table : listTables){
            System.out.println("\u001B[97m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
        }
        //demander de choisir une table
        System.out.println("--------------------------------------" + n + "Voici les différentes tables du restaurant" + n
                + "--------------------------------------" + n + "1-" + listTables.size() + ". Selectionner une table"
                + n + "0. Annuler");
        try {
            rep = scan.nextInt();
            if (!verif(rep, listTables.size() + 1)) {
                System.out.println("Entrée non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        if (rep > 0){
            int numTab = rep-1;
            //lorsque la table est selectionnée afficher les info de la table
            rep = -1;
            scan = new Scanner(System.in);
            System.out.println("--------------------------------------" + n + "Bienvenue à la table " + listTables.get(numTab).getNumero()
                    + n + "Nombre de couvert : " + listTables.get(numTab).getNbplace() + n + "Serveur assigné : " + listTables.get(numTab).getServer() + n + n + "--------------------------------------" + n
                    + "1. Changer le serveur assigné" + n + "0. Retourner à l'écran principal" + n + n + n + "Que voulez vous faire?");
            //demander de changer le serveur responsable de la table (oui ou non)
            try {
                rep = scan.nextInt();
                if (!verif(rep, 2)) {
                    System.out.println("Entrée non valide");
                    rep = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrée non valide");
                rep = -1;
            }
            //si oui
            if (rep == 1){
                //recuperer les serveurs
                recupServeur(GestionBDD.connect());
                //choisir un serveur changer dans la base
                rep = -1;
                scan = new Scanner(System.in);
                //afficher les serveurs
                for (String serveur : listServeur){
                    System.out.println("\u001B[97m" + "[" + serveur + "]" + "\u001B[0m");
                }
                //demander de choisir un serveur
                System.out.println("--------------------------------------" + n + "Voici les différentes serveurs du restaurant" + n
                        + "--------------------------------------" + n + "1-" + listServeur.size() + ". Selectionner un serveur"
                        + n + "0. Annuler");
                try {
                    rep = scan.nextInt();
                    if (!verif(rep, listServeur.size() + 1)) {
                        System.out.println("Entrée non valide");
                        rep = -1;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrée non valide");
                    rep = -1;
                }
                if (rep > 0){
                    String serveur = listServeur.get(rep-1);
                    try{
                        Connection conn = GestionBDD.connect();
                        Statement st = conn.createStatement();
                        GestionBDD.executeUpdate(conn,"UPDATE tableresto SET serveur = '" + serveur + "' WHERE numero = " + (numTab+1));
                        System.out.println("L'assignation a fonctionnée");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    System.out.println("Affectation annulée");
                }
            }
            else{
                System.out.println("Affectation annulée");
            }
        }
        else{
            System.out.println("Affectation annulée");
        }
    }

}
