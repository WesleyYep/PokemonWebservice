package nz.ac.auckland.pokemon.dto;

import nz.ac.auckland.pokemon.domain.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Enumerated;
import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

/** Class to represent a Pokemon Data Transmission Object.
 *
 * An instance of this class represents a DTO Pokemon, for being translated into XML and being passed between client/server
 *
 * Created by Wesley on 26/09/2015.
 */

@XmlRootElement(name="pokemon")
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonDTO {

	@XmlAttribute(name="id")
	private long id;

	@XmlElement(name="name")
	private String name;

	@XmlElement(name="nickname")
	private String nickname;

	@XmlElement(name="gender")
	@Enumerated
	private Gender gender;

	@XmlElement(name="level")
	private int level;

    @XmlElementWrapper(name = "moves")
	@XmlElement(name = "move")
	private Set<Move> moves;

	@XmlTransient
	private Team team;

	protected PokemonDTO() {

	}

	public PokemonDTO(String name, String nickname, Gender gender, int level, Set<Move> moves, Team team) throws IllegalArgumentException {
		this(0, name, nickname, gender, level, moves, team);
	}

	public PokemonDTO(long id, String name, String nickname, Gender gender, int level, Set<Move> moves, Team team) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.gender = gender;
		this.level = level;
        this.moves = moves;
		this.team = team;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		nickname = nickname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		gender = gender;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

    public Set<Move> getMoves() {
        return moves;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}


	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Pokemon: { [");
		buffer.append(id);
		buffer.append("]; ");
		if(name != null) {
			buffer.append(name);
			buffer.append(", ");
		}
		if(nickname != null) {
			buffer.append(nickname);
		}
		buffer.append("; ");
		if(gender != null) {
			buffer.append(gender);
		}
		buffer.append("; ");

		buffer.append("\n  ");
		buffer.append(level);

		buffer.append(" }");
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PokemonDTO))
            return false;
        if (obj == this)
            return true;

        PokemonDTO rhs = (PokemonDTO) obj;
        return new EqualsBuilder().
            append(name, rhs.name).
            append(nickname, rhs.nickname).
            append(gender, rhs.gender).
            append(level, rhs.level).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(name).
	            append(nickname).
	            append(gender).
                append(level).
	            toHashCode();
	}

}


