package nz.ac.auckland.setup.test;

import nz.ac.auckland.audit.test.AuditResourceTest;
import nz.ac.auckland.pokemon.test.TrainerResourceTest;
import nz.ac.auckland.pokemon.test.BattleResourceTest;
import nz.ac.auckland.pokemon.test.PokemonResourceTest;
import nz.ac.auckland.user.test.UserResourceTest;
import org.junit.runners.Suite;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by Wesley on 26/09/2015.
 */
@Suite.SuiteClasses( { AuditResourceTest.class, TrainerResourceTest.class, BattleResourceTest.class, PokemonResourceTest.class, UserResourceTest.class})
public class InitialiseTest {

    private static boolean initialize = true;

    public static void init() {
        if(!initialize) {
            return;
        }
        System.out.print("Tests are initializing.");
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
