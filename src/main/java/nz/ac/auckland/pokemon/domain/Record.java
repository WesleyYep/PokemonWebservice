package nz.ac.auckland.pokemon.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.HashSet;
import java.util.Set;

/**
 * Bean class to represent a Pokemon trainer's record.
 *
 * It keeps track the trainers win/loss record, badges, and competition wins
 *
 * @author Wesley Yep
 *
 */

//@Embeddable
//@XmlRootElement(name="record")
//@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Record {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
//	@XmlElement(name="battlesWon")
	private int battlesWon;
	
//	@XmlElement(name="battlesLost")
	private int battlesLost;
	
//	@XmlElement(name="badgesWon")
	private int badgesWon;
	
//	@XmlElement(name="competitionWins")
	private int competitionWins;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	 @JoinTable(
	 name = "RECORD_BATTLES",
	 joinColumns = @JoinColumn(name = "RECORD_ID"),
	 inverseJoinColumns = @JoinColumn(name = "BATTLE_ID")
	 )
	private Set<Battle> battles = new HashSet<Battle>();

	public Record() {
		badgesWon = 0;
		competitionWins = 0;
		battlesWon = 0;
		battlesLost = 0;
	}

	public Record(int battlesWon, int battlesLost, int badgesWon, int competitionWins) {
		this.battlesWon = battlesWon;
		this.battlesLost = battlesLost;
		this.badgesWon = badgesWon;
		this.competitionWins = competitionWins;
	}

	public int getBattlesWon() {
		return battlesWon;
	}

	public void setBattlesWon(int battlesWon) {
		this.battlesWon = battlesWon;
	}
	
	public int getBattlesLost() {
		return battlesLost;
	}

	public void setBattlesLost(int battlesLost) {
		this.battlesLost = battlesLost;
	}
	
	public int getBadgesWon() {
		return badgesWon;
	}

	public void setBadgesWon(int badgesWon) {
		this.badgesWon = badgesWon;
	}
	
	public int getCompetitionWins() {
		return competitionWins;
	}

	public void setCompetitionWins(int competitionWins) {
		this.competitionWins = competitionWins;
	}
	
	public Set<Battle> getBattles() {
		return battles;
	}
	
	public void setBattles(Set<Battle> battles) {
		this.battles = battles;
	}

}
