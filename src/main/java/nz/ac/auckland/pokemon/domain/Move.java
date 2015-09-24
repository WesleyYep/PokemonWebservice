package nz.ac.auckland.pokemon.domain;

import javax.persistence.Embeddable;

/**
 * Bean class to represent a Pokemon move.
 *
 * For this first Web service, a Move is simply represented by a unique id,
 * a name, attack power, accuracy, and type.
 *
 * @author Wesley Yep
 *
 */

@Embeddable
public class Move {
	private String name;
	private int power;
	private int accuracy;
	private Type type;
	
	public Move() {}
	
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
