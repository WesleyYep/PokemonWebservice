package nz.ac.auckland.pokemon.domain;

import nz.ac.auckland.pokemon.dto.TrainerDTO;

import org.joda.time.DateTime;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a pokemon battle between two trainers
 *
 * Created by Wesley on 24/09/2015.
 */
@Entity
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal( TemporalType.TIMESTAMP )
    private Date startTime;

    @Temporal( TemporalType.TIMESTAMP )
    private Date endTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "FIRST_TRAINER_ID", nullable = false)
    protected Trainer firstTrainer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "SECOND_TRAINER_ID", nullable = false)
    protected Trainer secondTrainer;

    private long winnerId;

    public Battle() {

    }

    public Battle(long id, Date startTime, Date endTime, Trainer firstTrainer, Trainer secondTrainer, long winnerId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.firstTrainer = firstTrainer;
        this.secondTrainer = secondTrainer;
        this.winnerId = winnerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Trainer getFirstTrainer() {
        return firstTrainer;
    }

    public void setFirstTrainer(Trainer firstTrainer) {
        this.firstTrainer = firstTrainer;
    }

    public Trainer getSecondTrainer() {
        return secondTrainer;
    }

    public void setSecondTrainer(Trainer secondTrainer) {
        this.secondTrainer = secondTrainer;
    }


    public long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(long winnerId) {
        this.winnerId = winnerId;
    }

}
