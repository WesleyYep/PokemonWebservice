package nz.ac.auckland.pokemon.test;

import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.dto.PokemonDTO;
import nz.ac.auckland.pokemon.services.PokemonMapper;
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
 * @author Ian Warren
 *
 */
public class ZPokemonResourceTest
{
    private Logger _logger = LoggerFactory.getLogger(ZPokemonResourceTest.class);

    @Test
    public void testsPass() {}

    @Test
    public void testPokemonResource()
    {
        // Use ClientBuilder to create a new client that can be used to create
        // connections to the Web service.
        Client client = ClientBuilder.newClient();
        try {
            _logger.info("Creating a new Pokemon ...");

            PokemonDTO pikachu = new PokemonDTO("pikachu", "pika", Gender.MALE, 5);
            // Send a HTTP POST message, with a message body containing the XML,
            // to the Web service.
            Response response = client.target("http://localhost:10000/services/pokemon")
                    .request().post(Entity.xml(pikachu));

            // Expect a HTTP 201 "Created" response from the Web service.
            int status = response.getStatus();

            if (status != 201) {
                _logger.error("Failed to create Pokemon; Web service responded with: " + status);
                fail();
            }

            // Extract location header from the HTTP response message. This should
            // give the URI for the newly created Trainer.
            String location = response.getLocation().toString();
            _logger.info("URI for new Pokemon: " + location);
            String[] array = location.split("/");
            int id = Integer.parseInt(array[array.length-1]);
            _logger.info("ID for new Pokemon: " + id);

            // Close the connection to the Web service.
            response.close();

            // Query the Web service for the new Trainer. Send a HTTP GET request.
            _logger.info("Querying the Pokemon ...");
            PokemonDTO pokemonDTO = client.target(location).request().get(PokemonDTO.class);
            Pokemon pokemon = PokemonMapper.toDomainModel(pokemonDTO);
            _logger.info("Retrieved Pokemon:\n" + pokemonDTO.toString());
            _logger.info("Trainer is: " + TestConstants.trainerId);

            // Send a HTTP PUT request to the Web service. The request URI is
            // that retrieved from the Web service (the response to the GET message)
            // and the message body is the above XML.
            location += "/" + TestConstants.trainerId;
            _logger.info("URI for sending caught PUT request: " + location);

            response = client.target(location).request().put(null);
            status = response.getStatus();
            if (status != 201) {
                _logger.error("Failed to catch Pokemon; Web service responded with: " + status);
                fail();
            }
//
//            // Extract location header from the HTTP response message. This should
//            // give the URI for the newly created Trainer.
//            location = response.getLocation().toString();
//            _logger.info("URI for updated Trainer: " + location);
//            array = location.split("/");
//            id = Integer.parseInt(array[array.length-1]);
//            _logger.info("ID for updated Trainer: " + id);
//            response.close();
//
//            // Query the Web service for the new Trainer. Send a HTTP GET request.
//            _logger.info("Querying the updated Trainer ...");
//            pokemonDTO = client.target(location).request().get(TrainerDTO.class);
//            // Trainer pokemon = TrainerMapper.toDomainModel(pokemonDTO);
//            _logger.info("Retrieved Trainer:\n" + pokemonDTO.toString());

        } finally {
            // Release any connection resources.
            client.close();
        }
    }
}

