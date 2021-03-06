package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.*;
import fr.ul.miage.Restaurant.Resto.misc.GestionBDD;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Serveur extends Utilisateur {
    public ArrayList<Table> listTables;

    public Serveur(String id) {
        super(id);
        listTables = new ArrayList<>();
    }

    public void recupTables(Connection conn) {
        listTables = new ArrayList<>();
        try {
            ResultSet rs = GestionBDD.executeSelect(conn,"SELECT * FROM tableresto ORDER BY numero");
            while (rs.next()) {
                listTables.add(new Table(rs.getInt(1), rs.getInt(2), rs.getString(3),
                        rs.getString(4), rs.getString(5)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Categorie> recupCategories(Connection conn) {
        ArrayList<Categorie> listCategories = new ArrayList<>();
        try {
            ResultSet rs = GestionBDD.executeSelect(conn,"SELECT * FROM categorie");
            while (rs.next()) {
                listCategories.add(new Categorie(rs.getInt(1), rs.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listCategories;
    }

    public ArrayList<Plat> recupPlats(Connection conn, int idCategorie) {
        ArrayList<Plat> listPlats = new ArrayList<>();
        Date dateJour = new Date();
        try {
            String query = "SELECT plat.idplat, plat.nomplat, plat.prixplat FROM plat JOIN cartejour_plat " +
                    "ON cartejour_plat.plat = plat.idplat JOIN cartedujour ON cartedujour.idcartedujour = cartejour_plat.carte" +
                    " AND cartedujour.datecartejour = '" + dateJour + "' AND plat.cat =" + idCategorie +
                    "JOIN plat_mp ON plat_mp.plat = plat.idplat JOIN mp ON plat_mp.mp = mp.idmp " +
                    "WHERE plat_mp.quantite <= mp.stockmp " +
                    "GROUP BY plat.nomplat, plat.idplat HAVING COUNT(*) = plat.nbmp";

            ResultSet rs = GestionBDD.executeSelect(conn,query);
            while (rs.next()) {
                listPlats.add(new Plat(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPlats;
    }

    public ArrayList<CommandeFacture> recupCommande(Connection conn, int numTable) {
        ArrayList<CommandeFacture> listCommande = new ArrayList<>();
        try {
            String query = "SELECT commande.numerocommande, commande.datecommande, plat.nomplat, plat.prixplat FROM commande " +
                    "JOIN souscommande ON commande.numerocommande = souscommande.commande " +
                    "JOIN plat ON souscommande.plat = plat.idplat " +
                    "AND commande.tableresto = " + numTable + " " +
                    "AND commande.statuscommande = 'En cours' " +
                    "ORDER by plat.prixplat";
            ResultSet rs = GestionBDD.executeSelect(conn,query);
            while (rs.next()) {
                listCommande.add(new CommandeFacture(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getInt(4)));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listCommande;
    }

    @Override
    public Integer afficherPrincipal() {
        recupTables(GestionBDD.connect());
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
        System.out.println("--------------------------------------" + n + "Bienvenue en salle, pr??t ?? servir ? " + n
                + "--------------------------------------" + n + "1-" + listTables.size() + ". Selectionner une table"
                + n + "0. Se d??connecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, listTables.size() + 1)) {
                System.out.println("Entr??e non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entr??e non valide");
            rep = -1;
        }
        return rep;

    }

    @Override
    public void appelMethode(Integer num) {
        int actionTable = EcranTableServeur(listTables.get(num - 1));
        switch (actionTable) {
            case 0:
                System.out.println("Retour ?? la page principale");
                break;
            case 1:
                changerEtatTable(num);
                break;
            case 2:
                changerEtatRepas(num);
                break;
            case 3:
                ajouterPlat(num);
                break;
            case 4:
                imprimerFacture(num);
                break;
        }
    }

    private void changerEtatTable(int numTable) {
        ArrayList<String> listeEtat = new ArrayList<>();
        //recuperer l'etat
        String etatActuel = listTables.get(numTable - 1).getEtattable();
        if (etatActuel.equals("Libre")) {
            listeEtat.add("Occup??e");
            listeEtat.add("R??serv??e");
        }
        if (etatActuel.equals("R??serv??e")) {
            listeEtat.add("Occup??e");
            listeEtat.add("Libre");
        }
        if (etatActuel.equals("Occup??e")) {
            listeEtat.add("D??barrass??e");
        }
        //faire le choix du nouvel etat
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        for (String etat : listeEtat) {
            System.out.println("\u001B[97m" + "[" + etat + "]" + "\u001B[0m");
        }
        System.out.println("--------------------------------------" + n + "Changer l'??tat de la table" + n
                + "--------------------------------------");
        if (listeEtat.size() != 0) {
            System.out.println("1-" + listeEtat.size() + ". Selectionner un ??tat");
        }
        System.out.println("0. Annuler" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, listeEtat.size() + 1)) {
                System.out.println("Entr??e non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entr??e non valide");
            rep = -1;
        }
        if (rep > 0) {
            //changer l'??tat dans la base
            String nouvelEtat = listeEtat.get(rep - 1);

            Connection conn = GestionBDD.connect();
            GestionBDD.executeUpdate(conn,"UPDATE tableresto SET etattable = '" + nouvelEtat + "' WHERE numero = " + (numTable));
            System.out.println("La modification a fonctionn??e");

        } else {
            System.out.println("Modification annul??e");
        }

    }

    private void changerEtatRepas(int numTable) {
        ArrayList<String> listeEtat = new ArrayList<>();
        //recuperer l'etat
        String etatActuel = listTables.get(numTable - 1).getEtatrepas();
        if (etatActuel != null) {
            if (etatActuel.equals("Entr??e")) {
                listeEtat.add("Plat");
                listeEtat.add("Dessert");
            }
            if (etatActuel.equals("Plat")) {
                listeEtat.add("Dessert");
            }
        }
        //faire le choix du nouvel etat
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        for (String etat : listeEtat) {
            System.out.println("\u001B[97m" + "[" + etat + "]" + "\u001B[0m");
        }
        System.out.println("--------------------------------------" + n + "Changer l'??tat du repas" + n
                + "--------------------------------------");
        if (listeEtat.size() != 0) {
            System.out.println("1-" + listeEtat.size() + ". Selectionner un ??tat");
        }
        System.out.println("0. Annuler" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, listeEtat.size() + 1)) {
                System.out.println("Entr??e non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entr??e non valide");
            rep = -1;
        }
        if (rep > 0) {
            //changer l'??tat dans la base
            String nouvelEtat = listeEtat.get(rep - 1);
            try {

                Connection conn = GestionBDD.connect();
                GestionBDD.executeUpdate(conn,"UPDATE tableresto SET etatrepas = '" + nouvelEtat + "' WHERE numero = " + (numTable));
                System.out.println("La modification a fonctionn??e");
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Modification annul??e");
        }

    }

    private void imprimerFacture(int numTab) {
        ArrayList<CommandeFacture> listeCommandFacture = recupCommande(GestionBDD.connect(), numTab);
        //affichage/impression de la facture
        int total = 0;
        System.out.println("FACTURE     table " + numTab);
        System.out.println("--------------------------------------");
        for (CommandeFacture commandeFacture : listeCommandFacture) {
            System.out.println(commandeFacture.getNomPlatCommande() + " => " + commandeFacture.getPrixPlatCommande() + " ???");
            total += commandeFacture.getPrixPlatCommande();
        }
        System.out.println("--------------------------------------");
        System.out.println("TOTAL   ===>   " + total + " ???");
        //cr??ation de la facture et modification etat de la commande sur la BDD
        try {
            Connection conn = GestionBDD.connect();
            String query = "INSERT INTO facture(datefacture, montantfacture, commande) VALUES ('" + listeCommandFacture.get(0).getDateCommande() + "', " + total + ", " + listeCommandFacture.get(0).getIdCommande() + ")";
            GestionBDD.executeUpdate(conn, query);
            System.out.println("La facture a bien ??t?? enregistr??e");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void colorerTable(Table table) {
        switch (table.getEtattable()) {
            case "Libre":
                System.out.println("\u001B[32m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
            case "Occup??e":
                System.out.println("\u001B[33m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
            case "D??barrass??e":
                System.out.println("\u001B[31m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
            case "R??serv??e":
                System.out.println("\u001B[36m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
            case "Dress??e":
                System.out.println("\033[0;35m" + "[Table " + table.getNumero() + "]" + "\u001B[0m");
                break;
        }
    }

    /**
     * M??thode pour afficher l'??cran d'une table au fois que le serveur l'a choisie.
     *
     * @param table table s??lectionn??e
     * @return le choix du serveur
     */
    private Integer EcranTableServeur(Table table) {
        int rep = -1;
        Scanner scan2 = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue ?? la table " + table.getNumero()
                + n + "Nombre de couvert : " + table.getNbplace() + n + "Etat de la table : " + table.getEtattable() + n
                + "Etat du repas : " + table.getEtatrepas() + n + n + "--------------------------------------" + n
                + "1. Changer le status de la table" + n + "2. Changer l'??tat du repas" + n + "3. Ajouter un plat" + n + "4. Obtenir la facture" + n
                + "0. Retourner ?? l'??cran principal" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan2.nextInt();
            if (!verif(rep, 5)) {
                System.out.println("Entr??e non valide");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entr??e non valide");
            rep = -1;
        }
        return rep;
    }

    private void InsererPlat(Connection conn, Integer numeroTable, Integer plat) {
        Integer idCommande = getIdCommande(conn, numeroTable);
        if (idCommande == -1) {
            creerCommande(conn, numeroTable);
            idCommande = getIdCommande(conn, numeroTable);
        }
        insererSousCommande(conn, plat, idCommande);
        modifierStockMP(conn, plat);
    }

    /**
     * M??thode pour modifier le stock de mati??re premi??re d'un plat
     *
     * @param conn connection ?? la base
     * @param plat plat concern??
     */
    private void modifierStockMP(Connection conn, Integer plat) {
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
     * M??thode pour r??cup??rer les mati??res premi??res d'un plat donn??
     *
     * @param conn connection ?? la base
     * @param plat plat concern??
     * @return une liste de mati??re premi??re pr??sente dans le plat
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
     * M??thode pour r??cuperer le stock du mati??re premi??re
     *
     * @param conn connection ?? la base
     * @param mps  liste de mati??re premi??re
     * @return une liste avec les infos des mati??res premi??res
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
     * M??thode pour ins??rer une sous-commande (un plat) li?? ?? une commande donn??e
     *
     * @param conn     connection ?? la base
     * @param plat     plat ?? ajouter
     * @param commande commande concern??e
     */
    private void insererSousCommande(Connection conn, Integer plat, Integer commande) {
        Date d = new Date();
        String sql = "INSERT INTO souscommande(heurecommande, etatsouscommande, plat, commande)VALUES ('" + d + "', 'commande', '" + plat + "', '" + commande + "')";
        GestionBDD.executeUpdate(conn,sql);
    }

    /**
     * M??thode pour cr??er une commande
     *
     * @param conn connection ?? la base
     * @param num  num??ro de la table li??e ?? la commande
     */
    private void creerCommande(Connection conn, Integer num) {
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
     * M??thode pour obtenir l'identifiant d'une commande li??e ?? une table
     *
     * @param conn  connection ?? la base
     * @param table num??ro de la table concern??
     * @return le num??ro de la table si une commande en cours pour cette table existe, -1 sinon
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
        System.out.println("Nos cat??gories de plats :");
        listCategories = recupCategories(GestionBDD.connect());
        for (Categorie categorie : listCategories) {
            System.out.println("\u001B[97m" + "[" + categorie.getNomcategorie() + "]" + "\u001B[0m");
        }
        System.out.println("--------------------------------------" + n + "Voici les diff??rentes cat??gories de plats du restaurant" + n
                + "--------------------------------------" + n + "1-" + listCategories.size() + ". Selectionner une cat??gorie"
                + n + "0. Annuler");
        try {
            rep = scan2.nextInt();
            if (!verif(rep, listCategories.size() + 1)) {
                System.out.println("Entr??e non valide");
                rep = -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entr??e non valide");
            rep = -1;
        }
        if (rep > 0) {
            categorieChoisie = listCategories.get(rep - 1);
            rep = -1;
            listePlats = recupPlats(GestionBDD.connect(), categorieChoisie.getIdcategorie());
            System.out.println("Nos plats");
            for (Plat plat : listePlats) {
                System.out.println("\u001B[97m" + "[" + plat.getNomplat() + " ?? " + plat.getPrixplat() + "]" + "\u001B[0m");
            }
            System.out.println("--------------------------------------" + n + "Voici les diff??rentes plats du restaurant" + n
                    + "--------------------------------------" + n + "1-" + listePlats.size() + ". Selectionner un plat"
                    + n + "0. Annuler");
            try {
                rep = scan2.nextInt();
                if (!verif(rep, listePlats.size() + 1)) {
                    System.out.println("Entr??e non valide");
                    rep = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entr??e non valide");
                rep = -1;
            }
            if (rep > 0) {
                platChoisi = listePlats.get(rep - 1);
                InsererPlat(GestionBDD.connect(), numTable, platChoisi.getIdplat());
            }
            else{
                System.out.println("Op??ration annul??e");
            }
        }
        else{
            System.out.println("Op??ration annul??e");
        }
    }




}