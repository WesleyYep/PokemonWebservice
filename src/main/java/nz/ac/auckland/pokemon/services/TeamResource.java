package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.domain.Team;
import nz.ac.auckland.pokemon.domain.TeamDTO;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.dto.PokemonDTO;
import nz.ac.auckland.pokemon.dto.PokemonListDTO;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;

/**
 * Webservice methods related to Pokemon creating, modifying, and catching
 *
 * @author Wesley Yep
 *
 */
@Path("/team")
public class TeamResource {
	// Setup a Logger.
	private static Logger _logger = LoggerFactory.getLogger(TeamResource.class);
	private static EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
			.createEntityManager();

	/**
	 * Adds a new Pokemon to the system. The state of the new Pokemon is
	 * described by a nz.ac.auckland.pokemon.dto.PokemonDTO object.
	 *
	 * @param teamDTO
	 *            the Team data included in the HTTP request body.
	 */
	@POST
	@Consumes("application/xml")
	public Response createTeam(@QueryParam("trainerId") long trainerId, TeamDTO teamDTO) {
		_logger.debug("Read team: " + teamDTO);
		em.getTransaction().begin();

		Team team = TeamMapper.toDomainModel(teamDTO);
		Trainer trainer = em.find(Trainer.class, trainerId);
		team.setTrainer(trainer);
		em.persist(team);
		em.getTransaction().commit();

		_logger.debug("Created team: " + team);
		return Response.created(URI.create("/team/" + team.getId()))
				.build();
	}

	/**
	 * Handles incoming HTTP GET requests for the relative URI "pokemon/{id}.
	 * @param id the unique id of the Trainer to retrieve.
	 * @return a StreamingOutput object storing a representation of the required
	 *         Trainer in XML format.
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public TeamDTO getTeam(@PathParam("id") long id) {
		_logger.debug("Retrieving team: " + id);
		em.getTransaction().begin();
		// Get the full Team object from the database.
		Team team = em.find(Team.class, id);

		// Convert the full Team to a short Team.
		TeamDTO teamDTO = TeamMapper.toDto(team);
		em.getTransaction().commit();

		_logger.debug("Retrieved team: " + team);

		return teamDTO;
	}

	/**
	 * Handles incoming HTTP GET requests for the relative URI "pokemon/{id}.
	 * @param id the unique id of the Trainer to retrieve.
	 * @return a StreamingOutput object storing a representation of the required
	 *         Trainer in XML format.
	 */
	@GET
	@Path("{id}/pokemon")
	@Produces("application/xml")
	public PokemonListDTO getPokemonFromTeam(@PathParam("id") long id) {
		_logger.debug("Finding pokemon from team: " + id);
		em.getTransaction().begin();
		// Get the full Team object from the database.
		Team team = em.find(Team.class, id);

		Set<Pokemon> pokemons = team.getPokemon();

		PokemonListDTO pld = new PokemonListDTO();
		List<PokemonDTO> pokemonDTOs = new ArrayList<PokemonDTO>();
		for (Pokemon p : pokemons) {
			pokemonDTOs.add(PokemonMapper.toDto(p));
		}
		pld.setPokemons(pokemonDTOs);

		em.getTransaction().commit();

		_logger.debug("Retrieved team pokemon: " + team);

		return pld;
	}

	public Collection<PokemonDTO> convertToCollection(Set<PokemonDTO> set) {
		Collection<PokemonDTO> coll = new HashSet<PokemonDTO>();
		coll.addAll(set);
		return coll;
	}


	/**
	 * Handles incoming HTTP PUT requests for the relative URI "pokemon/{id}.
	 * a XML representation of the updated Trainer.
	 * This is a standard PUT request that updates the pokemon
	 */
	@PUT
	@Path("{id}/updatePokemon")
	@Consumes("application/xml")
	public Response updateTeam(@PathParam("id") long id, PokemonListDTO pokemonListDTO) {
		_logger.debug("Updating team " + id);
		em.getTransaction().begin();
		Team team = em.find(Team.class, id);
		Set<Pokemon> pokemonSet = new HashSet<Pokemon>();
		for (PokemonDTO pokemonDTO : pokemonListDTO.getPokemons()) {
			pokemonSet.add(PokemonMapper.toDomainModel(pokemonDTO));
		}
		team.setPokemon(pokemonSet);
		for (Pokemon p : pokemonSet) {
			p.setTeam(team);
		}
		em.persist(team);
		for (Pokemon p : pokemonSet) {
			em.merge(p);
		}

		//em.persist(pokemon);
		em.getTransaction().commit();

		_logger.debug("Updated team: " + id + "by adding " + pokemonListDTO.getPokemons().size() + " pokemon");
        return Response.created(URI.create("/team/" + id))
                .build();
	}

	/**
	 * Handles incoming HTTP PUT requests for the relative URI "pokemon/{id}/{trainerId}.
	 * Clients will call this method and pass in both a Pokemon and Trainer DTO, represent the pokemon and the trainer that caught it
	 * .
	 */
	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public Response updateTeamDetails(@PathParam("id") long id, TeamDTO teamDTO) {
		_logger.debug("Updating team details: " + id);
		em.getTransaction().begin();
		// Get the full pokemon and trainer object from the database.
		Team team = em.find(Team.class, id);

        // Update the details of the Trainer to be updated.
		team.setTeamName(teamDTO.getTeamName());
		team.setTeamGrade(teamDTO.getTeamGrade());

		em.persist(team);

		em.getTransaction().commit();
		_logger.debug("Updated team: " + team);
		return Response.created(URI.create("/team/" + id))
				.build();
	}


}
