package Restaurant.Resto;

import java.sql.*;
import java.util.ArrayList;

public class Server {
    private int id;
    private ArrayList<Table> listTables;

    public Server(int id){
        this.id = id;
        listTables = new ArrayList<>();
    }

    public void recupTables(){
        try {
            String url= "jdbc:postgresql://plg-broker.ad.univ-lorraine.fr/Restaurant_G8";
            Connection conn= DriverManager.getConnection(url, "m1user1_03", "m1user1_03");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tableresto");
            while(rs.next()) {
                listTables.add(new Table(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
            }
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void afficherPrincipal(){
        recupTables();
        for (Table table : listTables){
            if (table.getServer() == id){
                System.out.println(ColorText.COL_GREEN + "[Table " + table.getNumero() + " ]" + ColorText.COL_RESET);
            }
            else{
                System.out.println(ColorText.COL_BRIGHT_WHITE + "[Table " + table.getNumero() + " ]" + ColorText.COL_RESET);
            }
        }
        System.out.println("Selectionner une table par son numéro [1-" + listTables.size() + "]");
        System.out.println("Ou se déconnecter [0]");
    }
}