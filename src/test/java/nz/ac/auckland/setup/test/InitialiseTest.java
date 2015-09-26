package nz.ac.auckland.setup.test;

import nz.ac.auckland.audit.test.AuditResourceTest;
import nz.ac.auckland.pokemon.test.ATrainerResourceTest;
import nz.ac.auckland.pokemon.test.BattleResourceTest;
import nz.ac.auckland.pokemon.test.ZPokemonResourceTest;
import nz.ac.auckland.user.test.UserResourceTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Suite;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by Wesley on 26/09/2015.
 */
@Suite.SuiteClasses( { AuditResourceTest.class, ATrainerResourceTest.class, BattleResourceTest.class, ZPokemonResourceTest.class, UserResourceTest.class})
public class InitialiseTest {

    private static boolean initialize = true;

    public static void init() {
        System.out.print("Tests are initializing.");
        if(!initialize) {
            return;
        }
        initialize = false;

        //clear database
       clearDatabase();
        //set up database tables
        initializeDatabase();
    }

    private static void initializeDatabase() {
        Client client = ClientBuilder.newClient();
        client.target("http://localhost:10000/services/test/init")
                .request().post(null);
        client.close();
    }

    private static void clearDatabase() {
        Client client = ClientBuilder.newClient();
        client.target("http://localhost:10000/services/test/clearAllDB")
                .request().post(null);
        client.close();
    }

}
