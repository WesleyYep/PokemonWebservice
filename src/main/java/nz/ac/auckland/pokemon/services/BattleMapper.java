package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Battle;
import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.dto.BattleDTO;
import nz.ac.auckland.pokemon.dto.PokemonDTO;
import org.joda.time.DateTime;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Parolees.
 * 
 * @author Ian Warren
 *
 */
public class BattleMapper {

	public static Battle toDomainModel(BattleDTO battleDTO) {
		Battle battle = new Battle(battleDTO.getId(),
				battleDTO.getStartTime().toDate(),
				battleDTO.getEndTime().toDate(),
				TrainerMapper.toDomainModel(battleDTO.getFirstTrainer()),
				TrainerMapper.toDomainModel(battleDTO.getSecondTrainer()),
                battleDTO.getWinnerId(),
                battleDTO.getLocation());
		return battle;
	}
	
	public static BattleDTO toDto(Battle battle) {
		BattleDTO battleDTO =
				new BattleDTO(
						battle.getId(),
						new DateTime(battle.getStartTime()),
						new DateTime(battle.getEndTime()),
						TrainerMapper.toDto(battle.getFirstTrainer()),
						TrainerMapper.toDto(battle.getSecondTrainer()),
						battle.getWinnerId(),
						battle.getLocation());
		return battleDTO;
		
	}
}
