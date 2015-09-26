package nz.ac.auckland.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.*;

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
	
	public User(String username, String lastname, String firstname) {
		_username = username;
		_lastname = lastname;
		_firstname = firstname;
	}
	
	public User(String username) {
		this(username, null, null);
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
}
