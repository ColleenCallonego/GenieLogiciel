import fr.ul.miage.Restaurant.Resto.Plat;
import fr.ul.miage.Restaurant.Resto.SousCommande;
import fr.ul.miage.Restaurant.Resto.Utilisateurs.Cuisinier;
import fr.ul.miage.Restaurant.Resto.misc.GestionBDD;
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
        assertNotNull(GestionBDD.connect());
    }

    @Test
    void testVerifNumSame(){
        Cuisinier c = new Cuisinier("PontTi");
        ArrayList<SousCommande> list = new ArrayList<SousCommande>();
        list.add(new SousCommande(1, "Frites"));
        assertEquals(true, c.verifNum(1, list));
    }

    @Test
    void testVerifNumNotSame(){
        Cuisinier c = new Cuisinier("PontTi");
        ArrayList<SousCommande> list = new ArrayList<SousCommande>();
        list.add(new SousCommande(1, "Frites"));
        assertEquals(false, c.verifNum(5, list));
    }
}
