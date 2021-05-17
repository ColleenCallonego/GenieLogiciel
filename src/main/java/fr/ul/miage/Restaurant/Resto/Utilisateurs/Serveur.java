package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.Categorie;
import fr.ul.miage.Restaurant.Resto.ColorText;
import fr.ul.miage.Restaurant.Resto.Mp;
import fr.ul.miage.Restaurant.Resto.Plat;
import fr.ul.miage.Restaurant.Resto.Table;
import fr.ul.miage.Restaurant.Resto.misc.GestionBDD;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
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
            Connection conn = GestionBDD.connect();
            ResultSet rs = GestionBDD.executeSelect(conn,"SELECT * FROM tableresto ORDER BY numero");
            while (rs.next()) {
                listTables.add(new Table(rs.getInt(1), rs.getInt(2), rs.getString(3),
                        rs.getString(4), rs.getString(5)));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Categorie> recupCategories() {
        ArrayList<Categorie> listCategories = new ArrayList<>();
        try {

            Connection conn = GestionBDD.connect();
            ResultSet rs = GestionBDD.executeSelect(conn,"SELECT * FROM categorie");
            while (rs.next()) {
                listCategories.add(new Categorie(rs.getInt(1), rs.getString(2)));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listCategories;
    }

    public ArrayList<Plat> recupPlats(int idCategorie) {
        ArrayList<Plat> listPlats = new ArrayList<>();
        Date dateJour = new Date();
        try {

            Connection conn = GestionBDD.connect();
            String query = "SELECT plat.idplat, plat.nomplat, plat.prixplat FROM plat JOIN cartejour_plat " +
                    "ON cartejour_plat.plat = plat.idplat JOIN cartedujour ON cartedujour.idcartedujour = cartejour_plat.carte" +
                    " AND cartedujour.datecartejour = '" + dateJour + "' AND plat.cat =" + idCategorie +
                    "JOIN plat_mp ON plat_mp.plat = plat.idplat JOIN mp ON plat_mp.mp = mp.idmp " +
                    "WHERE plat_mp.quantite <= mp.stockmp" +
                    "GROUP BY plat.nomplat, plat.idplat HAVING COUNT(*) = plat.nbmp";

            ResultSet rs = GestionBDD.executeSelect(conn,query);
            while (rs.next()) {
                listPlats.add(new Plat(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            conn.close();
        } catch (SQLException e) {
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
        for (Table table : listTables) {
            if (table.getServer().equals(id)) {
                colorerTable(table);
            } else {
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
        switch (actionTable) {
            case 0:
                System.out.println("Retour à la page principale");
                break;
            case 1:
                changerEtatTable(num);
                break;
            case 2:
                ajouterPlat(num);
                break;
            case 3:
                imprimerFacture();
                break;
        }
    }

    private void changerEtatTable(int numTable) {
        ArrayList<String> listeEtat = new ArrayList<>();
        //recuperer l'etat
        String etatActuel = listTables.get(numTable - 1).getEtattable();
        if (etatActuel.equals("Libre")) {
            listeEtat.add("Occupée");
            listeEtat.add("Réservée");
        }
        if (etatActuel.equals("Réservée")) {
            listeEtat.add("Occupée");
            listeEtat.add("Libre");
        }
        if (etatActuel.equals("Occupée")) {
            listeEtat.add("Débarrasée");
        }
        //faire le choix du nouvel etat
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        for (String etat : listeEtat) {
            System.out.println("\u001B[97m" + "[" + etat + "]" + "\u001B[0m");
        }
        System.out.println("--------------------------------------" + n + "Changer l'état de la table" + n
                + "--------------------------------------");
        if (listeEtat.size() != 0) {
            System.out.println("1-" + listeEtat.size() + ". Selectionner un état");
        }
        System.out.println("0. Annuler" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, listeEtat.size() + 1)) {
                System.out.println("Entrée non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        if (rep > 0) {
            //changer l'état dans la base
            String nouvelEtat = listeEtat.get(rep - 1);
            try {

                Connection conn = GestionBDD.connect();
                GestionBDD.executeUpdate(conn,"UPDATE tableresto SET etattable = '" + nouvelEtat + "' WHERE numero = " + (numTable));
                System.out.println("La modification a fonctionnée");
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Modification annulée");
        }

    }

    private void imprimerFacture() {
    }

    private void colorerTable(Table table) {
        switch (table.getEtattable()) {
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

    public void InsererPlat(Connection conn, Integer numeroTable, Integer plat) {
        Integer idCommande = getIdCommande(conn, numeroTable);
        if (idCommande == -1) {
            creerCommande(conn, numeroTable);
            idCommande = getIdCommande(conn, numeroTable);
        }
        insererSousCommande(conn, plat, idCommande);
        modifierStockMP(conn, plat);
    }

    /**
     * Méthode pour modifier le stock de matière première d'un plat
     *
     * @param conn connection à la base
     * @param plat plat concerné
     */
    public void modifierStockMP(Connection conn, Integer plat) {
        ArrayList<Mp> listMpsPlat = getInfoMpPlat(conn, plat);
        ArrayList<Mp> listInfoMps = getInfoMp(conn, listMpsPlat);
        for (Mp mp : listInfoMps) {
            for (Mp mpPlat : listMpsPlat) {
                if (mp.getIdmp() == mpPlat.getIdmp()) {
                    Integer newStock = mp.getStockmp() - mpPlat.getStockmp();
                    String sql = "UPDATE mp SET stockmp = " + newStock + "WHERE idmp = " + mp.getIdmp();
                    GestionBDD.executeUpdate(conn,sql);
                }
            }
        }
    }

    /**
     * Méthode pour récupérer les matières premières d'un plat donné
     *
     * @param conn connection à la base
     * @param plat plat concerné
     * @return une liste de matière première présente dans le plat
     */
    public ArrayList<Mp> getInfoMpPlat(Connection conn, Integer plat) {
        try {
            ArrayList<Mp> list = new ArrayList<Mp>();
            String sql = "SELECT mp, quantite FROM plat_mp WHERE plat = '" + plat + "'";
            ResultSet rs = GestionBDD.executeSelect(conn,sql);
            while (rs.next()) {
                list.add(new Mp(rs.getInt(1), rs.getInt(2)));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Méthode pour récuperer le stock du matière première
     *
     * @param conn connection à la base
     * @param mps  liste de matière première
     * @return une liste avec les infos des matières premières
     */
    public ArrayList<Mp> getInfoMp(Connection conn, ArrayList<Mp> mps) {
        try {
            ArrayList<Mp> list = new ArrayList<Mp>();
            for (Mp mp : mps) {
                String sql = "SELECT idmp, stockmp FROM mp WHERE idmp = '" + mp.getIdmp() + "'";
                ResultSet rs = GestionBDD.executeSelect(conn,sql);
                while (rs.next()) {
                    list.add(new Mp(rs.getInt(1), rs.getInt(2)));
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Méthode pour insérer une sous-commande (un plat) lié à une commande donnée
     *
     * @param conn     connection à la base
     * @param plat     plat à ajouter
     * @param commande commande concernée
     */
    public void insererSousCommande(Connection conn, Integer plat, Integer commande) {
        Date d = new Date();
        String sql = "INSERT INTO souscommande(heurecommande, etatsouscommande, plat, commande)VALUES ('" + d + "', 'commande', '" + plat + "', '" + commande + "')";
        GestionBDD.executeUpdate(conn,sql);
    }

    /**
     * Méthode pour créer une commande
     *
     * @param conn connection à la base
     * @param num  numéro de la table liée à la commande
     */
    public void creerCommande(Connection conn, Integer num) {
        try {
            java.util.Date d = new Date();
            String service;
            if (d.getHours() < 15) {
                service = "Dejeuner";
            } else {
                service = "Diner";
            }
            Statement st = conn.createStatement();
            String sql = "INSERT INTO commande(datecommande, service, statuscommande, tableresto)VALUES ('" + d + "', '" + service + "', 'En cours', '" + num + "')";
            Integer status = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour obtenir l'identifiant d'une commande liée à une table
     *
     * @param conn  connection à la base
     * @param table numéro de la table concerné
     * @return le numéro de la table si une commande en cours pour cette table existe, -1 sinon
     */
    public Integer getIdCommande(Connection conn, Integer table) {
        try {

            String sql = "SELECT numerocommande FROM commande WHERE tableresto = '" + table + "' AND statuscommande = 'En cours'";
            ResultSet rs = GestionBDD.executeSelect(conn,sql);
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void ajouterPlat(int numTable) {
        int rep = -1;
        Scanner scan2 = new Scanner(System.in);
        String n = System.getProperty("line.separator");

        ArrayList<Categorie> listCategories;
        ArrayList<Plat> listePlats;
        Categorie categorieChoisie;
        Plat platChoisi;
        System.out.println("Nos catégories de plats :");
        listCategories = recupCategories();
        for (Categorie categorie : listCategories) {
            System.out.println("\u001B[97m" + "[" + categorie.getNomcategorie() + "]" + "\u001B[0m");
        }
        System.out.println("--------------------------------------" + n + "Voici les différentes catégories de plats du restaurant" + n
                + "--------------------------------------" + n + "1-" + listCategories.size() + ". Selectionner une catégorie"
                + n + "0. Annuler");
        try {
            rep = scan2.nextInt();
            if (!verif(rep, listCategories.size() + 1)) {
                System.out.println("Entrée non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        if (rep > 0) {
            categorieChoisie = listCategories.get(rep - 1);
            rep = -1;
            listePlats = recupPlats(categorieChoisie.getIdcategorie());
            System.out.println("Nos plats");
            for (Plat plat : listePlats) {
                System.out.println("\u001B[97m" + "[" + plat.getNomplat() + " à " + plat.getPrixplat() + "]" + "\u001B[0m");
            }
            System.out.println("--------------------------------------" + n + "Voici les différentes plats du restaurant" + n
                    + "--------------------------------------" + n + "1-" + listePlats.size() + ". Selectionner un plat"
                    + n + "0. Annuler");
            try {
                rep = scan2.nextInt();
                if (!verif(rep, listePlats.size() + 1)) {
                    System.out.println("Entrée non valide");
                    rep = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrée non valide");
                rep = -1;
            }
            if (rep > 0) {
                platChoisi = listePlats.get(rep - 1);
                InsererPlat(GestionBDD.connect(), numTable, platChoisi.getIdplat());
            }
            System.out.println("Opération annulée");
        }
        System.out.println("Opération annulée");
    }




}