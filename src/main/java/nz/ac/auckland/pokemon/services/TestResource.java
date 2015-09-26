package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.audit.User;
import nz.ac.auckland.pokemon.domain.*;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Wesley on 26/09/2015.
 */
@Path("/test")
public class TestResource {

    // Setup a Logger.
    private static Logger _logger = LoggerFactory.getLogger(TestResource.class);
    private static EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
            .createEntityManager();

    @POST
    @Path("clearAllDB")
    public void clearDatabase() {
        _logger.info("Clearing database");
        em.getTransaction().begin();
        em.createNativeQuery("drop table AUDITENTRY").executeUpdate();
        em.createNativeQuery("drop table BATTLE").executeUpdate();
        em.createNativeQuery("drop table MOVES").executeUpdate();
        em.createNativeQuery("drop table POKEMON").executeUpdate();
        em.createNativeQuery("drop table POKEMON_TRAINER").executeUpdate();
        em.createNativeQuery("drop table TRAINER").executeUpdate();
        em.createNativeQuery("drop table TRAINER_CONTACTS").executeUpdate();
        em.createNativeQuery("drop table USER").executeUpdate();
        em.getTransaction().commit();
    }
    @POST
    @Path("init")
    public void initialiseDatabase() {
        _logger.info("Initialising database");
        em.getTransaction().begin();

        em.persist(new Trainer(0, "smith", "james", Gender.MALE, new LocalDate(1992, 12, 6).toDateTimeAtStartOfDay().toDate(), new Record()));
        em.persist(new Trainer(0, "jones", "john", Gender.MALE, new LocalDate(1991, 7, 7).toDateTimeAtStartOfDay().toDate(), new Record()));
        em.persist(new Trainer(0, "smith", "jesse", Gender.FEMALE, new LocalDate(1990, 3, 12).toDateTimeAtStartOfDay().toDate(), new Record()));
        em.persist(new Trainer(0, "parker", "tom", Gender.MALE, new LocalDate(1995, 4, 24).toDateTimeAtStartOfDay().toDate(), new Record()));
        em.persist(new Trainer(0, "potter", "harry", Gender.MALE, new LocalDate(1997, 2, 16).toDateTimeAtStartOfDay().toDate(), new Record()));

        Move thunderbolt = new Move("thunderbolt", 90, 100, Type.ELECTRIC);
        Move bugbuzz = new Move("bug buzz", 80, 100, Type.BUG);
        Move nightslash = new Move("night slash", 70, 100, Type.DARK);
        Move outrage = new Move("outrage", 120, 100, Type.DRAGON);
        Move earthquake = new Move("earthquake", 100, 100, Type.DRAGON);
        Move destinybond = new Move("destiny bond", 0, 100, Type.GHOST);
        Move psychic = new Move("psychic", 90, 100, Type.PSYCHIC);
        Move dynamicpunch = new Move("dynamic punch", 100, 50, Type.FIGHTING);

        em.persist(new Pokemon(0, "wobbuffet", "wobbly", Gender.MALE, 3, new HashSet<Move>(Arrays.asList(psychic, destinybond))));
        em.persist(new Pokemon(0, "garchomp", "power dragon", Gender.MALE, 3, new HashSet<Move>(Arrays.asList(earthquake, outrage))));
        em.persist(new Pokemon(0, "gengar", "ghostly", Gender.FEMALE, 3, new HashSet<Move>(Arrays.asList(nightslash, destinybond, thunderbolt))));
        em.persist(new Pokemon(0, "accelgor", "accelerator", Gender.FEMALE, 3, new HashSet<Move>(Arrays.asList(bugbuzz))));
        em.persist(new Pokemon(0, "machamp", "champion", Gender.MALE, 3, new HashSet<Move>(Arrays.asList(dynamicpunch))));
        em.persist(new Pokemon(0, "raichu", "electric rat", Gender.FEMALE, 3, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz))));
        em.persist(new Pokemon(0, "dragonite", "bigflyingbird", Gender.FEMALE, 3, new HashSet<Move>(Arrays.asList(outrage, earthquake, thunderbolt))));


        em.getTransaction().commit();
    }

}
