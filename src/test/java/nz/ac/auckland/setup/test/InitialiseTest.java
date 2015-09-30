package nz.ac.auckland.setup.test;

import nz.ac.auckland.audit.User;
import nz.ac.auckland.pokemon.services.PasswordHasher;
import nz.ac.auckland.pokemon.test.TrainerResourceTest;
import nz.ac.auckland.pokemon.test.BattleResourceTest;
import nz.ac.auckland.pokemon.test.PokemonResourceTest;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.fail;

/**
 * Created by Wesley on 26/09/2015.
 */
public class InitialiseTest {

    public static Cookie cookieUsername;
    public static Cookie cookiePassword;
    private static Logger _logger = LoggerFactory.getLogger(InitialiseTest.class);

    private static boolean initialize = true;

    public static void init() {
        if(!initialize) {
            return;
        }
        System.out.print("Tests are initializing.");
        initialize = false;

        //set up cookies
        cookieUsername = new Cookie("username", "wesleyyep");
        cookiePassword = new Cookie("password", PasswordHasher.passwordHash("password"));

        //clear database
       clearDatabase();
        //set up database tables
        initializeDatabase();
        //create a user
        initializeUser();
        //test ajax client
        testAjaxClient();
    }

    private static void testAjaxClient() {
        try {
            _logger.info("Testing Ajax client ...");

            Desktop.getDesktop().browse(new URI("http://localhost:10000"));
            Thread.sleep(5000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private static void initializeUser () {
        Client client = ClientBuilder.newClient();

        try {
            _logger.info("Creating a new User ...");

            User wesley = new User("wesleyyep", "wesley", "yep", PasswordHasher.passwordHash("password"));
            // Send a HTTP POST message, with a message body containing the XML,
            // to the Web service.
            Response response = client.target("http://localhost:10000/services/user")
                    .request().post(Entity.xml(wesley));
            cookiePassword = response.getCookies().get("password");
            cookieUsername = response.getCookies().get("username");
            _logger.info("username cookie is: "+ cookieUsername.getValue());
            _logger.info("password cookie is: "+ cookiePassword.getValue());

            // Expect a HTTP 201 "Created" response from the Web service.
            int status = response.getStatus();

            if (status != 201) {
                _logger.error("Failed to create User; Web service responded with: " + status);
                fail();
            }

            // Extract location header from the HTTP response message. This should
            // give the URI for the newly created Trainer.
            String location = response.getLocation().toString();
            _logger.info("URI for new User: " + location);

            // Close the connection to the Web service.
            response.close();



        } finally {
            client.close();
        }
    }

}
