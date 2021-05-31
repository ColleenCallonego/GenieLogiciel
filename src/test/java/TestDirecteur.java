import fr.ul.miage.Restaurant.Resto.Utilisateurs.Directeur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDirecteur {
    Directeur d;
    Connection conn;

    @BeforeEach
    void initialisation(){
        d = new Directeur("directeur");
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
    void testgetIdCarteDuJour() throws ParseException {
        Date da = new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-16");
        assertEquals(d.getIdCarteDuJour(conn, da), 5);
    }

    @Test
    void testgetPlats(){
        assertEquals(d.getPlats(conn).size(), 13);
    }
}
