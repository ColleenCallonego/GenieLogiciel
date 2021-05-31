import fr.ul.miage.Restaurant.Resto.Plat;
import fr.ul.miage.Restaurant.Resto.SousCommande;
import fr.ul.miage.Restaurant.Resto.Utilisateurs.Assistant;
import fr.ul.miage.Restaurant.Resto.Utilisateurs.Cuisinier;
import fr.ul.miage.Restaurant.Resto.misc.GestionBDD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCuisinier {
    Cuisinier c;
    Connection conn;

    @BeforeEach
    void initialisation(){
        c = new Cuisinier("PontTi");
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
    void testVerifNomPlatIdentique(){
        Plat p = new Plat(1, "Steak", 5, "Viande");
        ArrayList<Plat> list = new ArrayList<Plat>();
        list.add(p);
        assertEquals(false, c.verifNomPlat("Steak", list));
    }

    @Test
    void testVerifNomPlatPasIdentique(){
        Plat p = new Plat(1, "Steak", 5, "Viande");
        ArrayList<Plat> list = new ArrayList<Plat>();
        list.add(p);
        assertEquals(true, c.verifNomPlat("Frites", list));
    }

    @Test
    void testConnect(){
        assertNotNull(GestionBDD.connect());
    }

    @Test
    void testVerifNumSame(){
        ArrayList<SousCommande> list = new ArrayList<SousCommande>();
        list.add(new SousCommande(1, "Frites"));
        assertEquals(true, c.verifNum(1, list));
    }

    @Test
    void testVerifNumNotSame(){
        ArrayList<SousCommande> list = new ArrayList<SousCommande>();
        list.add(new SousCommande(1, "Frites"));
        assertEquals(false, c.verifNum(5, list));
    }

    @Test
    void testgetIdPlat(){
        Integer result = c.getIdPlat(conn, "Burger");
        assertEquals(16, result);
    }

    @Test
    void testGetCategories(){
        assertEquals(6, c.getCategories(conn).size());
    }

    @Test
    void testgetPlats(){
        assertEquals(13, c.getPlats(conn).size());
    }

    @Test
    void testgetMPs(){
        assertEquals(7, c.getMPs(conn).size());
    }

    @Test
    void testgetListeAttente(){
        assertEquals(0, c.getListeAttente(conn).size());
    }

    @Test
    void testgetListeAttenteEnfant(){
        assertEquals(0, c.getListeAttenteEnfant(conn).size());
    }
}
