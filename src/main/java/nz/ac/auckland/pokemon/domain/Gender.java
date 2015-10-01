package nz.ac.auckland.pokemon.domain;

/**
 * Simple enumeration for representing Gender.
 * Code borrowed from parolee.Gender from the example project
 */

public enum Gender {
	MALE, FEMALE;
	
	public static Gender fromString(String text) {
	    if (text != null) {
	      for (Gender g : Gender.values()) {
	        if (text.equalsIgnoreCase(g.toString())) {
	          return g;
	        }
	      }
	    }
	    return null;
	  }
}
