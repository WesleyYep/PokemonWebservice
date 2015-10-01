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
		Trainer t1 = em.find(Trainer.class, battleDTO.getFirstTrainer().getId());
		Trainer t2 = em.find(Trainer.class, battleDTO.getSecondTrainer().getId());
		Battle battle = BattleMapper.toDomainModel(battleDTO);
		if (t1 != null) { battle.setFirstTrainer(t1); }
		if (t2 != null) { battle.setSecondTrainer(t2); }
		//battle.setSecondTrainer(TrainerMapper.toDomainModel(battleDTO.getSecondTrainer()));
		em.persist(battle);
		em.getTransaction().commit();

		_logger.debug("Created battle: " + battle);
		return Response.created(URI.create("/battles/" + battle.getId()))
				.build();
	}
	
	/**
	 * Handles incoming HTTP GET requests for the relative URI "battles/{id}.
	 * The server will add the client to the list of trainers waiting for a battle
	 * Adds the request to the async list, ready to be resumed when server changes state (by receiving an accept battle post)
	 */
	@GET
	@Path("/challenge")
	@Produces("application/xml")
	public synchronized void registerForBattle(@Suspended AsyncResponse response) {
		_logger.info("adding async to list");
		responses.add(response); //register as looking for a battle
	}

	/**
	 * Resumes the oldest response that has not yet been dealt with. The response is passed the details of the trainer that accepted the battle and resumed
	 *
	 * @param trainer the trainer that accepted the battle
	 */
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
	 * @param id the unique id of the Battle to retrieve.
	 * @return a StreamingOutput object storing a representation of the required
	 *         Battle in XML format.
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
	 * This is a standard PUT request that ends the battle and gives it a end time and sets the winner
	 */
	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public Response endBattle(@PathParam("id") long id, BattleDTO battleDTO) {
		em.getTransaction().begin();
		Battle battle = em.find(Battle.class, id);
		_logger.info("Retrieved sent Battle:\n" + battleDTO.toString());

		// Update the details of the Battle to be updated.
		battle.setEndTime(battleDTO.getEndTime().toDate());
		battle.setWinnerId(battleDTO.getWinnerId());

		battle.getFirstTrainer().getRecord().setBattlesPlayed(battle.getFirstTrainer().getRecord().getBattlesPlayed()+1);
		battle.getSecondTrainer().getRecord().setBattlesPlayed(battle.getSecondTrainer().getRecord().getBattlesPlayed()+1);

		if (battleDTO.getWinnerId() == battle.getFirstTrainer().getId()) {
			battle.getFirstTrainer().getRecord().setBattlesWon(battle.getFirstTrainer().getRecord().getBattlesWon() + 1);
		} else {
			battle.getSecondTrainer().getRecord().setBattlesWon(battle.getSecondTrainer().getRecord().getBattlesWon()+1);
		}

		em.persist(battle);
		em.getTransaction().commit();
		_logger.debug("Updated battle: " + battle);
		return Response.created(URI.create("/battles/" + battle.getId()))
				.build();
	}

}
