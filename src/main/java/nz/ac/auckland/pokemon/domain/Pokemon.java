package nz.ac.auckland.pokemon.domain;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Bean class to represent a Trainer.
 *
 * For this first Web service, a Trainer is simply represented by a unique id,
 * a name, gender and date of birth.
 *
 * @author Ian Warren
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

	public Pokemon(long id, String lastName, String firstName, Gender gender, int level) {
		this.id = id;
		this.name = lastName;
		this.nickname = firstName;
		this.gender = gender;
		this.level = level;
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

}
