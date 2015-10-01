package nz.ac.auckland.pokemon.dto;

import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.domain.Record;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.LocalDate;

import nz.ac.auckland.pokemon.jaxb.LocalDateAdapter;

import javax.persistence.Enumerated;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** Class to represent a Trainer Data Transmission Object.
 *
 * An instance of this class represents a DTO Trainer, for being translated into XML and being passed between client/server
 *
 * Created by Wesley on 26/09/2015.
 */
@JsonIgnoreProperties
@XmlRootElement(name="trainer")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainerDTO {

	@XmlAttribute(name="id")
	private long id;

	@XmlElement(name="last-name")
	private String lastName;

	@XmlElement(name="first-name")
	private String firstName;

	@XmlElement(name="gender")
	@Enumerated
	private Gender gender;

	@XmlElement(name="date-of-birth")
	@XmlJavaTypeAdapter(value=LocalDateAdapter.class)
	private LocalDate dateOfBirth;
	
	@XmlElement(name="record")
	private Record record;

	protected TrainerDTO() {

	}
	
	public TrainerDTO(String lastName, String firstName, Gender gender, LocalDate dateOfBirth, Record record) throws IllegalArgumentException {
		this(0, lastName, firstName, gender, dateOfBirth, record);
	}

	public TrainerDTO(long id, String lastName, String firstName, Gender gender, LocalDate dateOfBirth, Record record) {
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

	public void setId(long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		firstName = firstName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate date) {
		this.dateOfBirth = date;
	}
	
	public Record getRecord() {
		return record;
	}
	
	public void setRecord(Record record) {
		this.record = record;
	}
	

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Trainer: { [");
		buffer.append(id);
		buffer.append("]; ");
		if(lastName != null) {
			buffer.append(lastName);
			buffer.append(", ");
		}
		if(firstName != null) {
			buffer.append(firstName);
		}
		buffer.append("; ");
		if(gender != null) {
			buffer.append(gender);
		}
		buffer.append("; ");

		buffer.append("\n  ");
		if(dateOfBirth != null) {
			buffer.append(dateOfBirth);
		}

		buffer.append(" }");
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TrainerDTO))
			return false;
		if (obj == this)
			return true;

		TrainerDTO rhs = (TrainerDTO) obj;
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


