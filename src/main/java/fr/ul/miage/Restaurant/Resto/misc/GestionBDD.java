package fr.ul.miage.Restaurant.Resto.misc;

import java.sql.*;

public class GestionBDD {
    /**
     * Méthode pour se connecter à la base de donnée
     *
     * @return la connection à la base
     */
    public static Connection connect() {
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

    /**
     * Méthode pour executer des requetes de type SELECT
     *
     * @return l'ensemble des resultats de la requete
     */
    public  static ResultSet executeSelect(Connection conn, String query){
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            conn.close();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Méthode pour executer des requetes de type UPDATE
     *
     *
     */
    public static void executeUpdate(Connection conn, String query){
        try {
            Statement st = conn.createStatement();
            st.executeUpdate(query);
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
