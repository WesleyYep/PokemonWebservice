package nz.ac.auckland.pokemon.domain;

import nz.ac.auckland.pokemon.dto.TrainerDTO;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a pokemon battle between two trainers
 * Also contains a location where the battle is occurring
 * Battles have a start and ent time
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
    
    @Embedded
    private GeoPosition location;

    public Battle() {

    }

    public Battle(long id, Date startTime, Date endTime, Trainer firstTrainer, Trainer secondTrainer, long winnerId, GeoPosition location) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.firstTrainer = firstTrainer;
        this.secondTrainer = secondTrainer;
        this.winnerId = winnerId;
        this.location = location;
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
    
    public GeoPosition getLocation() {
    	return location;
    }
    
    public void setLocation(GeoPosition location) {
    	this.location = location;
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Battle))
            return false;
        if (obj == this)
            return true;

        Battle rhs = (Battle) obj;
        return new EqualsBuilder().
                append(firstTrainer.getFirstName(), rhs.firstTrainer.getFirstName()).
                append(secondTrainer.getFirstName(), rhs.secondTrainer.getFirstName()).
                append(startTime, rhs.startTime).
                append(endTime, rhs.endTime).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(firstTrainer.getFirstName()).
                append(secondTrainer.getFirstName()).
                append(startTime).
                append(endTime).
                toHashCode();
    }
}
