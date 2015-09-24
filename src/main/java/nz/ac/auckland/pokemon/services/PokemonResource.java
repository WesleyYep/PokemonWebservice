package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.dto.PokemonDTO;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Implementation of the TrainerResource interface.
 *
 * @author Ian Warren
 *
 */
@Path("/pokemon")
public class PokemonResource {
	// Setup a Logger.
	private static Logger _logger = LoggerFactory.getLogger(PokemonResource.class);
	private static EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
			.createEntityManager();
	// Thread-safe data structure. This is necessary because a single
	// TrainerResourceImpl instance will be created and used to handle all
	// incoming requests. The JAX-RS implementation uses a thread-per-request
	// model and so concurrent requests will concurrently access the
	// TrainerResourceImpl object.
	//private Map<Integer, Trainer> trainerDB = new ConcurrentHashMap<Integer, Trainer>();
	//private AtomicInteger _idCounter = new AtomicInteger();

	/**
	 * Adds a new Parolee to the system. The state of the new Parolee is
	 * described by a nz.ac.auckland.parolee.dto.Parolee object.
	 *
	 * @param pokemonDTO
	 *            the Parolee data included in the HTTP request body.
	 */
	@POST
	@Consumes("application/xml")
	public Response createPokemon(PokemonDTO pokemonDTO) {
		_logger.debug("Read pokemon: " + pokemonDTO);
		em.getTransaction().begin();

		Pokemon pokemon = PokemonMapper.toDomainModel(pokemonDTO);
	//	pokemon.setId(_idCounter.incrementAndGet());
	//	trainerDB.put(pokemon.getId(), pokemon);
		em.persist(pokemon);
		em.getTransaction().commit();

		_logger.debug("Created pokemon: " + pokemon);
		return Response.created(URI.create("/pokemon/" + pokemon.getId()))
				.build();
	}

	/**
	 * Handles incoming HTTP GET requests for the relative URI "trainers/{id}.
	 * @param id the unique id of the Trainer to retrieve.
	 * @return a StreamingOutput object storing a representation of the required
	 *         Trainer in XML format.
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public PokemonDTO getPokemon(@PathParam("id") long id) {
		_logger.debug("Retrieving pokemon: " + id);
		em.getTransaction().begin();
		// Get the full Parolee object from the database.
		Pokemon pokemon = em.find(Pokemon.class, id);

		// Convert the full Parolee to a short Parolee.
		PokemonDTO pokemonDTO = PokemonMapper.toDto(pokemon);
		em.getTransaction().commit();

		_logger.debug("Retrieved pokemon: " + pokemon);

		return pokemonDTO;
	}



	/**
	 * Handles incoming HTTP PUT requests for the relative URI "pokemon/{id}.
	 * a XML representation of the updated Trainer.
	 * This is a standard PUT request that updates the pokemon
	 */
	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public Response updatePokemon(@PathParam("id") long id, PokemonDTO pokemonDTO) {
		_logger.debug("Retrieving pokemon: " + id);
		em.getTransaction().begin();
		// Get the full Pokemon object from the database.
		Pokemon pokemon = em.find(Pokemon.class, id);

		// Update the details of the Pokemon to be updated.
        pokemon.setName(pokemonDTO.getName());
        pokemon.setNickname(pokemonDTO.getNickname());
        pokemon.setGender(pokemonDTO.getGender());
        pokemon.setLevel(pokemonDTO.getLevel());
        pokemon.setMoves(pokemonDTO.getMoves());

        em.persist(pokemon);
		em.getTransaction().commit();
		_logger.debug("Updated pokemon: " + pokemon);
        return Response.created(URI.create("/pokemon/" + pokemon.getId()))
                .build();
	}

	/**
	 * Handles incoming HTTP PUT requests for the relative URI "pokemon/{id}/{trainerId}.
	 * Clients will call this method and pass in both a Pokemon and Trainer DTO, represent the pokemon and the trainer that caught it
	 * .
	 */
	@PUT
	@Path("{id}/{trainerId}")
	@Consumes("application/xml")
	public Response caughtPokemon(@PathParam("id") long id, @PathParam("trainerId") long trainerId) {
		_logger.debug("Retrieving pokemon: " + id);
		em.getTransaction().begin();
		// Get the full pokemon and trainer object from the database.
		Trainer trainer = em.find(Trainer.class, trainerId);
        Pokemon pokemon = em.find(Pokemon.class, id);

        // Update the details of the Trainer to be updated.
		pokemon.setTrainer(trainer);
		trainer.getPokemon().add(pokemon);

		em.getTransaction().commit();
		_logger.debug("Updated pokemon: " + pokemon);
		return Response.created(URI.create("/pokemon/" + pokemon.getId()))
				.build();
	}


}
