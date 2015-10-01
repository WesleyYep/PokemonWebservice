package nz.ac.auckland.audit;

import java.util.Date;
import javax.persistence.*;


/**
 * From Part 2 of the assignment
 * Represents the audit entry in a database table
 */
@Entity
public class AuditEntry {
	
	public enum CrudOperation {Create, Retrieve, Update, Delete}
	
	@Id
	@GeneratedValue(  generator = "ID_GENERATOR" )
	private Long _id;

	@Enumerated(value = EnumType.STRING)
	private CrudOperation _crudOperator;
	
	private String _uri;
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private User _user;
	
	private Date _timestamp;
	
	public AuditEntry(CrudOperation crudOp, String uri, User user) {
		_crudOperator = crudOp;
		_uri = uri;
		_user = user;
		_timestamp = new Date();
	}
	
	protected AuditEntry() {}
	
	public Long getId() {
		return _id;
	}
	
	public CrudOperation getCrudOperation() {
		return _crudOperator;
	}
	
	public String getUri() {
		return _uri;
	}
	
	public User getUser() {
		return _user;
	}
	
	public Date getTimestamp() {
		return _timestamp;
	}
}
