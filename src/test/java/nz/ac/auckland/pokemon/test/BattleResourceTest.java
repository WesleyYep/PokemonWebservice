package nz.ac.auckland.pokemon.test;

import nz.ac.auckland.pokemon.domain.GeoPosition;
import nz.ac.auckland.pokemon.dto.BattleDTO;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import nz.ac.auckland.setup.test.InitialiseTest;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

import static org.junit.Assert.fail;

/**
 * Simple JUnit test case to test the behaviour of the Pokemon Web service.
 * The test basically uses the Web service to create a new Battle, to query it,
 * to update it and to requery it.
 *
 * The test is implemented using the JAX-RS client API, which will be covered
 * later.
 *
 * @author Wesley Yep
 *
 */
public class BattleResourceTest
{
    private Logger _logger = LoggerFactory.getLogger(BattleResourceTest.class);

    @BeforeClass
    public static void initializeIfNeeded() {
        InitialiseTest.init();
    }


    @Test
    public void testsPass() {}

    @Test
    public void testBattlesResource()
    {
        // Use ClientBuilder to create a new client that can be used to create
        // connections to the Web service.
        final Client client = ClientBuilder.newClient();
        final Client client2 = ClientBuilder.newClient();
        _logger.info("starting battles tests");

        try {
            //now get one client to send battle request and accept request asynchronously
            final WebTarget target = client.target(InitialiseTest.webserviceURL + "/services/battles/challenge");
    		target.request()
    				 .async()
            		 .get(new InvocationCallback<TrainerDTO>() {
                         public void completed( TrainerDTO opponent ) {
                             _logger.info("callback was called!");
                            createBattle(opponent); //create the battle once the callback is called
                         }
                         public void failed( Throwable t) {
                             _logger.error("Failed to call second response");
                         }
                     });

            _logger.info("sending post to battle accept");

            //new trainer will send a post request to accept the battle request from the first trainer

            //Query the Web service for another trainer which we will use as the second trainer that accepts the battle
            _logger.info(("Querying trainer John"));
            TrainerDTO john = client2.target(InitialiseTest.webserviceURL + "/services/trainers?firstName=john&lastName=jones&dob=1991-07-07").request().get(TrainerDTO.class);
            _logger.info("Retrieved Trainer John:\n" + john.toString());

            //send the POST request to server to accept the challenge
            //this should actually occur before the previous callback
            Response response = client2.target(InitialiseTest.webserviceURL + "/services/battles/challenge")
                    .request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).post(Entity.xml(john));
            int status = response.getStatus();

            if (status >= 400) {
                _logger.error("Failed to accept Battle; Web service responded with: " + status);
                fail();
            }



        } finally {
            client.close();
            client2.close();
        }
    }

    /**
     * This is called by the callback method to create a battle once the response from server was resumed
     */
    private void createBattle(TrainerDTO opponent) {
        Client client = ClientBuilder.newClient();

        _logger.info(("Querying trainer Harry"));
        TrainerDTO harry = client.target(InitialiseTest.webserviceURL + "/services/trainers?firstName=harry&lastName=potter&dob=1997-02-16").request().get(TrainerDTO.class);
        _logger.info("Retrieved Trainer Harry:\n" + harry.toString());

        BattleDTO defaultBattle = new BattleDTO(new DateTime(2015, 9, 24, 21, 57), new DateTime(2015, 9, 24, 21, 57),
                harry, opponent, 19, new GeoPosition(120.0, 40.20)); //end time is initially same as start time

        Response response = client.target(InitialiseTest.webserviceURL + "/services/battles")
                .request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).post(Entity.xml(defaultBattle));
        int status = response.getStatus();

        _logger.info("Location: " + response.getLocation().toString());
        if (status != 201) {
            _logger.error("Failed to create Battle; Web service responded with: " + status);
            fail();
        }
        client.close();
        //finish carrying out the tests now
        getBattleAndUpdate(response);
    }

    /**
     * After accepting the battle, now see if we can query the battle and the update it with a PUT request to signal that the battle has ended
     * and that there is a winner
     */
    private void getBattleAndUpdate(Response response) {
        Client client = ClientBuilder.newClient();

        // Extract location header from the HTTP response message. This should
        // give the URI for the newly created Battle.
        String location = response.getLocation().toString();
        _logger.info("URI for new Battle: " + location);
        String[] array = location.split("/");
        int id = Integer.parseInt(array[array.length-1]);
        _logger.info("ID for new Battle: " + id);

        // Close the connection to the Web service.
        response.close();

        // Query the Web service for the new Battle. Send a HTTP GET request.
        _logger.info("Querying the Battle ...");
        BattleDTO battleDTO = client.target(location).request().get(BattleDTO.class);
        _logger.info("Retrieved Battle:\n" + battleDTO.toString());

        BattleDTO updateBattle = new BattleDTO(battleDTO.getStartTime(), new DateTime(2015, 9, 24, 22, 10), battleDTO.getFirstTrainer(),
                battleDTO.getSecondTrainer(), battleDTO.getFirstTrainer().getId(), battleDTO.getLocation());

        // Send a HTTP PUT request to the Web service. The request URI is
        // that retrieved from the Web service (the response to the GET message)
        // and the message body is the above XML.
        Response newResponse = client.target(location).request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).put(Entity.xml(updateBattle));
        int status = newResponse.getStatus();
        if (status != 201) {
            _logger.error("Failed to update Battle; Web service responded with: " + status);
            fail();
        }

        newResponse.close();
        client.close();
    }
}

