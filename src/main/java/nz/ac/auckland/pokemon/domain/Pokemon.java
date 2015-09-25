package nz.ac.auckland.pokemon.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Bean class to represent a Pokemon.
 *
 * For this first Web service, a Pokemon is simply represented by a unique id,
 * a name, nickname, gender and level.
 *
 * @author Wesley Yep
 *
 */

@Entity
public class Pokemon {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private String nickname;
	private int level;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name="POKEMON_TRAINER",
		joinColumns =
		@JoinColumn(name = "POKEMON_ID"),
		inverseJoinColumns =
		@JoinColumn(nullable = false)
	)
	protected Trainer trainer;

	@Enumerated(value = EnumType.STRING)
	private Gender gender;

	@ElementCollection
	@CollectionTable(name = "MOVES")
    protected Set<Move> moves = new HashSet<Move>();

	public Pokemon(long id, String lastName, String firstName, Gender gender, int level, Set<Move> moves) {
		this.id = id;
		this.name = lastName;
		this.nickname = firstName;
		this.gender = gender;
		this.level = level;
        this.moves = moves;
//		_associates = new HashSet<Parolee>();
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String firstname) {
		nickname = firstname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

    public void learnMove(Move move) {
        moves.add(move);
    }

    public void forgetMove(Move move) {
        moves.remove(move);
    }

    public Set<Move> getMoves() {
        return moves;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pokemon))
			return false;
		if (obj == this)
			return true;

		Pokemon rhs = (Pokemon) obj;
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
