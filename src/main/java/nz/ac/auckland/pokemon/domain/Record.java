package nz.ac.auckland.pokemon.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.HashSet;
import java.util.Set;

/**
 * Bean class to represent a Pokemon trainer's record.
 *
 * It keeps track the trainers win/loss record, badges, and competition wins
 * It is embedded inside a trainer Entity since a Record should not exist without the trainer that it belongs to
 *
 * @author Wesley Yep
 *
 */

@Embeddable
@XmlRootElement(name="record")
@XmlAccessorType(XmlAccessType.FIELD)
public class Record {
	
	@XmlElement(name="battlesWon")
	private int battlesWon;
	
	@XmlElement(name="battlesPlayed")
	private int battlesPlayed;
	
	@XmlElement(name="badgesWon")
	private int badgesWon;
	
	@XmlElement(name="competitionWins")
	private int competitionWins;

	public Record() {
		badgesWon = 0;
		competitionWins = 0;
		battlesWon = 0;
		battlesPlayed = 0;
	}

	public Record(int battlesWon, int battlesLost, int badgesWon, int competitionWins) {
		this.battlesWon = battlesWon;
		this.battlesPlayed = battlesLost;
		this.badgesWon = badgesWon;
		this.competitionWins = competitionWins;
	}

	public int getBattlesWon() {
		return battlesWon;
	}

	public void setBattlesWon(int battlesWon) {
		this.battlesWon = battlesWon;
	}
	
	public int getBattlesPlayed() {
		return battlesPlayed;
	}

	public void setBattlesPlayed(int battlesPlayed) {
		this.battlesPlayed = battlesPlayed;
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
