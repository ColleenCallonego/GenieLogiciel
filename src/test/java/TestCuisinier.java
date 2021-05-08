import fr.ul.miage.Restaurant.Resto.Plat;
import fr.ul.miage.Restaurant.Resto.Utilisateurs.Cuisinier;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCuisinier {
    @Test
    void testVerifNomPlatIdentique(){
        Cuisinier c = new Cuisinier("PontTi");
        Plat p = new Plat(1, "Steak", 5, "Viande");
        ArrayList<Plat> list = new ArrayList<Plat>();
        list.add(p);
        assertEquals(false, c.verifNomPlat("Steak", list));
    }

    @Test
    void testVerifNomPlatPasIdentique(){
        Cuisinier c = new Cuisinier("PontTi");
        Plat p = new Plat(1, "Steak", 5, "Viande");
        ArrayList<Plat> list = new ArrayList<Plat>();
        list.add(p);
        assertEquals(true, c.verifNomPlat("Frites", list));
    }

    @Test
    void testConnect(){
        Cuisinier c = new Cuisinier("PontTi");
        assertNotNull(c.connect());
    }

}
