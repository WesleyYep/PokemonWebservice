package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.audit.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * This Resource class contains the webservice method to create a new user in the database
 *
 * Created by Wesley on 26/09/2015.
 */
@Path("/user")
public class UserResource {

    // Setup a Logger.
    private static Logger _logger = LoggerFactory.getLogger(UserResource.class);
    private static EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
            .createEntityManager();

    /**
     * Register a user in the database
     * This method is not subject to the authorization that is needed for the other POST and PUT requests
     */
    @POST
    @Consumes("application/xml")
    public Response register(User user) {
        _logger.info("Read user: " + user.getUsername());
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        NewCookie cookieUsername = new NewCookie("username", "wesleyyep");
        NewCookie cookiePassword = new NewCookie("password", PasswordHasher.passwordHash("password"));

        return Response.created(URI.create("/user/" + user.getId())).cookie(cookieUsername).cookie(cookiePassword)
                .build();
    }



}
