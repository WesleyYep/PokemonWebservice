package nz.ac.auckland.pokemon.test;

import nz.ac.auckland.pokemon.domain.Battle;
import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.domain.GeoPosition;
import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.dto.BattleDTO;
import nz.ac.auckland.pokemon.dto.TrainerDTO;

import nz.ac.auckland.pokemon.services.BattleMapper;
import nz.ac.auckland.setup.test.InitialiseTest;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

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
            final WebTarget target = client.target("http://localhost:10000/services/battles/challenge");
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
            //this should actually occur before the previous callback
            TrainerDTO john = new TrainerDTO("smith", "john", Gender.MALE, new LocalDate(1960, 12, 12) , new Record());
            Response response = client2.target("http://localhost:10000/services/battles/challenge")
                    .request().post(Entity.xml(john));
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

    private void createBattle(TrainerDTO opponent) {
        Client client = ClientBuilder.newClient();

        BattleDTO defaultBattle = new BattleDTO(new DateTime(2015, 9, 24, 21, 57), new DateTime(2015, 9, 24, 21, 57),
                TestConstants.trainer1, opponent, 19, new GeoPosition(120.0, 40.20)); //end time is initially same as start time
        Response response = client.target("http://localhost:10000/services/battles")
                .request().post(Entity.xml(defaultBattle));
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

    private void getBattleAndUpdate(Response response) {
        Client client = ClientBuilder.newClient();

        // Extract location header from the HTTP response message. This should
        // give the URI for the newly created Trainer.
        String location = response.getLocation().toString();
        _logger.info("URI for new Battle: " + location);
        String[] array = location.split("/");
        int id = Integer.parseInt(array[array.length-1]);
        _logger.info("ID for new Battle: " + id);

        // Close the connection to the Web service.
        response.close();

        // Query the Web service for the new Trainer. Send a HTTP GET request.
        _logger.info("Querying the Battle ...");
        BattleDTO battleDTO = client.target(location).request().get(BattleDTO.class);
        _logger.info("Retrieved Battle:\n" + battleDTO.toString());

        BattleDTO updateBattle = new BattleDTO(battleDTO.getStartTime(), new DateTime(2015, 9, 24, 22, 10), battleDTO.getFirstTrainer(),
                battleDTO.getSecondTrainer(), battleDTO.getFirstTrainer().getId(), battleDTO.getLocation());

        // Send a HTTP PUT request to the Web service. The request URI is
        // that retrieved from the Web service (the response to the GET message)
        // and the message body is the above XML.
        Response newResponse = client.target(location).request().put(Entity.xml(updateBattle));
        int status = newResponse.getStatus();
        if (status != 201) {
            _logger.error("Failed to update Battle; Web service responded with: " + status);
            fail();
        }

        newResponse.close();
        client.close();
    }
}

