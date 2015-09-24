package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.domain.Trainer;
import nz.ac.auckland.pokemon.dto.PokemonDTO;
import nz.ac.auckland.pokemon.dto.TrainerDTO;
import org.joda.time.LocalDate;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Parolees.
 * 
 * @author Ian Warren
 *
 */
public class PokemonMapper {

	public static Pokemon toDomainModel(PokemonDTO pokemonDTO) {
		Pokemon pokemon = new Pokemon(pokemonDTO.getId(),
				pokemonDTO.getName(),
				pokemonDTO.getNickname(),
				pokemonDTO.getGender(),
				pokemonDTO.getLevel(),
                pokemonDTO.getMoves());
		return pokemon;
	}
	
	public static PokemonDTO toDto(Pokemon pokemon) {
		PokemonDTO pokemonDTO =
				new PokemonDTO(
						pokemon.getId(),
						pokemon.getName(),
						pokemon.getNickname(),
						pokemon.getGender(),
						pokemon.getLevel(),
						pokemon.getMoves());
		return pokemonDTO;
		
	}
}
