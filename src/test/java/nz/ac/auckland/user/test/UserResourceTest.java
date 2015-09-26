package nz.ac.auckland.user.test;

import nz.ac.auckland.audit.User;
import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.domain.GeoPosition;
import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.dto.BattleDTO;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import nz.ac.auckland.pokemon.test.TestConstants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.fail;

/**
 * Simple JUnit test case to test the behaviour of the Parolee Web service.
 * The test basically uses the Web service to create a new Parolee, to query it,
 * to update it and to requery it.
 *
 * The test is implemented using the JAX-RS client API, which will be covered
 * later.
 *
 * @author Wesley Yep
 *
 */
public class UserResourceTest
{
    private Logger _logger = LoggerFactory.getLogger(UserResourceTest.class);

    @Test
    public void testsPass() {}

    @Test
    public void testAjaxClient() {
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

    @Test
    public void testUserResource()
    {
        Client client = ClientBuilder.newClient();

        try {
            _logger.info("Creating a new User ...");

            User wesley = new User("wesleyyep", "wesley", "yep");
            // Send a HTTP POST message, with a message body containing the XML,
            // to the Web service.
            Response response = client.target("http://localhost:10000/services/user")
                    .request().post(Entity.xml(wesley));

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

