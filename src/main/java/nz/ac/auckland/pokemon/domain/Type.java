package nz.ac.auckland.pokemon.domain;


/**
 *  Enum to represent the types of each move
 *  Moves can only be certain types, which are shown below
 */

public enum Type {
	NORMAL, WATER, FIRE, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DARK, DRAGON, STEEL, FAIRY;

	public static Type fromString(String text) {
	    if (text != null) {
	      for (Type t : Type.values()) {
	        if (text.equalsIgnoreCase(t.toString())) {
	          return t;
	        }
	      }
	    }
	    return null;
	  }	
}
