package Restaurant.Resto.Utilisateurs;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import Restaurant.Resto.Categorie;
import Restaurant.Resto.Mp;
import Restaurant.Resto.Plat;

public class Cuisinier extends Utilisateur {

    public Cuisinier(String id) {
        super(id);
    }

    /**
     * Méthode pour afficher l'écran pricipal du cuisinier
     *
     * @return choix du cuisinier
     */
    @Override
    public Integer afficherPrincipal() {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue dans la cuisine " + n
                + "--------------------------------------" + n + "1. Définir un nouveau plat" + n
                + "2. Finir une commande" + n + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, 3)) {
                System.out.println("Entrée non valide");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        scan.close();
        return rep;
    }

    public void definitionPlat() {
        try {
            String nomPlat = "";
            Connection conn = connect();
            // Visualisation des plats déjà présents
            ArrayList<Plat> plats = getPlats(conn);
            if (plats.isEmpty()) {
                System.out.println("Il n'y a pas encore de plat enregistré.");
            } else {
                System.out.println("Voici les plats déjà enregistré :");
                for (Plat plat : plats) {
                    System.out.println("- " + plat);
                }
            }

            // récupérer le nom du plat à ajouter
            Boolean bonNom = false;
            while (!bonNom) {
                try {
                    Scanner sa = new Scanner(System.in);
                    System.out.println("Quel plat voulez vous ajouter ? (Entrez son nom)");
                    nomPlat = sa.nextLine();
                    if (!verifNomPlat(nomPlat, plats)) {
                        System.out.println("Ce plat existe déjà");
                    } else {
                        bonNom = true;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Nom non valide");
                }
            }

            // récupérer le prix du plat à ajouter
            Boolean bonPrix = false;
            Integer prix = 0;
            while (!bonPrix) {
                try {
                    Scanner s = new Scanner(System.in);
                    System.out.println("Quel est le prix de ce plat ? (Un prix entier)");
                    prix = s.nextInt();
                    if (prix > 0) {
                        bonPrix = true;
                    } else {
                        System.out.println("Prix non valide");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Prix non valide");
                }
            }

            // récupérer la catégorie du plat à ajouter
            ArrayList<Categorie> cates = getCategories(conn);
            Boolean bonneCat = false;
            Integer numCat = 0;
            Integer j = 1;
            while (!bonneCat) {
                for (Categorie categorie : cates) {
                    System.out.println("-" + j + " " + categorie);
                    j++;
                }
                try {
                    Scanner s = new Scanner(System.in);
                    System.out.println("Quelle est la catégorie de ce plat ? (tapez son numéro)");
                    numCat = s.nextInt();
                    if (verif(numCat, cates.size() + 2) && numCat != 0) {
                        bonneCat = true;
                    } else {
                        System.out.println("Entrée non valide");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrée non valide");
                }
            }

            // ajout du plat dans la base
            insererPlat(conn, nomPlat, prix, cates.get(numCat - 1).getIdcategorie());
            Integer idPlat = getIdPlat(conn, nomPlat);

            // ajout du mp pour le plat
            Boolean encoreMp = true;
            ArrayList<Mp> mps = getMPs(conn);
            Integer numMp;
            while (encoreMp) {
                Integer i = 1;
                for (Mp mp : mps) {
                    System.out.println("-" + i + " " + mp);
                    i++;
                }
                try {
                    Scanner s = new Scanner(System.in);
                    System.out.println("Quelle matière première voulez vous ajouter au plat ? (tapez son numéro)");
                    numMp = s.nextInt();
                    if (verif(numMp, mps.size() + 2) && numMp != 0) {
                        s = new Scanner(System.in);
                        Integer quantite;
                        System.out.println(
                                "Combien de matière première ce plat nécessite-t-il ? (entrez une quantité entière)");
                        quantite = s.nextInt();
                        insererPlat_mp(conn, idPlat, mps.get(numMp - 1).getIdmp(), quantite);
                        Boolean mauvaiseRep = true;
                        while (mauvaiseRep) {
                            try {
                                String rep;
                                s = new Scanner(System.in);
                                System.out.println("Ajouter une autre matière première à ce plat ? (Oui ou Non)");
                                rep = s.nextLine();
                                if (rep.equals("Oui")) {
                                    encoreMp = true;
                                    mauvaiseRep = false;
                                } else if (rep.equals("Non")) {
                                    encoreMp = false;
                                    mauvaiseRep = false;
                                } else {
                                    System.out.println("Réponse non valide");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Réponse non valide");
                            }
                        }
                    } else {
                        System.out.println("Entrée non valide");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrée non valide");
                }
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour inserer le lien entre mp et plat
     * 
     * @param conn
     * @param idPlat
     * @param idMp
     * @param quantite
     */
    public void insererPlat_mp(Connection conn, Integer idPlat, Integer idMp, Integer quantite) {
        try {
            Statement st = conn.createStatement();
            String sql = "INSERT INTO plat_mp(plat, mp, quantite)VALUES ('" + idPlat + "',' " + idMp + "',' " + quantite
                    + "')";
            Integer status = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour vérifier que le nom du plat n'est pas déja pris
     * 
     * @param nomPlat
     * @param plats
     * @return
     */
    public boolean verifNomPlat(String nomPlat, ArrayList<Plat> plats) {
        for (Plat plat : plats) {
            if (plat.compareNom(nomPlat)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Méthode pour insérer un plat dans la base
     * 
     * @param conn
     * @param nom
     * @param prix
     * @param idCat
     */
    public void insererPlat(Connection conn, String nom, Integer prix, Integer idCat) {
        try {
            System.out.println(nom + " " + prix + " " + idCat);
            Statement st = conn.createStatement();
            String sql = "INSERT INTO plat(nomplat, prixplat, cat)VALUES ('" + nom + "',' " + prix + "',' " + idCat
                    + "')";
            Integer status = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour obtenir l'id d'un plat
     * 
     * @param conn
     * @param nom
     * @return
     */
    public Integer getIdPlat(Connection conn, String nom) {
        try {
            Statement st = conn.createStatement();
            String sql = "SELECT idplat FROM plat WHERE nomplat = '" + nom + "'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Méthode pour récupérer les différentes catégories présentes dans la base
     * 
     * @param conn le connection à la base
     * @return une liste de catégorie
     */
    public ArrayList<Categorie> getCategories(Connection conn) {
        ArrayList<Categorie> cats = new ArrayList<Categorie>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM categorie");
            while (rs.next()) {
                cats.add(new Categorie(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cats;
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

    /**
     * Méthode pour récupérer les différentes matières premières dans la base de
     * données
     * 
     * @param conn la connection à la base
     * @return une liste de mp
     */
    public ArrayList<Mp> getMPs(Connection conn) {
        ArrayList<Mp> mps = new ArrayList<Mp>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mp");
            while (rs.next()) {
                mps.add(new Mp(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mps;
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
}
