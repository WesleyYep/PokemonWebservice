package nz.ac.auckland.pokemon.domain;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to represent a geographic location in terms of latitude and longitude of a battle
 * Code modified from parolee.Geolocation from the Parolee domain model.
 *
 */
@Embeddable
@XmlRootElement(name="geoposition")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeoPosition {
	
	@XmlElement(name="latitude")
	private double latitude;
	
	@XmlElement(name="longitude")
	private double longitude;
	
	protected GeoPosition() {}
	
	public GeoPosition(double lat, double lng) {
		latitude = lat;
		longitude = lng;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GeoPosition))
            return false;
        if (obj == this)
            return true;

        GeoPosition rhs = (GeoPosition) obj;
        return new EqualsBuilder().
            append(latitude, rhs.latitude).
            append(longitude, rhs.longitude).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(latitude).
	            append(longitude).
	            toHashCode();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("(");
		buffer.append(latitude);
		buffer.append(",");
		buffer.append(longitude);
		buffer.append(")");
		
		return buffer.toString();
	}
}
