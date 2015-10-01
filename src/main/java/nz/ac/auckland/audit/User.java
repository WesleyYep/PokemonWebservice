package nz.ac.auckland.audit;

import nz.ac.auckland.pokemon.services.PasswordHasher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.*;

/**
 * Based on the User class of part 2 of the assignment
 * Modified to contain a password hash for security reasons and to authenticate the user
 */
@Entity
@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	@XmlAttribute(name="id")
	@Id
	@GeneratedValue( generator = "ID_GENERATOR")
	private Long _id;
	
	@Column(unique=true)
	@XmlElement
	private String _username;

	@XmlElement
	private String _lastname;

	@XmlElement
	private String _firstname;

	@XmlElement
	@Column(nullable = false)
	private String passwordHash;
	
	public User(String username, String lastname, String firstname, String passwordHash) {
		_username = username;
		_lastname = lastname;
		_firstname = firstname;
		this.passwordHash = passwordHash;
	}
	
	public User(String username) {
		this(username, null, null, PasswordHasher.passwordHash("default"));
	}
	
	public User() {}
	
	public Long getId() {
		return _id;
	}
	
	public String getUsername() {
		return _username;
	}
	
	public String getLastname() {
		return _lastname;
	}
	
	public String getFirstname() {
		return _firstname;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}
