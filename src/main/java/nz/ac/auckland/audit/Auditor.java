package nz.ac.auckland.audit;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This auditor interceptor has been extended to check that all users who want to make POST and PUT requests are authenticated (there are some exceptions for testing purposes).
 * Anyone may perform GET requests, and the USER_ID of the audit entry will be null
 * All requests will be stored in the AuditEntry table in the database
 *
 * @author Wesley Yep
 *
 */
public class Auditor implements ContainerRequestFilter {
	private static String DEFAULT_USERNAME = "UNKNOWN";

	private static Logger _logger = LoggerFactory.getLogger(Auditor.class);
	
	/**
	 * When the Auditor is registered, this method will be called automatically
	 * by the JAX-RS run-time when an incoming HTTP request is received and
	 * prior to it being forwarded for processing to a Web service resource.
	 */
	public void filter(ContainerRequestContext cxt) throws IOException {
		_logger.debug("Auditor is filtering");
		
		// Read the value of the cookieUsername named "username". If there is no such
		// cookieUsername, use DEFAULT_USERNAME.
		Map<String,Cookie> cookies = cxt.getCookies();
		String username = DEFAULT_USERNAME;
		Cookie cookieUsername = cookies.get("username");
		Cookie cookiePassword = cookies.get("password");

		//_logger.info("method type: " + cxt.getMethod());

		//user registration POST, test/init and test/clearDB, and any GET methods are allowed by anyone
		//or other POST and PUT must require a valid user cookie
		if (!cxt.getUriInfo().getPath().contains("user") && !cxt.getUriInfo().getPath().contains("test")) {
	//		_logger.info(cxt.getUriInfo().getPath());
			if (!cxt.getMethod().equals("GET") && (cookieUsername == null || cookiePassword == null)) {
				_logger.info("Sorry, you need to register to do this request.");
				cxt.abortWith(Response.status(403).type("text/plain")
						.entity("Sorry, you need to register to do this request.").build());
			}
		}

		if(cookieUsername != null) {
			username = cookieUsername.getValue();
		}

		EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
				.createEntityManager();

		// Start a transaction for persisting the audit data and getting the user from the database.
		em.getTransaction().begin();

		// Fetch the User object corresponding to username. If there is no such
		// user with that name, create a new User.
		User user = fetchUser(em, username);
		if (!cxt.getUriInfo().getPath().contains("user") && !cxt.getUriInfo().getPath().contains("test") && !cxt.getMethod().equals("GET")) {
			_logger.info(cxt.getUriInfo().getPath());
			if (user == null || cookiePassword == null || !user.getPasswordHash().equals(cookiePassword.getValue())) {
				_logger.info("Sorry, you need to register to do this request.");
				cxt.abortWith(Response.status(403).type("text/plain")
						.entity("Sorry, you need to register to do this request.").build());
			}
		}

		// Create a new AuditEntry object to store details about the Web service
		// invocation.
		AuditEntry.CrudOperation crudOp = null;
		Request request = cxt.getRequest();
		String httpMethod = request.getMethod();
		if(httpMethod.equalsIgnoreCase("GET")) {
			crudOp = AuditEntry.CrudOperation.Retrieve;
		} else if(httpMethod.equalsIgnoreCase("POST")) {
			crudOp = AuditEntry.CrudOperation.Create;
		} else if(httpMethod.equalsIgnoreCase("PUT")) {
			crudOp = AuditEntry.CrudOperation.Update;
		} else if(httpMethod.equalsIgnoreCase("DELETE")) {
			crudOp = AuditEntry.CrudOperation.Delete;
		}
		String uri = cxt.getUriInfo().getRequestUri().toString();
		AuditEntry auditable = new AuditEntry(crudOp, uri, user);

		// Persist the AuditEntry in the database.
		em.persist(auditable);
		em.getTransaction().commit();
	}
	
	protected User fetchUser(EntityManager em, String username) {
		User user = null;
		try {
			_logger.debug("Querying the database for the current user");
			TypedQuery<User> query = em.createQuery(
				"select u from User u where u._username = :username", User.class
				).setParameter("username", username);
			user = query.getSingleResult();
		} catch(NoResultException e) {
			// User doesn't exist in the database, so return null, which will return a 403 Not authenticated
			return null;
		}
		return user;
	}
}
