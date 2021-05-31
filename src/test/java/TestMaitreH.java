import fr.ul.miage.Restaurant.Resto.Utilisateurs.Cuisinier;
import fr.ul.miage.Restaurant.Resto.Utilisateurs.MaitreH;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMaitreH {
    MaitreH m;
    Connection conn;

    @BeforeEach
    void initialisation(){
        m = new MaitreH("LamourJe");
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
    void testrecupTables(){
        m.recupTables(conn);
        assertEquals(m.listTables.size(), 8);
    }

    @Test
    void testrecupServeur(){
        m.recupServeur(conn);
        assertEquals(m.listServeur.size(), 4);
    }
}
