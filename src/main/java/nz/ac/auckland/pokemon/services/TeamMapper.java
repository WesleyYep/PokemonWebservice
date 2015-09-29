package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Team;
import nz.ac.auckland.pokemon.domain.TeamDTO;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import org.joda.time.LocalDate;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Parolees.
 * 
 * @author Ian Warren
 *
 */
public class TeamMapper {

	public static Team toDomainModel(TeamDTO teamDTO) {
		Team team = new Team(teamDTO.getId(),
				teamDTO.getTeamName(),
				teamDTO.getTeamGrade(),
				teamDTO.getTrainer(),
				teamDTO.getPokemon());
		return team;
	}
	
	public static TeamDTO toDto(Team team) {
		TeamDTO teamDTO =
				new TeamDTO(
						team.getId(),
						team.getTeamName(),
						team.getTeamGrade(),
						team.getTrainer(),
						team.getPokemon());
		return teamDTO;
		
	}
}
