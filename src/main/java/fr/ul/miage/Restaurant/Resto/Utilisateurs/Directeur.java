package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.Plat;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

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
            if (!verif(rep, 8)) {
                System.out.println("Entrée non valide");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        scan.close();
        return rep;
    }

    public void gestionCarteDuJour(){
        Scanner scan = new Scanner(System.in);
        Connection conn = connect();
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String n = System.getProperty("line.separator");
        System.out.println("Bienvenue de la gestion de la carte du jour !");
        System.out.print("Nous allons créer la carte du jour du ");
        System.out.println(s.format(date));
        if (getIdCarteDuJour(conn, date) == -1){
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
            while(encore || mauvaiseRep){
                Integer i = 1;
                for (Plat plat : plats) {
                    System.out.println("- " + i + " " + plat);
                    i++;
                }
                System.out.println("Quel plat voulez vous ajouter à la carte du jour ? (tapez son numéro)");
                mauvaiseRep = true;
                rep = scan.nextInt();
                if (verif(rep, plats.size() + 2) && rep != 0){
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
                }
                else{
                    System.out.println("Entrée non valide !");
                }
            }
        }
    }

    /**
     * Méthode pour se connecter à la base de donnée
     *
     * @return la connection à la base
     */
    public Connection connect() {
        String url = "jdbc:postgresql://plg-broker.ad.univ-lorraine.fr/Restaurant_G8";
        String user = "m1user1_03";
        String password = "m1user1_03";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void creerCarteDuJour(Connection conn, Date d) {
        try {
            Statement st = conn.createStatement();
            String sql = "INSERT INTO cartedujour(datecartejour)VALUES ('" + d + "')";
            Integer status = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getIdCarteDuJour(Connection conn, Date d){
        try {
            Statement st = conn.createStatement();
            String sql = "SELECT idcartedujour FROM cartedujour WHERE datecartejour = '" + d + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                return rs.getInt(1);
            }
            else{
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
    public ArrayList<Plat> getPlats(Connection conn) {
        ArrayList<Plat> plats = new ArrayList<Plat>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT plat.idplat, plat.nomplat, plat.prixplat, categorie.nomcategorie FROM plat JOIN categorie ON plat.cat = categorie.idcategorie");
            while (rs.next()) {
                plats.add(new Plat(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }

    public void créerCarteJour_Plat(Connection conn, Integer carte, Integer plat){
        try {
            Statement st = conn.createStatement();
            String sql = "INSERT INTO cartejour_plat(carte, plat)VALUES ('" + carte + "',' " + plat + "')";
            Integer status = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
