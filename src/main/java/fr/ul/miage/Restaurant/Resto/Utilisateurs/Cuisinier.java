package fr.ul.miage.Restaurant.Resto.Utilisateurs;

import fr.ul.miage.Restaurant.Resto.Categorie;
import fr.ul.miage.Restaurant.Resto.Mp;
import fr.ul.miage.Restaurant.Resto.Plat;
import fr.ul.miage.Restaurant.Resto.SousCommande;
import fr.ul.miage.Restaurant.Resto.misc.GestionBDD;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static fr.ul.miage.Restaurant.Resto.Main.scan;

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

        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue dans la cuisine " + n
                + "--------------------------------------" + n + "1. Définir un nouveau plat" + n
                + "2. Finir une commande" + n + "0. Se déconnecter" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, 3)) {
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
                definitionPlat();
                break;
            case 2:
                finalisationCommande();
                break;
        }
    }

    private void finalisationCommande() {
        Connection conn = GestionBDD.connect();
        int idCommande;
        do {
            idCommande = afficherListesAttentes(conn);
        } while (idCommande == -1);
        if (idCommande != 0) {
            String query = "UPDATE souscommande SET etatsouscommande = 'prepare' WHERE idsouscommande = '" + idCommande + "'";
            GestionBDD.executeUpdate(conn, query);
        }

    }

    private void definitionPlat() {
        try {
            String nomPlat = "";
            Connection conn = GestionBDD.connect();
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
    private void insererPlat_mp(Connection conn, Integer idPlat, Integer idMp, Integer quantite) {
        String sql = "INSERT INTO plat_mp(plat, mp, quantite)VALUES ('" + idPlat + "',' " + idMp + "',' " + quantite
                + "')";
        GestionBDD.executeUpdate(conn, sql);
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
    private void insererPlat(Connection conn, String nom, Integer prix, Integer idCat) {
        System.out.println(nom + " " + prix + " " + idCat);

        String sql = "INSERT INTO plat(nomplat, prixplat, cat)VALUES ('" + nom + "',' " + prix + "',' " + idCat
                + "')";
        GestionBDD.executeUpdate(conn, sql);
    }

    /**
     * Méthode pour obtenir l'id d'un plat
     *
     * @param conn
     * @param nom
     * @return
     */
    private Integer getIdPlat(Connection conn, String nom) {
        try {

            String sql = "SELECT idplat FROM plat WHERE nomplat = '" + nom + "'";
            ResultSet rs = GestionBDD.executeSelect(conn, sql);
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
    private ArrayList<Categorie> getCategories(Connection conn) {
        ArrayList<Categorie> cats = new ArrayList<Categorie>();
        try {

            ResultSet rs = GestionBDD.executeSelect(conn, "SELECT * FROM categorie");
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
    private ArrayList<Plat> getPlats(Connection conn) {
        ArrayList<Plat> plats = new ArrayList<Plat>();
        try {

            ResultSet rs = GestionBDD.executeSelect(conn,
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
    private ArrayList<Mp> getMPs(Connection conn) {
        ArrayList<Mp> mps = new ArrayList<Mp>();
        try {

            ResultSet rs = GestionBDD.executeSelect(conn, "SELECT * FROM mp");
            while (rs.next()) {
                mps.add(new Mp(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mps;
    }


    /**
     * Méthode pour afficher les deux listes d'attentes en cuisine
     *
     * @param conn connection à la base
     * @return le numero de la sous commandde qui a été préparer ou -1 si la saisie est incorrecte
     */
    private Integer afficherListesAttentes(Connection conn) {
        Integer rep;
        ArrayList<SousCommande> enfants = getListeAttenteEnfant(conn);
        ArrayList<SousCommande> normaux = getListeAttente(conn);
        if (enfants.size() ==0){
            System.out.println("Il n'y a pas de menu enfant à préparer");
        }
        else{
            System.out.println("Voici la liste d'attente des menus enfants (prioritaire)");
            for (SousCommande sscom : enfants) {
                System.out.println(sscom);
            }
        }
        if (normaux.size() == 0){
            System.out.println("Il n'y a pas de plat normal à préparer");
        }
        else{
            System.out.println("Voici la liste d'attente des plats (non prioritaire)");
            for (SousCommande sscom : normaux) {
                System.out.println(sscom);
            }
        }
        System.out.println("0. Annuler");
        System.out.println("Quelle commande avez vous préparez ? (Entrez son numéro)");
        try {
            Scanner s = new Scanner(System.in);
            rep = s.nextInt();
            Boolean bonneRep = false;
            String list = "Enfants";
            Integer i = 0;
            ArrayList<SousCommande> fusion = enfants;
            fusion.addAll(normaux);
            if (verifNum(rep, fusion) || rep == 0) {
                return rep;
            } else {
                System.out.println("Numéro non valide");
                return -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Numéro non valide");
            return -1;
        }
    }

    /**
     * Méthode pour vérifier le numéro d'une sous-commande
     *
     * @param num    numéro rentré par le cuisinier
     * @param sscoms liste de toute les commandes possibles
     * @return true si le numéro existe dans la liste, false sinon
     */
    public boolean verifNum(Integer num, ArrayList<SousCommande> sscoms) {
        for (SousCommande com : sscoms) {
            if (com.sameNum(num)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode pour obtenir la liste d'attente des plats normaux
     *
     * @param conn connection à la base
     */
    private ArrayList<SousCommande> getListeAttente(Connection conn) {
        ArrayList<SousCommande> sscommandes = new ArrayList<SousCommande>();
        try {

            ResultSet rs = GestionBDD.executeSelect(conn, "SELECT souscommande.idsouscommande, plat.nomplat FROM souscommande JOIN plat ON plat.idplat = souscommande.plat WHERE souscommande.etatsouscommande = 'commande' AND plat.nomplat != 'Menu enfant' ORDER BY souscommande.heurecommande ASC;");
            while (rs.next()) {
                sscommandes.add(new SousCommande(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sscommandes;
    }

    /**
     * Méthode pour obtenir la liste d'attente des plats pour enfants
     *
     * @param conn connection à la base
     */
    private ArrayList<SousCommande> getListeAttenteEnfant(Connection conn) {
        ArrayList<SousCommande> sscommandes = new ArrayList<SousCommande>();
        try {

            ResultSet rs = GestionBDD.executeSelect(conn, "SELECT souscommande.idsouscommande, plat.nomplat FROM souscommande JOIN plat ON plat.idplat = souscommande.plat WHERE souscommande.etatsouscommande = 'commande' AND plat.nomplat = 'Menu enfant' ORDER BY souscommande.heurecommande ASC");
            while (rs.next()) {
                sscommandes.add(new SousCommande(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sscommandes;
    }
}
