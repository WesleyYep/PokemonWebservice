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
 * It keeps track of all the battles that the trainer has been in.
 *
 * @author Wesley Yep
 *
 */

@Embeddable
public class Record {

	@ElementCollection
	@CollectionTable(name = "RECORD_BATTLES",
	joinColumns = @JoinColumn(name = "RECORD_ID"))
	protected Set<Battle> battles = new HashSet<Battle>();

	public Record() {}

	public Record(Set<Battle> battles) {
		this.battles = battles;
	}

	public void addBattle(Battle b) {
		battles.add(b);
	}

	public Set<Battle> getBattles() {
		return battles;
	}

	public void setBattles(Set<Battle> battles) {
		this.battles = battles;
	}

}
