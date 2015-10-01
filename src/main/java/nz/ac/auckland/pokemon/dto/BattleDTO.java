package nz.ac.auckland.pokemon.dto;

import nz.ac.auckland.pokemon.domain.GeoPosition;
import nz.ac.auckland.pokemon.jaxb.DateTimeAdapter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** Class to represent a Battle Data Transmission Object.
 *
 * An instance of this class represents a DTO Battle, for being translated into XML and being passed between client/server
 *
 * Created by Wesley on 26/09/2015.
 */

@XmlRootElement(name="pokemon")
@XmlAccessorType(XmlAccessType.FIELD)
public class BattleDTO {

	@XmlAttribute(name="id")
	private long id;

	@XmlElement(name="startTime")
	@XmlJavaTypeAdapter(value=DateTimeAdapter.class)
	private DateTime startTime;

	@XmlElement(name="endTime")
	@XmlJavaTypeAdapter(value=DateTimeAdapter.class)
	private DateTime endTime;

	@XmlElement(name="firstTrainer")
	private TrainerDTO firstTrainer;

	@XmlElement(name="secondTrainer")
	private TrainerDTO secondTrainer;

	@XmlElement(name="winnerId")
	private long winnerId;
	
	@XmlElement(name="location")
	private GeoPosition location;

	protected BattleDTO() {

	}

	public BattleDTO(DateTime startTime, DateTime endTime, TrainerDTO firstTrainer, TrainerDTO secondTrainer,
			long winnerId, GeoPosition location) throws IllegalArgumentException {
		this(0, startTime, endTime, firstTrainer, secondTrainer, winnerId, location);
	}

	public BattleDTO(long id, DateTime startTime, DateTime endTime, TrainerDTO firstTrainer, TrainerDTO secondTrainer, 
			long winnerId, GeoPosition location) {
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

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public TrainerDTO getFirstTrainer() {
		return firstTrainer;
	}

	public void setFirstTrainer(TrainerDTO firstTrainer) {
		this.firstTrainer = firstTrainer;
	}

	public TrainerDTO getSecondTrainer() {
		return secondTrainer;
	}

	public void setSecondTrainer(TrainerDTO secondTrainer) {
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
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Battle: { [");
		buffer.append(id);
		buffer.append("]; ");
		buffer.append(firstTrainer.getLastName());
		buffer.append(", ");
		buffer.append(secondTrainer.getLastName());
		buffer.append(" }");
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BattleDTO))
			return false;
		if (obj == this)
			return true;

		BattleDTO rhs = (BattleDTO) obj;
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


