import fr.ul.miage.Restaurant.Resto.Utilisateurs.Assistant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAssistant {
    static Assistant a;
    static Connection conn;

    @BeforeAll
    static void initialisation(){
        a = new Assistant("rogersPr");
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
        a.recupTables(conn);
        assertEquals(8, a.listTables.size());
    }
}

