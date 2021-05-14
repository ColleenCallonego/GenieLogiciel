package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.Categorie;
import fr.ul.miage.Restaurant.Resto.ColorText;
import fr.ul.miage.Restaurant.Resto.Plat;
import fr.ul.miage.Restaurant.Resto.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Date;
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

    public ArrayList<Categorie> recupCategories() {
        ArrayList<Categorie> listCategories = new ArrayList<>();
        try {
            String url = "jdbc:postgresql://plg-broker.ad.univ-lorraine.fr/Restaurant_G8";
            Connection conn = DriverManager.getConnection(url, "m1user1_03", "m1user1_03");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM categorie");
            while(rs.next()) {
                listCategories.add(new Categorie(rs.getInt(1), rs.getString(2)));
            }
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return listCategories;
    }

    public ArrayList<Plat> recupPlats(int idCategorie) {
        ArrayList<Plat> listPlats = new ArrayList<>();
        Date dateJour = new Date();
        try {
            String url = "jdbc:postgresql://plg-broker.ad.univ-lorraine.fr/Restaurant_G8";
            Connection conn = DriverManager.getConnection(url, "m1user1_03", "m1user1_03");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT plat.idplat, plat.nomplat, plat.prixplat FROM plat JOIN cartejour_plat ON cartejour_plat.plat = plat.idplat JOIN cartedujour ON cartedujour.idcartedujour = cartejour_plat.carte AND cartedujour.datecartejour = '" + dateJour + "' AND plat.cat =" + idCategorie);
            while(rs.next()) {
                listPlats.add(new Plat(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return listPlats;
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
        int actionTable = EcranTableServeur(listTables.get(num - 1));
        switch (actionTable){
            case 0:
                System.out.println("Retour à la page principale");
                break;
            case 1:

                break;
            case 2:
                ajouterPlat();
                break;
            case 3:

                break;

        }
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

    private void ajouterPlat() {
        int rep = -1;
        Scanner scan2 = new Scanner(System.in);
        String n = System.getProperty("line.separator");

        ArrayList<Categorie> listCategories;
        ArrayList<Plat> listePlats;
        Categorie categorieChoisie;
        Plat platChoisi;
        System.out.println("Nos catégories de plats :");
        listCategories = recupCategories();
        for (Categorie categorie : listCategories){
            System.out.println("\u001B[97m" + "[" + categorie.getNomcategorie() + "]" + "\u001B[0m");
        }
        System.out.println("--------------------------------------" + n + "Voici les différentes catégories de plats du restaurant" + n
                + "--------------------------------------" + n + "1-" + listCategories.size() + ". Selectionner une catégorie"
                + n + "0. Annuler");
        try {
            rep = scan2.nextInt();
            if (!verif(rep, listCategories.size()+1)) {
                System.out.println("Entrée non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        if (rep > 0){
            categorieChoisie = listCategories.get(rep-1);
            rep = -1;
            listePlats = recupPlats(categorieChoisie.getIdcategorie());
            System.out.println("Nos plats");
            for (Plat plat : listePlats){
                System.out.println("\u001B[97m" + "[" + plat.getNomplat() + " à " + plat.getPrixplat() + "]" + "\u001B[0m");
            }
            System.out.println("--------------------------------------" + n + "Voici les différentes plats du restaurant" + n
                    + "--------------------------------------" + n + "1-" + listePlats.size() + ". Selectionner un plat"
                    + n + "0. Annuler");
            try {
                rep = scan2.nextInt();
                if (!verif(rep, listePlats.size()+1)) {
                    System.out.println("Entrée non valide");
                    rep = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrée non valide");
                rep = -1;
            }
            if (rep > 0){
                platChoisi = listePlats.get(rep-1);
                //fonction de colleen
            }
            System.out.println("Opération annulée");
        }
        System.out.println("Opération annulée");
    }

}