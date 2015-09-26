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
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by Wesley on 26/09/2015.
 */
@Path("/user")
public class UserResource {

    // Setup a Logger.
    private static Logger _logger = LoggerFactory.getLogger(UserResource.class);
    private static EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
            .createEntityManager();

    /**
     * For now, create a battle by passing in a single BattleDBO
     * This may change later to pass in a single Trainer object to register for a battle
     */
    @POST
    @Consumes("application/xml")
    public Response register(User user) {
        _logger.info("Read user: " + user.getUsername());
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        return Response.created(URI.create("/user/" + user.getId()))
                .build();
    }

//    @POST
//    @Path("clearAllDB")
//    public void clearDatabase() {
//        _logger.info("Clearing database");
//        em.getTransaction().begin();
//        em.createNativeQuery("drop table AUDITENTRY").executeUpdate();
//        em.createNativeQuery("drop table BATTLE").executeUpdate();
//        em.createNativeQuery("drop table MOVES").executeUpdate();
//        em.createNativeQuery("drop table POKEMON").executeUpdate();
//        em.createNativeQuery("drop table POKEMON_TRAINER").executeUpdate();
//        em.createNativeQuery("drop table TRAINER").executeUpdate();
//        em.createNativeQuery("drop table TRAINER_CONTACTS").executeUpdate();
//        em.createNativeQuery("drop table USER").executeUpdate();
//        em.getTransaction().commit();
//    }

}
