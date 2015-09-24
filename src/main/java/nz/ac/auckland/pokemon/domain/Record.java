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

@Embeddable
@XmlRootElement(name="record")
@XmlAccessorType(XmlAccessType.FIELD)
public class Record {
	
	@XmlAttribute(name="battlesWon")
	private int battlesWon;
	
	@XmlAttribute(name="battlesLost")
	private int battlesLost;
	
	@XmlAttribute(name="badgesWon")
	private int badgesWon;
	
	@XmlAttribute(name="competitionWins")
	private int competitionWins;

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

}
