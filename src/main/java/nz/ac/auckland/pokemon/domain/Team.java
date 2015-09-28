package nz.ac.auckland.pokemon.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Bean class to represent a Pokemon trainer's record.
 *
 * It keeps track the trainers win/loss record, badges, and competition wins
 *
 * @author Wesley Yep
 *
 */

@Entity
@XmlRootElement(name="team")
@XmlAccessorType(XmlAccessType.FIELD)
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@XmlElement(name="teamName")
	private String teamName;

	@XmlElement(name="teamGrade")
	private String teamGrade;

	@XmlElement(name="trainer")
	@OneToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(unique = true)
	private Trainer trainer;

	@XmlElement(name="pokemonList")
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "TEAM_ID", nullable = true)
	private Set<Pokemon> pokemon = new HashSet<Pokemon>();

	protected Team() {	}

	public Team(String teamName, String teamGrade, Trainer trainer, Set<Pokemon> pokemon) {
		this.teamName = teamName;
		this.teamGrade = teamGrade;
		this.pokemon = pokemon;
		this.trainer = trainer;
	}

	public Team(long id, String teamName, String teamGrade, Trainer trainer, Set<Pokemon> pokemon) {
		this.id = id;
		this.teamName = teamName;
		this.teamGrade = teamGrade;
		this.pokemon = pokemon;
		this.trainer = trainer;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public String getTeamGrade() {
		return teamGrade;
	}

	public void setTeamGrade(String teamGrade) {
		this.teamGrade = teamGrade;
	}

	public Set<Pokemon> getPokemon() {
		return pokemon;
	}

	public void setPokemon(Set<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pokemon))
			return false;
		if (obj == this)
			return true;

		Team rhs = (Team) obj;
		return new EqualsBuilder().
				append(teamName, rhs.teamName).
				append(teamGrade, rhs.teamGrade).
				append(trainer, rhs.trainer).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).
				append(teamName).
				append(teamGrade).
				append(trainer).
				toHashCode();
	}
}
