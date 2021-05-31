package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.Table;
import fr.ul.miage.Restaurant.Resto.misc.GestionBDD;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static fr.ul.miage.Restaurant.Resto.Main.scan;

public class Assistant extends Utilisateur {
    private ArrayList<Table> listTables;

    public Assistant(String id) {
        super(id);
        listTables = new ArrayList<>();
    }

    @Override
    public Integer afficherPrincipal() {
        recupTables();
        int rep = -1;

        String n = System.getProperty("line.separator");
        for (Table table : listTables){
            if (table.getEtattable().equals("Débarrassée")){
                System.out.println("\u001B[31m" + "[Table " + table.getNumero() + " ]" + "\u001B[0m");
            }
            else if (table.getEtattable().equals("Dressée")){
                System.out.println("\033[0;35m" + "[Table " + table.getNumero() + " ]" + "\u001B[0m");
            }
            else{
                System.out.println("\u001B[97m" + "[Table " + table.getNumero() + " ]" + "\u001B[0m");
            }
        }
        System.out.println("--------------------------------------" + n + "Bienvenue en salle, prêt à dresser ? " + n
                + "--------------------------------------" + n + "1-" + listTables.size() + ". Selectionner une table"
                + n + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
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
        return rep;
    }

    @Override
    public void appelMethode(Integer num) {
        Connection conn = GestionBDD.connect();
        Integer rep = EcranTableAssitant(num - 1);
        switch (rep){
            case 1:
                debarraserTable(conn, num);
                break;
            case 2 :
                dresserTable(conn, num);
        }
    }

    private void recupTables() {
        listTables = new ArrayList<>();
        try {
            Connection conn = GestionBDD.connect();
            ResultSet rs = GestionBDD.executeSelect(conn,"SELECT * FROM tableresto ORDER BY numero");
            while (rs.next()) {
                listTables
                        .add(new Table(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour afficher l'écran d'une table au fois que l'assitant l'a choisie.
     *
     * @param numero numéro de la table concernée
     * @return choix de l'assistant
     */
    private Integer EcranTableAssitant(Integer numero) {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue à la table numéro " + numero + n
                + "--------------------------------------" + n + "1. Débarraser" + n
                + "2. Dresser" + n
                + "0. Retourner à l'écran principal" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, 3)) {
                System.out.println("Entrée non valide");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        return rep;
    }

    /**
     * Méthode pour débarraser une table
     * @param conn la connection à la base
     * @param numero le numéro de la table
     */
    public void debarraserTable(Connection conn, Integer numero){
        if (listTables.get(numero - 1).getEtattable().equals("Débarrasée")){
            String sql = "UPDATE tableresto SET etattable = 'Dressée' WHERE numero = " + numero;
            GestionBDD.executeUpdate(conn, sql);
        }
        else{
            System.out.println("Cette table n'a pas besoin d'être débarrasée.");
        }
    }

    /**
     * Méthode pour dresser une table
     * @param conn le connection à la base
     * @param numero le numéro de la table
     */
    public void dresserTable (Connection conn, Integer numero){
        if (listTables.get(numero - 1).getEtattable().equals("Dressée")){
            String sql = "UPDATE tableresto SET etattable = 'Libre' WHERE numero = " + numero;
            GestionBDD.executeUpdate(conn, sql);
        }
        else{
            System.out.println("Cette table n'a pas besoin d'être dresser.");
        }
    }
}
