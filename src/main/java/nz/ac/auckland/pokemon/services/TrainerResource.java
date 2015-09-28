package nz.ac.auckland.pokemon.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nz.ac.auckland.pokemon.domain.Battle;
import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.domain.Trainer;

/**
 * Webservice methods for /trainers
 *
 * @author Wesley Yep
 *
 */
@Path("/trainers")
public class TrainerResource {
	// Setup a Logger.
	private static Logger _logger = LoggerFactory.getLogger(TrainerResource.class);
	private static EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
			.createEntityManager();

	/**
	 * Adds a new Trainer to the system. The state of the new Trainer is
	 * described by a nz.ac.auckland.pokemon.dto.Trainer object.
	 *
	 * @param trainerDTO
	 *            the Trainer data included in the HTTP request body.
	 */
	@POST
	@Consumes("application/xml")
	public Response createTrainer(TrainerDTO trainerDTO) {
		_logger.debug("Read trainer: " + trainerDTO);
		em.getTransaction().begin();

		Trainer trainer = TrainerMapper.toDomainModel(trainerDTO);
		trainer.setRecord(new Record());
		em.persist(trainer);
		em.getTransaction().commit();

		_logger.debug("Created trainer: " + trainer);
		return Response.created(URI.create("/trainers/" + trainer.getId()))
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
	@Produces({"application/xml","application/json"})
	public TrainerDTO getTrainer(@PathParam("id") long id) {
		_logger.debug("Retrieving trainer: " + id);
		em.getTransaction().begin();
		// Get the full Parolee object from the database.
		Trainer trainer = em.find(Trainer.class, id);

		// Convert the full Parolee to a short Parolee.
		TrainerDTO trainerDTO = TrainerMapper.toDto(trainer);
		em.getTransaction().commit();

		_logger.debug("Retrieved trainer: " + trainer);

		return trainerDTO;
	}

	@GET
	@Produces("application/xml")
	public TrainerDTO getTrainerByNameAndDOB(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
											 @QueryParam("dob") String dob) {
		_logger.info("Retrieving trainer: " + firstName + " " + lastName + " " + dob);
		em.getTransaction().begin();
		// Get the full Parolee object from the database.
		_logger.info("running query: SELECT t FROM Trainer t WHERE firstname='" + firstName + "' AND lastname='" + lastName + "' AND dateofbirth='" + dob + "'");
		Trainer trainer = (Trainer) em.createQuery("SELECT t FROM Trainer t WHERE firstname='" + firstName + "' AND lastname='" + lastName + "' AND dateofbirth='" + dob + "'").getSingleResult();

		// Convert the full Parolee to a short Parolee.
		TrainerDTO trainerDTO = TrainerMapper.toDto(trainer);
		em.getTransaction().commit();

		_logger.info("Retrieved trainer: " + trainer);

		return trainerDTO;
	}

	@GET
	@Path("all")
	@Produces("application/xml")
	public TrainerListDTO findAll(@QueryParam("start") int start, @QueryParam("size") int size) {
		em.getTransaction().begin();
		List<Trainer> trainers = em.createQuery("SELECT t FROM Trainer t").getResultList();
		List<TrainerDTO> trainerDTOs = new ArrayList<TrainerDTO>();
		_logger.info("all trainer number: " + trainers.size());

		for (int i = start; i < start + size; i++) {
			trainerDTOs.add(TrainerMapper.toDto(trainers.get(i)));
		}

		List<Link> links = new ArrayList<Link>();
		TrainerListDTO trainersDTO = new TrainerListDTO();
		URI nextUri, previousUri;
		if (start + size < trainers.size()) {
			nextUri = URI.create("http://localhost:10000/services/trainers?start=" + (start+size) + "&size=" + size);
		} else {
			nextUri = URI.create("no_next_uri");
		}
		if (start - size >= 0) {
			previousUri = URI.create("http://localhost:10000/services/trainers?start=" + (start-size) + "&size=" + size);
		} else {
			previousUri = URI.create("no_previous_uri");
		}
		_logger.info("next uri generated is: " + nextUri);
		_logger.info("previous uri generated is: " + previousUri);
		links.add(Link.fromUri(nextUri).rel("next").type(MediaType.APPLICATION_XML).build());
		links.add(Link.fromUri(previousUri).rel("previous").type(MediaType.APPLICATION_XML).build());

		trainersDTO.setLinks(links);
		trainersDTO.setTrainers(trainerDTOs);

		em.getTransaction().commit();
		return trainersDTO;
	}

	@GET
	@Path("{id}/getPokemon")
	@Produces("application/xml")
	public PokemonListDTO findAllPokemon(@QueryParam("start") int start, @QueryParam("size") int size, @PathParam("id") long trainerId) {
		em.getTransaction().begin();

		List<Pokemon> pokemons = em.createQuery("SELECT p FROM Pokemon p").getResultList();
		List<PokemonDTO> pokemonDTOs = new ArrayList<PokemonDTO>();
		_logger.info("all pokemon number: " + pokemons.size());
		int i = 0;
		while (pokemonDTOs.size() < size && i < pokemons.size()) {
			Trainer t = pokemons.get(i).getTrainer();
			if (t != null) {
				if (t.getId() == trainerId) {
					pokemonDTOs.add(PokemonMapper.toDto(pokemons.get(i)));
				}
			}
			i++;
		}

		List<Link> links = new ArrayList<Link>();
		PokemonListDTO pokemonListDTO = new PokemonListDTO();
		URI nextUri, previousUri;
		if (pokemonDTOs.size() == size) {
			nextUri = URI.create("http://localhost:10000/services/" + trainerId + "/getPokemon?start=" + (start+size) + "&size=" + size);
		} else {
			nextUri = URI.create("no_next_uri");
		}
		if (start - size >= 0) {
			previousUri = URI.create("http://localhost:10000/services/" + trainerId + "/getPokemon?start=" + (start-size) + "&size=" + size);
		} else {
			previousUri = URI.create("no_previous_uri");
		}
		_logger.info("next uri generated is: " + nextUri);
		_logger.info("previous uri generated is: " + previousUri);
		links.add(Link.fromUri(nextUri).rel("next").type(MediaType.APPLICATION_XML).build());
		links.add(Link.fromUri(previousUri).rel("previous").type(MediaType.APPLICATION_XML).build());

		pokemonListDTO.setLinks(links);
		pokemonListDTO.setPokemons(pokemonDTOs);

		em.getTransaction().commit();
		return pokemonListDTO;
	}

	@GET
	@Path("{id}/getContacts")
	@Produces("application/xml")
	public TrainerListDTO findAllContacts(@QueryParam("start") int start, @QueryParam("size") int size, @PathParam("id") long id) {

		em.getTransaction().begin();
		List<Trainer> trainers = em.createQuery("SELECT t FROM Trainer t").getResultList();

		List<TrainerDTO> trainerDTOs = new ArrayList<TrainerDTO>();
		Trainer trainer = em.find(Trainer.class, id);

		int i = 0;
		while(trainerDTOs.size() < size && i < trainers.size()) {
			if (trainer.getContacts().contains(trainers.get(i))) {
				trainerDTOs.add(TrainerMapper.toDto(trainers.get(i)));
			}
			i++;
		}

		List<Link> links = new ArrayList<Link>();
		TrainerListDTO trainersDTO = new TrainerListDTO();
		URI nextUri, previousUri;
		if (trainerDTOs.size() == size) {
			nextUri = URI.create("http://localhost:10000/services/trainers?start=" + (start+size) + "&size=" + size);
		} else {
			nextUri = URI.create("no_next_uri");
		}
		if (start - size >= 0) {
			previousUri = URI.create("http://localhost:10000/services/trainers?start=" + (start-size) + "&size=" + size);
		} else {
			previousUri = URI.create("no_previous_uri");
		}
		_logger.info("next uri generated is: " + nextUri);
		_logger.info("previous uri generated is: " + previousUri);
		links.add(Link.fromUri(nextUri).rel("next").type(MediaType.APPLICATION_XML).build());
		links.add(Link.fromUri(previousUri).rel("previous").type(MediaType.APPLICATION_XML).build());

		trainersDTO.setLinks(links);
		trainersDTO.setTrainers(trainerDTOs);

		em.getTransaction().commit();
		return trainersDTO;
	}

	@GET
	@Path("{id}/getBattles")
	@Produces("application/xml")
	public BattleListDTO findAllBattles(@QueryParam("start") int start, @QueryParam("size") int size, @PathParam("id") long trainerId) {
		em.getTransaction().begin();

		List<Battle> battles = em.createQuery("SELECT b FROM Battle b").getResultList();
		List<BattleDTO> battleDTOs = new ArrayList<BattleDTO>();
		_logger.info("all battle number: " + battles.size());
		int i = 0;
		while (battleDTOs.size() < size && i < battles.size()) {
			Trainer t1 = battles.get(i).getFirstTrainer();
			Trainer t2 = battles.get(i).getSecondTrainer();

			if (t1 != null && t1.getId() == trainerId || t2 != null && t2.getId() == trainerId) {
				battleDTOs.add(BattleMapper.toDto(battles.get(i)));
			}
			i++;
		}

		List<Link> links = new ArrayList<Link>();
		BattleListDTO battleListDTO = new BattleListDTO();
		URI nextUri, previousUri;
		if (battleDTOs.size() == size) {
			nextUri = URI.create("http://localhost:10000/services/" + trainerId + "/getBattles?start=" + (start+size) + "&size=" + size);
		} else {
			nextUri = URI.create("no_next_uri");
		}
		if (start - size >= 0) {
			previousUri = URI.create("http://localhost:10000/services/" + trainerId + "/getBattles?start=" + (start-size) + "&size=" + size);
		} else {
			previousUri = URI.create("no_previous_uri");
		}
		_logger.info("next uri generated is: " + nextUri);
		_logger.info("previous uri generated is: " + previousUri);
		links.add(Link.fromUri(nextUri).rel("next").type(MediaType.APPLICATION_XML).build());
		links.add(Link.fromUri(previousUri).rel("previous").type(MediaType.APPLICATION_XML).build());

		battleListDTO.setLinks(links);
		battleListDTO.setBattles(battleDTOs);

		em.getTransaction().commit();
		return battleListDTO;
	}

	/**
	 * Handles incoming HTTP PUT requests for the relative URI "trainers/{id}.
	 * a XML representation of the updated Trainer.
	 */
	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public Response updateTrainer(@PathParam("id") long id, TrainerDTO trainerDTO) {
		_logger.debug("Retrieving trainer: " + id);
		em.getTransaction().begin();
		// Get the full Parolee object from the database.
		Trainer trainer = em.find(Trainer.class, id);

		// Update the details of the Trainer to be updated.
		trainer.setFirstName(trainerDTO.getFirstName());
		trainer.setLastName(trainerDTO.getLastName());
		trainer.setGender(trainerDTO.getGender());
		trainer.setDateOfBirth(trainerDTO.getDateOfBirth());

		em.getTransaction().commit();
		_logger.debug("Updated trainer: " + trainer);
		return Response.created(URI.create("/trainers/" + trainer.getId()))
				.build();
	}

	/**
	 * Handles incoming HTTP PUT requests for the relative URI "trainers/{id}/{contact_id}.
	 * a XML representation of the updated Trainer.
	 * This method is used to add contacts between trainers
	 */
	@PUT
	@Path("{id}/{contact_id}")
	@Consumes("application/xml")
	public Response addTrainerContact (@PathParam("id") long id, @PathParam("contact_id") long contactId) {
		_logger.debug("Retrieving trainer: " + id + " and contact: " + contactId);
		em.getTransaction().begin();
		// Get the full Parolee object from the database.
		Trainer trainer = em.find(Trainer.class, id);
		Trainer contact = em.find(Trainer.class, contactId);

		// Update the contacts of the Trainer to be updated.
		trainer.addContact(contact);

		em.getTransaction().commit();
		_logger.debug("Updated trainer contact");
		return Response.created(URI.create("/trainers/" + trainer.getId()))
				.build();
	}


}
