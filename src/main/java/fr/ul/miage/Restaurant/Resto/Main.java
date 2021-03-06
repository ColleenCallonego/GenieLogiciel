package fr.ul.miage.Restaurant.Resto;

import fr.ul.miage.Restaurant.Resto.Utilisateurs.*;

import java.sql.*;
import java.util.Scanner;

public class Main {

    static String userType;
    static String userId;
    public static Scanner scan=new Scanner(System.in);

    public static void main(String[] args) {

        Connexion();
        Utilisateur u;
        switch (userType) {
            case "Directeur":
                u = new Directeur(userId);
                break;
            case "MaitreHotel":
                u = new MaitreH(userId);
                break;
            case "Serveur":
                u = new Serveur(userId);
                break;
            case "Assistant":
                u = new Assistant(userId);
                break;
            case "Cuisinier":
                u = new Cuisinier(userId);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + userType);
        }

        int answer;
        do{
            answer=u.afficherPrincipal();
            if (answer > 0){
                u.appelMethode(answer);
            }
        }while(answer!=0);

    scan.close();



    }

    private static void Connexion() {

        String id, mdp;
        do {
            System.out.println("Saisissez votre identifiant:");
            id = scan.nextLine();
            System.out.println("Saisissez votre mot de passe:");
            mdp = scan.nextLine();

        } while (!identification(id, mdp));

    }

    public static boolean identification(String id, String mdp) {
        boolean connected = false;
        try {
            String url = "jdbc:postgresql://plg-broker.ad.univ-lorraine.fr/Restaurant_G8";
            String query = "SELECT * FROM utilisateur WHERE idutili='" + id + "' and mdp='" + mdp + "'";
            Connection conn = DriverManager.getConnection(url, "m1user1_03", "m1user1_03");
            Statement st = conn.createStatement();
            ResultSet rs = (st).executeQuery(query);
            if (rs.next()) {
                System.out.println("vous etes maintenant connecte en tant que: " + id);
                userType = rs.getString("typeutili");
                userId = id;
                connected = true;
            } else {
                System.out.println("Identifiant ou mot de passe invalide");
                connected = false;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connected;
    }
}