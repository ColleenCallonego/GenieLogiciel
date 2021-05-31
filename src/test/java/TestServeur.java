import fr.ul.miage.Restaurant.Resto.Mp;
import fr.ul.miage.Restaurant.Resto.Utilisateurs.Cuisinier;
import fr.ul.miage.Restaurant.Resto.Utilisateurs.Serveur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServeur {
    Serveur s;
    Connection conn;

    @BeforeEach
    void initialisation(){
        s = new Serveur("debreckMo");
        String url = "jdbc:postgresql://plg-broker.ad.univ-lorraine.fr/RestaurantTest_G8";
        String user = "m1user1_03";
        String password = "m1user1_03";
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void recupTableTest(){
        s.recupTables(conn);
        assertEquals(8, s.listTables.size());
    }

    @Test
    void testRecupCategories(){
        assertEquals(6, s.recupCategories(conn).size());
    }

    @Test
    void testRecupPlats(){
        assertEquals(0, s.recupPlats(conn, 6).size());
    }

    @Test
    void testrecupCommande(){
        assertEquals(0, s.recupCommande(conn, 2).size());
    }

    @Test
    void testgetInfoMpPlat(){
        assertEquals(1, s.getInfoMpPlat(conn, 12).size());
    }

    @Test
    void testgetInfoMp(){
        assertEquals(0, s.getInfoMp(conn, new ArrayList<Mp>()).size());
    }

    @Test
    void testgetIdCommande(){
        assertEquals(-1, s.getIdCommande(conn, 2));
    }
}
