package nz.ac.auckland.pokemon.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Bean class to represent a Pokemon trainer's team.
 *
 * It keeps track the trainers team name, grade, the trainer which it belongs to, and the pokemon in that team
 * A trainer should only have one team at a time
 *
 * @author Wesley Yep
 *
 */

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String teamName;

	private String teamGrade;

	@OneToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(unique = true)
	private Trainer trainer;

	@OneToMany(mappedBy = "team")
	private Set<Pokemon> pokemon = new HashSet<Pokemon>();

	protected Team() {	}

	public Team(String teamName, String teamGrade, Trainer trainer, Set<Pokemon> pokemon) {
		this(0, teamName, teamGrade, trainer, pokemon);
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
