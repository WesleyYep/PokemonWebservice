package nz.ac.auckland.pokemon.domain;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.*;

/**
 * Bean class to represent a Trainer.
 *
 * For this first Web service, a Trainer is simply represented by a unique id,
 * a name, gender and date of birth.
 *
 * @author Wesley Yep
 *
 */

@Entity
public class Trainer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String lastName;
	private String firstName;

	@Enumerated(value = EnumType.STRING)
	private Gender gender;
	@Temporal( TemporalType.DATE )
	private Date dateOfBirth;

	@OneToMany(mappedBy = "trainer")
	private List<Pokemon> pokemon = new ArrayList<Pokemon>();

	@ManyToMany
	@JoinTable(
			name = "TRAINER_CONTACTS",
			joinColumns = @JoinColumn(name = "TRAINER_ID"),
			inverseJoinColumns = @JoinColumn(name = "OTHER_ID")
	)
	private Set<Trainer> contacts = new HashSet<Trainer>();

	@Embedded
	private Record record;

	public Trainer() {

	}

	public Trainer(long id, String lastName, String firstName, Gender gender, Date dateOfBirth, Record record) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.record = record;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstname) {
		firstName = firstname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth.toDateTimeAtStartOfDay().toDate();
	}

	public List<Pokemon> getPokemon() {
		return pokemon;
	}

	public void setPokemon(List<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}

	public void addContact(Trainer t) {
		contacts.add(t);
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
	
	public void wonBattle() {
		record.setBattlesWon(record.getBattlesWon()+1);
	}
	
	public void lostBattle() {
		record.setBattlesLost(record.getBattlesLost()+1);
	}
	
	public void wonBadge() {
		record.setBadgesWon(record.getBadgesWon()+1);
	}

	public void wonCompetition() {
		record.setCompetitionWins(record.getCompetitionWins()+1);
	}
}
