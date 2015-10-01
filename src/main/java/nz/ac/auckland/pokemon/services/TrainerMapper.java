package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Record;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import org.joda.time.LocalDate;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Trainers.
 * 
 * @author Wesley Yep
 *
 */
public class TrainerMapper {

	public static Trainer toDomainModel(TrainerDTO dtoTrainer) {
		Trainer trainer = new Trainer(dtoTrainer.getId(),
				dtoTrainer.getLastName(),
				dtoTrainer.getFirstName(),
				dtoTrainer.getGender(),
				dtoTrainer.getDateOfBirth().toDateTimeAtStartOfDay().toDate(),
				dtoTrainer.getRecord());
		return trainer;
	}
	
	public static TrainerDTO toDto(Trainer trainer) {
		TrainerDTO trainerDTO =
				new TrainerDTO(
						trainer.getId(),
						trainer.getLastName(),
						trainer.getFirstName(),
						trainer.getGender(),
						new LocalDate(trainer.getDateOfBirth()),
						trainer.getRecord());
		return trainerDTO;
		
	}
}
