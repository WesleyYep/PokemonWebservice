package nz.ac.auckland.pokemon.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resources;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import nz.ac.auckland.pokemon.dto.TrainerDTO;

import nz.ac.auckland.pokemon.dto.TrainerListDTO;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.domain.Gender;

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
	@Produces("application/xml")
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
	public TrainerListDTO findAll(@QueryParam("start") int start, @QueryParam("size") int size) {
		List<Trainer> trainers = em.createQuery("SELECT t FROM Trainer t").getResultList();
		List<TrainerDTO> trainerDTOs = new ArrayList<TrainerDTO>();

		for (int i = start; i < start + size; i++) {
			trainerDTOs.add(TrainerMapper.toDto(trainers.get(i)));
		}

		List<Link> links = new ArrayList<Link>();
		TrainerListDTO trainersDTO = new TrainerListDTO();
		URI nextUri, previousUri;
		if (start + size < trainers.size()) {
			nextUri = URI.create("http://localhost:10000/services/trainers?start=" + (start+size) + "&size=" + size);
		} else {
			nextUri = URI.create("");
		}
		if (start - size >= 0) {
			previousUri = URI.create("http://localhost:10000/services/trainers?start=" + (start-size) + "&size=" + size);
		} else {
			previousUri = URI.create("");
		}
		_logger.info("next uri generated is: " + nextUri);
		_logger.info("previous uri generated is: " + previousUri);
		links.add(Link.fromUri(nextUri).rel("next").type(MediaType.APPLICATION_XML).build());
		links.add(Link.fromUri(previousUri).rel("previous").type(MediaType.APPLICATION_XML).build());

		trainersDTO.setLinks(links);
		trainersDTO.setTrainers(trainerDTOs);
		return trainersDTO;
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
