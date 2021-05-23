package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.Plat;
import fr.ul.miage.Restaurant.Resto.misc.GestionBDD;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import static fr.ul.miage.Restaurant.Resto.Main.scan;

public class Directeur extends Utilisateur {

    public Directeur(String id) {
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

        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "   Bienvenue M. le Directeur   " + n
                + "--------------------------------------" + n + "1. Gestion des employés" + n
                + "2. Gestion de la carte du jour" + n + "3. Gestion des stocks de matières premières" + n
                + "4. Analyser les ventes" + n + "5. Popularité des plats" + n
                + "6. Connaitre le temps de rotation moyen" + n + "7. Connaitre le temps de préparatiion moyen" + n
                + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, 8)) {
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
                gestionEmployes();
                break;
            case 2:
                gestionCarteDuJour();
                break;
            case 3:
                gestionMatPremieres();

                break;
            case 4:
                menuVentes();

                break;
            case 5:
                popularitePLats();

                break;
            case 6:

                break;
            case 7:

                break;
        }
    }

    private void menuVentes() {
        //TODO ajouter un switch avec scanner quand on aura besoin d'une autre fonction
        consulterRecettes();
    }

    private void consulterRecettes() {
        scan.reset();
        System.out.println("Quelle granularite temporelle souhaitez-vous ?");
        System.out.println("0.Recette journaliere\n1.Recette hebdomadaire\n2.Recette mensuelle");
        int num = scan.nextInt();
        String query,subQuery;
        switch (num){
            case 0: subQuery = "heurecommande";
                System.out.println("  Date             Recette");
                break;
            case 1: subQuery = "date_part('week',heurecommande)";
                System.out.println("  Semaine  Recette");
                break;
            case 2: subQuery = "date_part('month',heurecommande)";
                System.out.println("  Mois    Recette");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + num);
        }
        query = "SELECT "+subQuery+" as date,SUM(total) as recette \n" +
                "FROM (SELECT heurecommande,SUM(prixplat) as total from souscommande join plat ON souscommande.plat = plat.idplat\n" +
                "GROUP BY prixplat,heurecommande \n" +
                "ORDER BY heurecommande ) as r GROUP BY date ORDER BY date DESC";

        Connection conn = GestionBDD.connect();
        ResultSet rs = GestionBDD.executeSelect(conn,query);

        try{
            while (rs.next()){
                System.out.println("- "+rs.getArray("date")+"       "+rs.getArray("recette")+" €");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        attendreFinConsultation();
    }

    private void popularitePLats() {
        int nbTotalCommandes = 1;
        try {

            String query = "SELECT COUNT(*) FROM souscommande";
            Connection conn = GestionBDD.connect();
            ResultSet rs = GestionBDD.executeSelect(conn,query);
            if (rs.next()){
               nbTotalCommandes = rs.getInt(1);
            }
            query = "SELECT nomplat,COUNT(*) from souscommande join plat ON souscommande.plat = plat.idplat " +
                    "GROUP BY nomplat ORDER BY count DESC";
            rs = GestionBDD.executeSelect(conn,query);

            System.out.println("Voici les différents plats avec leur popularité:");


            while (rs.next()) {
                System.out.println("- "+rs.getArray("nomplat") + "  " + String.format("%.2f", 100*(rs.getFloat("count"))/nbTotalCommandes) +" %");
            }

            attendreFinConsultation();



            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void attendreFinConsultation() {
        System.out.println("\nAppuyer sur une touche puis sur entree pour revenir au menu principal");
        scan.reset();
        scan.next();
    }

    /**
     * gere les employes
     */
    private void gestionEmployes() {
        scan.reset();
        System.out.println("Que souhaitez-vous faire ?\n0.retour\n1.ajouter un employer\n2.supprimer un employe");
        int choice = scan.nextInt();
        switch (choice) {
            case 0:
                break;
            case 1:
                ajouterEmploye();
                break;
            case 2:
                supprimerEmploye();
                break;

        }
    }

    private void supprimerEmploye() {
        scan.reset();
        System.out.println("id de l'employe ?");
        String name = scan.next();

        try {

            String query = "DELETE FROM utilisateur WHERE idutili='" + name + "'";
            Connection conn = GestionBDD.connect();
            GestionBDD.executeUpdate(conn,query);

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private void ajouterEmploye() {
        scan.reset();
        System.out.println("id de l'employe ?");
        String name = scan.next();
        System.out.println("mdp ?");
        String mdp = scan.next();
        System.out.println("type/fonction ?");
        String type = scan.next();

        try {

            String query = "INSERT INTO utilisateur (idutili, mdp, typeutili) VALUES ('" + name + "','" + mdp + "','" + type + "')";
            Connection conn = GestionBDD.connect();
            GestionBDD.executeUpdate(conn,query);

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void gestionMatPremieres() {
        System.out.println("Selectionner l'option 'ajout' ou l'aliment à modifier");
        try {

            String query = "SELECT * FROM mp";
            Connection conn = GestionBDD.connect();
            ResultSet rs = GestionBDD.executeSelect(conn,query);

            //affichage des matieres premieres
            int index = 2;
            System.out.println("0.retour\n1.ajout");
            while (rs.next()) {
                System.out.println(index + "." + rs.getArray("nommp") + "  " + rs.getArray("stockmp"));
                index++;
            }
            int choice = scan.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    ajouterMatPremiere();
                    break;
                default:

                    System.out.println("Selectionner la nouvelle quantite");
                    int quantity = scan.nextInt();
                    modifQuantite(choice, quantity);
            }

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * ajoute une matiere premiere
     */
    private void ajouterMatPremiere() {
        scan.reset();
        System.out.println("nom de l'aliment ?");
        String name = scan.next();
        System.out.println("quantite ?");
        int quantity = scan.nextInt();

        try {

            String query = "INSERT INTO mp (nommp, stockmp) VALUES ('" + name + "','" + quantity + "')";
            Connection conn = GestionBDD.connect();
            GestionBDD.executeUpdate(conn, query);

            System.out.println("changement effectue avec succes");

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * modifie la quantité d'une matiere premiere
     *
     * @param id
     * @param quantity
     */
    private void modifQuantite(int id, int quantity) {
        try {
            Connection conn = GestionBDD.connect();
            String query = "UPDATE mp SET stockmp = '" + quantity + "' WHERE idmp ='" + id + "'";
            GestionBDD.executeUpdate(conn, query);

            System.out.println("changement effectue avec succes");

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void gestionCarteDuJour() {
        Scanner scan = new Scanner(System.in);
        Connection conn = GestionBDD.connect();
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String n = System.getProperty("line.separator");
        System.out.println("Bienvenue de la gestion de la carte du jour !");
        System.out.print("Nous allons créer la carte du jour du ");
        System.out.println(s.format(date));
        if (getIdCarteDuJour(conn, date) == -1) {
            creerCarteDuJour(conn, date);
        }
        Integer idCarte = getIdCarteDuJour(conn, date);
        ArrayList<Plat> plats = getPlats(conn);
        if (plats.isEmpty()) {
            System.out.println("Il n'y a pas encore de plat enregistré.");
        } else {
            System.out.println("Voici les plats déjà enregistré :");
            Boolean encore = true;
            Boolean mauvaiseRep = true;
            Integer rep;
            while (encore || mauvaiseRep) {
                Integer i = 1;
                for (Plat plat : plats) {
                    System.out.println("- " + i + " " + plat);
                    i++;
                }
                System.out.println("Quel plat voulez vous ajouter à la carte du jour ? (tapez son numéro)");
                mauvaiseRep = true;
                rep = scan.nextInt();
                if (verif(rep, plats.size() + 2) && rep != 0) {
                    Scanner scan2 = new Scanner(System.in);
                    mauvaiseRep = false;
                    créerCarteJour_Plat(conn, idCarte, plats.get(rep - 1).getIdplat());
                    Boolean mauvaise = true;
                    while (mauvaise) {
                        try {
                            String rep2;
                            System.out.println("Ajouter un autre plat à la carte du jour ? (Oui ou Non)");
                            rep2 = scan2.nextLine();
                            if (rep2.equals("Oui")) {
                                encore = true;
                                mauvaise = false;
                                mauvaiseRep = false;
                            } else if (rep2.equals("Non")) {
                                encore = false;
                                mauvaise = false;
                                mauvaiseRep = false;
                            } else {
                                System.out.println("Réponse non valide");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Réponse non valide");
                        }
                    }
                } else {
                    System.out.println("Entrée non valide !");
                }
            }
        }
    }


    private void creerCarteDuJour(Connection conn, Date d) {
        String sql = "INSERT INTO cartedujour(datecartejour)VALUES ('" + d + "')";
        GestionBDD.executeUpdate(conn, sql);
    }

    private Integer getIdCarteDuJour(Connection conn, Date d) {
        try {
            String sql = "SELECT idcartedujour FROM cartedujour WHERE datecartejour = '" + d + "'";
            ResultSet rs = GestionBDD.executeSelect(conn, sql);
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

    /**
     * Méthode pour récupérer les différents plats déjà présents dans la base
     *
     * @param conn la connection à la base
     * @return une liste de plat
     */
    private ArrayList<Plat> getPlats(Connection conn) {
        ArrayList<Plat> plats = new ArrayList<Plat>();
        try {
            String query = "SELECT plat.idplat, plat.nomplat, plat.prixplat, categorie.nomcategorie FROM plat JOIN categorie ON plat.cat = categorie.idcategorie";
            ResultSet rs = GestionBDD.executeSelect(conn, query);
            while (rs.next()) {
                plats.add(new Plat(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }

    private void créerCarteJour_Plat(Connection conn, Integer carte, Integer plat) {
        String sql = "INSERT INTO cartejour_plat(carte, plat)VALUES ('" + carte + "',' " + plat + "')";
        GestionBDD.executeUpdate(conn, sql);
    }
}
