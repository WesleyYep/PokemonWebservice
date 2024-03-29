package nz.ac.auckland.pokemon.test;

import static org.junit.Assert.fail;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.dto.*;
import nz.ac.auckland.setup.test.InitialiseTest;
import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for Testing the TrainerResource
 * The test is implemented using the JAX-RS client API
 *
 * @author Wesley Yep
 *
 */
public class TrainerResourceTest
{
    private Logger _logger = LoggerFactory.getLogger(TrainerResourceTest.class);

    @BeforeClass
    public static void initializeIfNeeded() {

        InitialiseTest.init();
    }

    @Test
    public void testsPass() {}

    /**
     * This method tests getting the JSON data from the web service, rather than XML
     * This webservice method can produce both XML and JSON
     */
    @Test
    public void testGetJSONTrainer() {
        Client client = ClientBuilder.newClient();
        try {
            TrainerDTO trainerDTO = client.target(InitialiseTest.webserviceURL + "/services/trainers/1").request().accept(MediaType.APPLICATION_JSON).get(TrainerDTO.class);
            _logger.info("Got Trainer from json: " + trainerDTO.toString());
        } finally {
            client.close();
        }
    }

    @Test
    public void testTrainerResource()
    {
        // Use ClientBuilder to create a new client that can be used to create
        // connections to the Web service.
        Client client = ClientBuilder.newClient();
        try {
            _logger.info("Creating a new Trainer ...");

            TrainerDTO ash = new TrainerDTO("ketchum", "ash", Gender.MALE, new LocalDate(1958, 5, 17), new Record());
            // Send a HTTP POST message, with a message body containing the XML,
            // to the Web service.
            Response response = client.target(InitialiseTest.webserviceURL + "/services/trainers")
                    .request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).post(Entity.xml(ash));

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
            TrainerDTO updateTrainer = new TrainerDTO(ash.getLastName(), "newAsh", Gender.MALE, new LocalDate(1960, 5, 17), new Record());

            // Send a HTTP PUT request to the Web service. The request URI is
            // that retrieved from the Web service (the response to the GET message)
            // and the message body is the above XML.
            response = client.target(location).request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).put(Entity.xml(updateTrainer));
            status = response.getStatus();
            if (status != 201) {
                _logger.error("Failed to update Trainer; Web service responded with: " + status);
                fail();
            }

            response.close();


            //now add a new trainer and test adding a contact
            TrainerDTO brock = new TrainerDTO("Harrison", "Brock", Gender.MALE, new LocalDate(1999, 4, 12), new Record());
            response = client.target(InitialiseTest.webserviceURL + "/services/trainers")
                    .request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).post(Entity.xml(brock));
            status = response.getStatus();
            if (status != 201) {
                _logger.error("Failed to create Trainer; Web service responded with: " + status);
                fail();
            }
            location = response.getLocation().toString() + "/" + id;
            _logger.info("Trying to update contact at location: " + location);
            response.close();
            response = client.target(location).request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).put(null);
            status = response.getStatus();
            if (status != 201) {
                _logger.error("Failed to add contact; Web service responded with: " + status);
                fail();
            }
            response.close();

            //now get a list of HATEOAS trainers
            _logger.info("Querying the Trainer list ...");
            TrainerListDTO trainerListDTO = client.target(InitialiseTest.webserviceURL + "/services/trainers/all")
                    .queryParam("start", 0)
                    .queryParam("size", 3)
                    .request()
                    .get(TrainerListDTO.class);
            _logger.info("Retrieved trainer list - next: " + trainerListDTO.getNext());
            _logger.info("Retrieved trainer list - previous: " + trainerListDTO.getPrevious());
            _logger.info("Retrieved trainer list - size: " + trainerListDTO.getTrainers().size());

        } finally {
            client.close();
        }
    }

    /**
     * Test getting all the pokemon from a trainer
     */
    @Test
    public void testTrainerResourceGetPokemon()
    {
        Client client = ClientBuilder.newClient();
        try {
            //get a list of HATEOAS pokemon that have been caught by that trainer
            _logger.info("Querying the Trainer list ...");
            PokemonListDTO pokemonListDTO = client.target(InitialiseTest.webserviceURL + "/services/trainers/1/getPokemon")
                    .queryParam("start", 0)
                    .queryParam("size", 3)
                    .request()
                    .get(PokemonListDTO.class);
            _logger.info("Retrieved pokemon list - next: " + pokemonListDTO.getNext());
            _logger.info("Retrieved pokemon list - previous: " + pokemonListDTO.getPrevious());
            _logger.info("Retrieved pokemon list - size: " + pokemonListDTO.getPokemons().size());

        } finally {
            client.close();
        }
    }

    /**
     * Test getting the contacts of a trainer
     */
    @Test
    public void testTrainerResourceGetContacts()
    {
        Client client = ClientBuilder.newClient();
        try {
            //get a list of HATEOAS trainer that are contacts of that trainer
            _logger.info("Querying the Contacts list ...");
            TrainerListDTO trainerListDTO = client.target(InitialiseTest.webserviceURL + "/services/trainers/4/getContacts")
                    .queryParam("start", 0)
                    .queryParam("size", 3)
                    .request()
                    .get(TrainerListDTO.class);
            _logger.info("Retrieved contacts list - next: " + trainerListDTO.getNext());
            _logger.info("Retrieved contacts list - previous: " + trainerListDTO.getPrevious());
            _logger.info("Retrieved contacts list - size: " + trainerListDTO.getTrainers().size());

        } finally {
            client.close();
        }
    }

    /**
     * Test getting battles of a trainer
     */
    @Test
    public void testTrainerResourceGetBattles()
    {
        Client client = ClientBuilder.newClient();
        try {
            //get a list of HATEOAS battles that have been fought by that trainer
            _logger.info("Querying the Contacts list ...");
            BattleListDTO battleListDTO = client.target(InitialiseTest.webserviceURL + "/services/trainers/1/getBattles")
                    .queryParam("start", 0)
                    .queryParam("size", 3)
                    .request()
                    .get(BattleListDTO.class);
            _logger.info("Retrieved battles list - next: " + battleListDTO.getNext());
            _logger.info("Retrieved battles list - previous: " + battleListDTO.getPrevious());
            _logger.info("Retrieved battles list - size: " + battleListDTO.getBattles().size());

        } finally {
            client.close();
        }
    }

    /**
     * Test getting the trainer's team
     */
    @Test
    public void testTrainerResourceGetTeam() {
        Client client = ClientBuilder.newClient();
        try {
            _logger.info("Querying the trainer's team...");
             TeamDTO t = client.target(InitialiseTest.webserviceURL + "/services/trainers/1/team")
                    .request()
                    .get(TeamDTO.class);
            _logger.info("Retrieved the team: " + t.getTeamName());

            PokemonListDTO pokemonListDTO = client.target(InitialiseTest.webserviceURL + "/services/team/" + t.getId() + "/pokemon")
                    .request()
                    .get(PokemonListDTO.class);
            _logger.info("Retrieved team's pokemon - size of team: " + pokemonListDTO.getPokemons().size());

        } finally {
            client.close();
        }
    }


}

