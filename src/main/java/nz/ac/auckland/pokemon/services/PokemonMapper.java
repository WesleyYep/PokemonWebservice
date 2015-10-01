package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.dto.PokemonDTO;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Pokemon.
 * 
 * @author Wesley Yep
 *
 */
public class PokemonMapper {

	public static Pokemon toDomainModel(PokemonDTO pokemonDTO) {
		Pokemon pokemon = new Pokemon(pokemonDTO.getId(),
				pokemonDTO.getName(),
				pokemonDTO.getNickname(),
				pokemonDTO.getGender(),
				pokemonDTO.getLevel(),
                pokemonDTO.getMoves(),
				pokemonDTO.getTeam());
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
						pokemon.getMoves(),
						pokemon.getTeam());
		return pokemonDTO;
		
	}
}
