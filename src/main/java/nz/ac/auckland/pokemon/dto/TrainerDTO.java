package nz.ac.auckland.pokemon.dto;

import nz.ac.auckland.pokemon.domain.Gender;
import nz.ac.auckland.pokemon.domain.Record;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;

import nz.ac.auckland.pokemon.jaxb.LocalDateAdapter;

import javax.persistence.Enumerated;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/* Class to represent a Trainer.
 *
 * An instance of this class represents a DTO Trainer
 *
 */

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

	/**
	 * Constructs a DTO Trainer instance. This method is intended to be called
	 * by Web service clients when creating new Trainers. The id field is not
	 * required because it is generated by the Web service. Similarly, the
	 * last-known-position field is not required. Of the constructor's
	 * parameters, all fields must be non-null with the exception of curfew,
	 * which is optional (not all Trainers are subject to a curfew).
     *
	 */
	public TrainerDTO(String lastName, String firstName, Gender gender, LocalDate dateOfBirth, Record record) throws IllegalArgumentException {
		this(0, lastName, firstName, gender, dateOfBirth, record);
	}

	/**
	 * Constructs a DTO Parolee instance. This method should NOT be called by
	 * Web Service clients. It is intended to be used by the Web Service
	 * implementation when creating a DTO Parolee from a domain-model Parolee
	 * object.
	 */
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


