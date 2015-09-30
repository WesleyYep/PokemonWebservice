package nz.ac.auckland.pokemon.test;

import nz.ac.auckland.pokemon.domain.*;
import nz.ac.auckland.pokemon.dto.*;
import nz.ac.auckland.pokemon.services.PokemonMapper;
import nz.ac.auckland.pokemon.services.TrainerMapper;
import nz.ac.auckland.setup.test.InitialiseTest;
import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Unit test for Testing the TrainerResource
 * The test is implemented using the JAX-RS client API
 *
 * @author Wesley Yep
 *
 */
public class TeamResourceTest
{
    private Logger _logger = LoggerFactory.getLogger(TeamResourceTest.class);

    @BeforeClass
    public static void initializeIfNeeded() {
        InitialiseTest.init();
    }

    @Test
    public void testsPass() {}


    @Test
    public void testNewTeam() {
        Client client = ClientBuilder.newClient();
        try {
            //firstly get the trainer called jesse
            TrainerDTO trainerDTO = client.target("http://localhost:10000/services/trainers?firstName=jesse&lastName=smith&dob=1990-03-12")
                    .request()
                    .get(TrainerDTO.class);

            //now get some of jesse's pokemon
            PokemonListDTO pokemonListDTO = client.target("http://localhost:10000/services/trainers/" + trainerDTO.getId() + "/getPokemon?start=0&size=3")
                    .request()
                    .get(PokemonListDTO.class);

            List<Pokemon> pokemons = new ArrayList<Pokemon>();
            for (PokemonDTO p : pokemonListDTO.getPokemons()) {
                pokemons.add(PokemonMapper.toDomainModel(p));
            }

            //now create team (trainer and pokemon are transient
            TeamDTO team = new TeamDTO("awesomeTeam", "A", null, null);
            Response response = client.target("http://localhost:10000/services/team?trainerId=" + trainerDTO.getId())
                    .request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).post(Entity.xml(team));
            int status = response.getStatus();

            if (status != 201) {
                _logger.error("Failed to create Team; Web service responded with: " + status);
                fail();
            }
            // give the URI for the newly created Trainer.
            String location = response.getLocation().toString();
            _logger.info("URI for new Trainer: " + location);
            String[] array = location.split("/");
            int id = Integer.parseInt(array[array.length - 1]);
            _logger.info("ID for new Trainer: " + id);

            response.close();

            //now add a pokemon to the team
            response = client.target("http://localhost:10000/services/team/"+ id + "/updatePokemon")
                    .request().cookie(InitialiseTest.cookieUsername).cookie(InitialiseTest.cookiePassword).put(Entity.xml(pokemonListDTO));
            status = response.getStatus();

            if (status != 201) {
                _logger.error("Failed to create Team; Web service responded with: " + status);
                fail();
            }
            response.close();


        } finally {
            client.close();
        }
    }

    @Test
    public void testGetTeam() {
        Client client = ClientBuilder.newClient();
        try {
            //get the team with id = 2
            _logger.info("Querying the Team: 2");
            TeamDTO t = client.target("http://localhost:10000/services/team/2")
                    .request()
                    .get(TeamDTO.class);
            _logger.info("Retrieved team: " + t);

            PokemonListDTO pokemonListDTO = client.target("http://localhost:10000/services/team/" + t.getId() + "/pokemon")
                    .request()
                    .get(PokemonListDTO.class);
            _logger.info("Retrieved team's pokemon - size of team: " + pokemonListDTO.getPokemons().size());

        } finally {
            client.close();
        }
    }


}

