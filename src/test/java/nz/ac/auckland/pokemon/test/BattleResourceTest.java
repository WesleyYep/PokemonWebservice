package nz.ac.auckland.pokemon.test;

import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.dto.BattleDTO;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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

    @Test
    public void testsPass() {}

    @Test
    public void testBattlesResource()
    {
        // Use ClientBuilder to create a new client that can be used to create
        // connections to the Web service.
        Client client = ClientBuilder.newClient();
        try {
            TrainerDTO bob = new TrainerDTO("smith", "bob", Gender.MALE, new LocalDate(1958, 5, 17));
            TrainerDTO john = new TrainerDTO("smith", "john", Gender.MALE, new LocalDate(1960, 12, 12));

            BattleDTO defaultBattle = new BattleDTO(new DateTime(2015, 9, 24, 21, 57), new DateTime(2015, 9, 24, 22, 12),
                    bob, john, 19);
            Response response = client.target("http://localhost:10000/services/battles")
                    .request().post(Entity.xml(defaultBattle));
            int status = response.getStatus();

            if (status != 201) {
                _logger.error("Failed to create Trainer; Web service responded with: " + status);
                fail();
            }

//            // Extract location header from the HTTP response message. This should
//            // give the URI for the newly created Trainer.
//            String location = response.getLocation().toString();
//            _logger.info("URI for new Trainer: " + location);
//            String[] array = location.split("/");
//            int id = Integer.parseInt(array[array.length-1]);
//            _logger.info("ID for new Trainer: " + id);
//
//            // Close the connection to the Web service.
//            response.close();
//
//            TestConstants.trainerId = id;
//            // Query the Web service for the new Trainer. Send a HTTP GET request.
//            _logger.info("Querying the Trainer ...");
//            TrainerDTO trainerDTO = client.target(location).request().get(TrainerDTO.class);
//            // Trainer trainer = TrainerMapper.toDomainModel(trainerDTO);
//            _logger.info("Retrieved Trainer:\n" + trainerDTO.toString());
//
//
//            TrainerDTO updateTrainer = new TrainerDTO(ash.getLastName(), "newAsh", Gender.MALE, new LocalDate(1960, 5, 17));
//
//            // Send a HTTP PUT request to the Web service. The request URI is
//            // that retrieved from the Web service (the response to the GET message)
//            // and the message body is the above XML.
//            response = client.target(location).request().put(Entity.xml(updateTrainer));
//            status = response.getStatus();
//            if (status != 201) {
//                _logger.error("Failed to update Trainer; Web service responded with: " + status);
//                fail();
//            }
//
//            response.close();
//
//
//            //now add a new trainer and test adding a contact
//            TrainerDTO brock = new TrainerDTO("Harrison", "Brock", Gender.MALE, new LocalDate(1999, 4, 12));
//            response = client.target("http://localhost:10000/services/trainers")
//                    .request().post(Entity.xml(brock));
//            status = response.getStatus();
//            if (status != 201) {
//                _logger.error("Failed to create Trainer; Web service responded with: " + status);
//                fail();
//            }
//            location = response.getLocation().toString() + "/" + TestConstants.trainerId;
//            _logger.debug("Trying to update contact at location: " + location);
//            response.close();
//            response = client.target(location).request().put(null);
//            status = response.getStatus();
//            if (status != 201) {
//                _logger.error("Failed to add contact; Web service responded with: " + status);
//                fail();
//            }

        } finally {
            client.close();
        }
    }
}

