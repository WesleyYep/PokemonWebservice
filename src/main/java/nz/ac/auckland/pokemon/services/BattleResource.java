package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Battle;
import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.dto.BattleDTO;
import nz.ac.auckland.pokemon.dto.PokemonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

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
		if (t1.getRecord() == null) {
			t1.setRecord(new Record());
		}
		if (t2.getRecord() == null) {
			t2.setRecord(new Record());
		}
		t1.addToRecord(battle);
		t2.addToRecord(battle);
		em.persist(t1);
		em.persist(t2);
		em.persist(battle);
		em.getTransaction().commit();

		_logger.debug("Created battle: " + battle);
		return Response.created(URI.create("/battle/" + battle.getId()))
				.build();
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

		// Update the details of the Pokemon to be updated.
		battle.setStartTime(battleDTO.getStartTime().toDate());
		battle.setEndTime(battleDTO.getEndTime().toDate());
		battle.setFirstTrainer(TrainerMapper.toDomainModel(battleDTO.getFirstTrainer()));
		battle.setSecondTrainer(TrainerMapper.toDomainModel(battleDTO.getSecondTrainer()));
		battle.setWinnerId(battleDTO.getWinnerId());

		em.persist(battle);
		em.getTransaction().commit();
		_logger.debug("Updated battle: " + battle);
		return Response.created(URI.create("/battle/" + battle.getId()))
				.build();
	}

}
