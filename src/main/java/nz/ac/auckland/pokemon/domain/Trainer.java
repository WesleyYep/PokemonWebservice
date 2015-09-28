package nz.ac.auckland.pokemon.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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


	public Set<Trainer> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Trainer> contacts) {
		this.contacts = contacts;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Trainer))
			return false;
		if (obj == this)
			return true;

		Trainer rhs = (Trainer) obj;
		return new EqualsBuilder().
				append(lastName, rhs.lastName).
				append(firstName, rhs.firstName).
				append(gender, rhs.gender).
				append(dateOfBirth, rhs.dateOfBirth).
				isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).
				append(lastName).
				append(firstName).
				append(gender).
				append(dateOfBirth).
				toHashCode();
	}
	
}
