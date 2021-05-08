package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.ColorText;
import fr.ul.miage.Restaurant.Resto.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Serveur extends Utilisateur {
    private ArrayList<Table> listTables;

    public Serveur(String id) {
        super(id);
        listTables = new ArrayList<>();
    }

    public void recupTables() {
        listTables = new ArrayList<>();
        try {
            String url = "jdbc:postgresql://plg-broker.ad.univ-lorraine.fr/Restaurant_G8";
            Connection conn = DriverManager.getConnection(url, "m1user1_03", "m1user1_03");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tableresto ORDER BY numero");
            while(rs.next()) {
                listTables.add(new Table(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer afficherPrincipal() {
        recupTables();
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        for (Table table : listTables){
            if (table.getServer().equals(id)){
                colorerTable(table);
            }
            else{
                System.out.println("\u001B[97m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
            }
        }
        System.out.println("--------------------------------------" + n + "Bienvenue en salle, prêt à servir ? " + n
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
        EcranTableServeur(listTables.get(num - 1));
    }

    private void colorerTable(Table table) {
        switch(table.getEtattable()) {
            case "Libre":
                System.out.println("\u001B[32m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
            case "Occupée":
                System.out.println("\u001B[33m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
            case "Débarrasée":
                System.out.println("\u001B[31m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
            case "Réservée":
                System.out.println("\u001B[36m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
            case "Dressée":
                System.out.println("\033[0;35m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
        }
    }

    /**
     * Méthode pour afficher l'écran d'une table au fois que le serveur l'a choisie.
     *
     * @param table table sélectionnée
     * @return le choix du serveur
     */
    public Integer EcranTableServeur(Table table) {
        int rep = -1;
        Scanner scan2 = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue à la table " + table.getNumero()
                + n + "Nombre de couvert : " + table.getNbplace() + n + "Etat de la table : " + table.getEtattable() + n
                + "Etat du repas : " + table.getEtatrepas() + n + n + "--------------------------------------" + n
                + "1. Changer le status de la table" + n + "2. Ajouter un plat" + n + "3. Obtenir la facture" + n
                + "0. Retourner à l'écran principal" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan2.nextInt();
            if (!verif(rep, 4)) {
                System.out.println("Entrée non valide");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        return rep;
    }
}