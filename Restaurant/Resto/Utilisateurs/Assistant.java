package Restaurant.Resto.Utilisateurs;

import Restaurant.Resto.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Assistant extends Utilisateur {
    private ArrayList<Table> listTables;

    public Assistant(String id) {
        super(id);
        listTables = new ArrayList<>();
    }

    @Override
    public Integer afficherPrincipal() {
        return null;// pour le moment pas encore fait
    }

    public void recupTables() {
        try {
            String url = "jdbc:postgresql://plg-broker.ad.univ-lorraine.fr/Restaurant_G8";
            Connection conn = DriverManager.getConnection(url, "m1user1_03", "m1user1_03");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tableresto ORDER BY numero");
            while (rs.next()) {
                listTables
                        .add(new Table(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour afficher l'écran d'une table au fois que l'assitant l'a choisie.
     *
     * @param numero numéro de la table concernée
     * @return choix de l'assistant
     */
    public static Integer EcranTableAssitant(Integer numero) {
        int rep = -1;
        Scanner scan = new Scanner(System.in);
        String n = System.getProperty("line.separator");
        System.out.println("--------------------------------------" + n + "Bienvenue à la table numéro " + numero + n
                + "--------------------------------------" + n + "1. Changer le status de la table" + n
                + "0. Retourner à l'écran principal" + n + n + n + "Que voulez vous faire?");
        try {
            rep = scan.nextInt();
            if (!verif(rep, 2)) {
                System.out.println("Entrée non valide");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrée non valide");
            rep = -1;
        }
        scan.close();
        return rep;
    }
}
