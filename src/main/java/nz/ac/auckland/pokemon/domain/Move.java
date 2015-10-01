package nz.ac.auckland.pokemon.domain;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean class to represent a Pokemon move.
 *
 * A Move is represented by a unique id,
 * a name, attack power, accuracy, and type.
 *
 * Pokemon can have a nultiple number of moves. Some pokemon may share the same move
 *
 * @author Wesley Yep
 *
 */

@Embeddable
@XmlRootElement(name="move")
@XmlAccessorType(XmlAccessType.FIELD)
public class Move {

	@XmlElement(name="name")
	private String name;

	@XmlElement(name="power")
	private int power;

	@XmlElement(name="accuracy")
	private int accuracy;

	@XmlElement(name="type")
	@Enumerated
	private Type type;
	
	public Move() {}

	public Move(String name, int power, int accuracy, Type type) {
		this.name = name;
		this.power = power;
		this.accuracy = accuracy;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public int getAccuracy() {
		return accuracy;
	}
	
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type t) {
		this.type = t;
	}
}
