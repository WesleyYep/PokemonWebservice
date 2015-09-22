package nz.ac.auckland.pokemon.test;

import static org.junit.Assert.fail;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import nz.ac.auckland.pokemon.services.TrainerMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple JUnit test case to test the behaviour of the Parolee Web service.
 * The test basically uses the Web service to create a new Parolee, to query it,
 * to update it and to requery it.
 *
 * The test is implemented using the JAX-RS client API, which will be covered
 * later.
 *
 * @author Ian Warren
 *
 */
public class TrainerResourceTest
{
    private Logger _logger = LoggerFactory.getLogger(TrainerResourceTest.class);

    @Test
    public void testsPass() {}

    @Test
    public void testTrainerResource()
    {
        // Use ClientBuilder to create a new client that can be used to create
        // connections to the Web service.
        Client client = ClientBuilder.newClient();
        try {
            _logger.info("Creating a new Trainer ...");

            TrainerDTO ash = new TrainerDTO("ketchum", "ash", Gender.MALE, new LocalDate(1958, 5, 17));
            // Send a HTTP POST message, with a message body containing the XML,
            // to the Web service.
            Response response = client.target("http://localhost:10000/services/trainers")
                    .request().post(Entity.xml(ash));

            // Expect a HTTP 201 "Created" response from the Web service.
            int status = response.getStatus();

            if (status != 201) {
                _logger.error("Failed to create Trainer; Web service responded with: " + status);
                fail();
            }

            // Extract location header from the HTTP response message. This should
            // give the URI for the newly created Trainer.
            String location = response.getLocation().toString();
            _logger.info("URI for new Trainer: " + location);
            String[] array = location.split("/");
            int id = Integer.parseInt(array[array.length-1]);
            _logger.info("ID for new Trainer: " + id);

            // Close the connection to the Web service.
            response.close();


            // Query the Web service for the new Trainer. Send a HTTP GET request.
            _logger.info("Querying the Trainer ...");
            TrainerDTO trainerDTO = client.target(location).request().get(TrainerDTO.class);
            // Trainer trainer = TrainerMapper.toDomainModel(trainerDTO);
            _logger.info("Retrieved Trainer:\n" + trainerDTO.toString());


            TrainerDTO updateTrainer = new TrainerDTO(ash.getLastName(), "newAsh", Gender.MALE, new LocalDate(1960, 5, 17));

            // Send a HTTP PUT request to the Web service. The request URI is
            // that retrieved from the Web service (the response to the GET message)
            // and the message body is the above XML.
            response = client.target(location).request().put(Entity.xml(updateTrainer));
            status = response.getStatus();
            if (status != 201) {
                _logger.error("Failed to create Trainer; Web service responded with: " + status);
                fail();
            }

            // Extract location header from the HTTP response message. This should
            // give the URI for the newly created Trainer.
            location = response.getLocation().toString();
            _logger.info("URI for updated Trainer: " + location);
            array = location.split("/");
            id = Integer.parseInt(array[array.length-1]);
            _logger.info("ID for updated Trainer: " + id);
            response.close();

            // Query the Web service for the new Trainer. Send a HTTP GET request.
            _logger.info("Querying the updated Trainer ...");
            trainerDTO = client.target(location).request().get(TrainerDTO.class);
            // Trainer trainer = TrainerMapper.toDomainModel(trainerDTO);
            _logger.info("Retrieved Trainer:\n" + trainerDTO.toString());

        } finally {
            // Release any connection resources.
            client.close();
        }
    }
}
