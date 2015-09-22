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

	public Trainer(long id, String lastName, String firstName, Gender gender, Date dateOfBirth) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
//		_associates = new HashSet<Parolee>();
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
}