package nz.ac.auckland.pokemon.dto;

import nz.ac.auckland.pokemon.domain.Gender;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;

import javax.persistence.Enumerated;
import javax.xml.bind.annotation.*;

/* Class to represent a Trainer.
 *
 * An instance of this class represents a DTO Trainer
 *
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

	protected PokemonDTO() {

	}

	/**
	 * Constructs a DTO Trainer instance. This method is intended to be called
	 * by Web service clients when creating new Trainers. The id field is not
	 * required because it is generated by the Web service. Similarly, the
	 * last-known-position field is not required. Of the constructor's
	 * parameters, all fields must be non-null with the exception of curfew,
	 * which is optional (not all Trainers are subject to a curfew).
     *
	 */
	public PokemonDTO(String lastName, String firstName, Gender gender, int level) throws IllegalArgumentException {
		this(0, lastName, firstName, gender, level);
	}

	/**
	 * Constructs a DTO Parolee instance. This method should NOT be called by
	 * Web Service clients. It is intended to be used by the Web Service
	 * implementation when creating a DTO Parolee from a domain-model Parolee
	 * object.
	 */
	public PokemonDTO(long id, String lastName, String firstName, Gender gender, int level) {
		this.id = id;
		this.name = lastName;
		this.nickname = firstName;
		this.gender = gender;
		this.level = level;
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
            append(id, rhs.id).
            append(name, rhs.name).
            append(nickname, rhs.nickname).
            append(gender, rhs.gender).
            append(level, rhs.level).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(id).
	            append(name).
	            append(nickname).
	            append(gender).
                append(level).
	            toHashCode();
	}
}


