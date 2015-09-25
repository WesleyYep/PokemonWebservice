package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Battle;
import nz.ac.auckland.pokemon.domain.GeoPosition;
import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.dto.BattleDTO;
import nz.ac.auckland.pokemon.dto.PokemonDTO;
import nz.ac.auckland.pokemon.dto.TrainerDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Webservice methods related to Pokemon battles between trainers
 *
 * @author Wesley Yep
 *
 */
@Path("/battles")
public class BattleResource {
	// Setup a Logger.
	private static Logger _logger = LoggerFactory.getLogger(BattleResource.class);
	private static EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
			.createEntityManager();
	private Executor executor = Executors.newSingleThreadExecutor();
	List<AsyncResponse> responses = new ArrayList<AsyncResponse>();

	/**
	 * For now, create a battle by passing in a single BattleDBO
	 * This may change later to pass in a single Trainer object to register for a battle
	 */
	@POST
	@Consumes("application/xml")
	public Response createBattle(BattleDTO battleDTO) {
		_logger.debug("Read battle: " + battleDTO);
		em.getTransaction().begin();

		Battle battle = BattleMapper.toDomainModel(battleDTO);
		Trainer t1 = TrainerMapper.toDomainModel(battleDTO.getFirstTrainer());
		Trainer t2 = TrainerMapper.toDomainModel(battleDTO.getSecondTrainer());
		t1.setRecord(new Record());
		t2.setRecord(new Record());
		t1.addToRecord(battle);
		t2.addToRecord(battle);

		em.persist(battle);
		em.getTransaction().commit();

		_logger.debug("Created battle: " + battle);
		return Response.created(URI.create("/battles/" + battle.getId()))
				.build();
	}
	
	/**
	 * Handles incoming HTTP GET requests for the relative URI "battles/{id}.
	 */
	@GET
	@Path("/challenge")
	@Produces("application/xml")
	public synchronized void registerForBattle(@Suspended AsyncResponse response) {
		_logger.info("adding async to list");
		responses.add(response); //register as looking for a battle
	}

	@POST
	@Path("/challenge")
	@Consumes("application/xml")
	public synchronized void acceptBattle(TrainerDTO trainer) {

		if (responses.size() > 0) {
			_logger.info("popping from list");

			responses.remove(responses.size()-1).resume(trainer); //tell the the first trainer that this trainer is accepting their battle request
		}
	}

	
	
	/**
	 * Handles incoming HTTP GET requests for the relative URI "battles/{id}.
	 * @param id the unique id of the Trainer to retrieve.
	 * @return a StreamingOutput object storing a representation of the required
	 *         Trainer in XML format.
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public BattleDTO getBattle(@PathParam("id") long id) {
		_logger.debug("Retrieving battle: " + id);
		em.getTransaction().begin();
		Battle battle = em.find(Battle.class, id);
		BattleDTO battleDTO = BattleMapper.toDto(battle);
		em.getTransaction().commit();
		_logger.debug("Retrieved battle: " + battle);

		return battleDTO;
	}



	/**
	 * Handles incoming HTTP PUT requests for the relative URI "battles/{id}.
	 * a XML representation of the updated Trainer.
	 * This is a standard PUT request that updates the pokemon
	 */
	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public Response updateBattle(@PathParam("id") long id, BattleDTO battleDTO) {
		em.getTransaction().begin();
		Battle battle = em.find(Battle.class, id);
		_logger.info("Retrieved sent Battle:\n" + battleDTO.toString());

		// Update the details of the Battle to be updated.
	//	battle.setStartTime(battleDTO.getStartTime().toDate());
		battle.setEndTime(battleDTO.getEndTime().toDate());
	//	battle.setFirstTrainer(TrainerMapper.toDomainModel(battleDTO.getFirstTrainer()));
	//	battle.setSecondTrainer(TrainerMapper.toDomainModel(battleDTO.getSecondTrainer()));
		battle.setWinnerId(battleDTO.getWinnerId());

		em.persist(battle);
		em.getTransaction().commit();
		_logger.debug("Updated battle: " + battle);
		return Response.created(URI.create("/battles/" + battle.getId()))
				.build();
	}

}
