package nz.ac.auckland.pokemon.services;

import nz.ac.auckland.pokemon.domain.*;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.HashSet;

/**
 * This contains the webservice methods that are needed to reset and initialise the database
 *
 * Created by Wesley on 26/09/2015.
 *
 */
@Path("/test")
public class TestResource {

    // Setup a Logger.
    private static Logger _logger = LoggerFactory.getLogger(TestResource.class);
    private static EntityManager em = Persistence.createEntityManagerFactory("pokemonPU")
            .createEntityManager();

    /**
     * Clears the database before the tests are run
     */
    @POST
    @Path("clearAllDB")
    public void clearDatabase() {
        _logger.info("Clearing database");
        em.getTransaction().begin();
        em.createNativeQuery("drop table AUDITENTRY").executeUpdate();
        em.createNativeQuery("drop table BATTLE").executeUpdate();
        em.createNativeQuery("drop table MOVES").executeUpdate();
        em.createNativeQuery("drop table POKEMON").executeUpdate();
        em.createNativeQuery("drop table POKEMON_TEAM").executeUpdate();
        em.createNativeQuery("drop table POKEMON_TRAINER").executeUpdate();
        em.createNativeQuery("drop table TEAM").executeUpdate();
        em.createNativeQuery("drop table TRAINER").executeUpdate();
        em.createNativeQuery("drop table TRAINER_CONTACTS").executeUpdate();
        em.createNativeQuery("drop table USER").executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * Sets up the database with some data
     */
    @POST
    @Path("init")
    public void initialiseDatabase() {
        _logger.info("Initialising database");
        em.getTransaction().begin();

        Record r1 = new Record(100,21, 2, 1);
        Record r2 = new Record(15,230, 1, 0);
        Record r3 = new Record(543,212, 8, 3);
        Record r4 = new Record(43,44, 3, 10);
        Record r5 = new Record(259,0, 9, 0);

        Trainer james = new Trainer(0, "smith", "james", Gender.MALE, new LocalDate(1992, 12, 6).toDateTimeAtStartOfDay().toDate(), r1);
        Trainer john = new Trainer(0, "jones", "john", Gender.MALE, new LocalDate(1991, 7, 7).toDateTimeAtStartOfDay().toDate(), r2);
        Trainer jesse = new Trainer(0, "parker", "jesse", Gender.FEMALE, new LocalDate(1990, 3, 12).toDateTimeAtStartOfDay().toDate(), r3);
        Trainer tom = new Trainer(0, "riddle", "tom", Gender.MALE, new LocalDate(1995, 4, 24).toDateTimeAtStartOfDay().toDate(), r4);
        Trainer harry = new Trainer(0, "potter", "harry", Gender.MALE, new LocalDate(1997, 2, 16).toDateTimeAtStartOfDay().toDate(), r5);

        james.addContact(jesse);
        jesse.addContact(james);
        tom.addContact(harry);
        tom.addContact(john);
        harry.addContact(james);
        harry.addContact(jesse);

        em.persist(james);
        em.persist(john);
        em.persist(jesse);
        em.persist(tom);
        em.persist(harry);

        Move thunderbolt = new Move("thunderbolt", 90, 100, Type.ELECTRIC);
        Move bugbuzz = new Move("bug buzz", 80, 100, Type.BUG);
        Move nightslash = new Move("night slash", 70, 100, Type.DARK);
        Move outrage = new Move("outrage", 120, 100, Type.DRAGON);
        Move earthquake = new Move("earthquake", 100, 100, Type.DRAGON);
        Move destinybond = new Move("destiny bond", 0, 100, Type.GHOST);
        Move psychic = new Move("psychic", 90, 100, Type.PSYCHIC);
        Move dynamicpunch = new Move("dynamic punch", 100, 50, Type.FIGHTING);

        Pokemon wobbuffet = new Pokemon(0, "wobbuffet", "wobbly", Gender.MALE, 30, new HashSet<Move>(Arrays.asList(psychic, destinybond)));
        Pokemon garchomp = new Pokemon(0, "garchomp", "power dragon", Gender.MALE, 100, new HashSet<Move>(Arrays.asList(earthquake, outrage)));
        Pokemon gengar = new Pokemon(0, "gengar", "ghostly", Gender.FEMALE, 60, new HashSet<Move>(Arrays.asList(nightslash, destinybond, thunderbolt)));
        Pokemon machamp = new Pokemon(0, "machamp", "champion", Gender.MALE, 29, new HashSet<Move>(Arrays.asList(dynamicpunch)));
        Pokemon raichu = new Pokemon(0, "raichu", "electric rat", Gender.FEMALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon charmander = new Pokemon(0, "charmander", "fire lizard", Gender.FEMALE, 67, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon charmeleon = new Pokemon(0, "charmeleon", "charmeleon", Gender.FEMALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon charizard = new Pokemon(0, "charizard", "bigfirelizard", Gender.MALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon bulbasaur = new Pokemon(0, "bulbasaur", "bulbasaur", Gender.FEMALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon ivysaur = new Pokemon(0, "ivysaur", "ivysaur", Gender.MALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon venusaur = new Pokemon(0, "venusaur", "venusaur", Gender.FEMALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon squirte = new Pokemon(0, "squirte", "squirte", Gender.MALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon wartortle = new Pokemon(0, "wartortle", "wartortle", Gender.MALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));
        Pokemon blastoise = new Pokemon(0, "blastoise", "blastoise", Gender.FEMALE, 99, new HashSet<Move>(Arrays.asList(thunderbolt, bugbuzz)));

        wobbuffet.setTrainer(james);
        garchomp.setTrainer(harry);
        gengar.setTrainer(tom);
        machamp.setTrainer(tom);
        raichu.setTrainer(jesse);
        charmander.setTrainer(james);
        charmeleon.setTrainer(james);
        charizard.setTrainer(james);
        bulbasaur.setTrainer(james);
        ivysaur.setTrainer(james);
        venusaur.setTrainer(james);
        squirte.setTrainer(james);
        wartortle.setTrainer(james);
        blastoise.setTrainer(james);

        Team jamesTeam = new Team(0, "epicTeam", "C", james, new HashSet<Pokemon>(Arrays.asList(charmander, ivysaur, bulbasaur)));
        Team tomsTeam = new Team(0, "coolTeam", "B", tom, new HashSet<Pokemon>(Arrays.asList(gengar, machamp)));
        charmander.setTeam(jamesTeam);
        ivysaur.setTeam(jamesTeam);
        bulbasaur.setTeam(jamesTeam);
        gengar.setTeam(tomsTeam);
        machamp.setTeam(tomsTeam);

        em.persist(jamesTeam);
        em.persist(tomsTeam);

        em.persist(wobbuffet);
        em.persist(garchomp);
        em.persist(gengar);
        em.persist(new Pokemon(0, "accelgor", "accelerator", Gender.FEMALE, 3, new HashSet<Move>(Arrays.asList(bugbuzz))));
        em.persist(machamp);
        em.persist(raichu);
        em.persist(new Pokemon(0, "dragonite", "bigflyingbird", Gender.FEMALE, 3, new HashSet<Move>(Arrays.asList(outrage, earthquake, thunderbolt))));
        em.persist(charmander);
        em.persist(charmeleon);
        em.persist(charizard);
        em.persist(bulbasaur);
        em.persist(ivysaur);
        em.persist(venusaur);
        em.persist(squirte);
        em.persist(wartortle);
        em.persist(blastoise);

        em.persist(new Battle(0, new LocalDate(1992, 12, 6).toDate(), new LocalDate(1992, 12, 7).toDate(),
                jesse, harry, harry.getId(), new GeoPosition(20,30)));
        em.persist(new Battle(0, new LocalDate(2015, 8, 9).toDate(), new LocalDate(2015, 8, 10).toDate(),
                james, tom, james.getId(), new GeoPosition(45,45)));
        em.persist(new Battle(0, new LocalDate(2012, 12, 21).toDate(), new LocalDate(2012, 12, 22).toDate(),
                john, james, james.getId(), new GeoPosition(45,45)));

        em.getTransaction().commit();
    }

}
