package nz.ac.auckland.pokemon.dto;

import nz.ac.auckland.pokemon.domain.Trainer;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * Class to represent a TrainerList Data Transmission Object.
 * It has HATEOAS support using a list of links which would contain the next and previous links
 *
 * An instance of this class represents a DTO list of trainers, for being translated into XML and being passed between client/server
 * Created by Wesley on 26/09/2015.
 */
@XmlRootElement(name = "trainers")
public class TrainerListDTO {

    private List<Link> links;
    private Collection<TrainerDTO> trainers;

    @XmlElementRef
    public Collection<TrainerDTO> getTrainers() {
        return trainers;
    }

    public void setTrainers(Collection<TrainerDTO> trainers) {
        this.trainers = trainers;
    }

    @XmlElement(name = "link")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @XmlTransient
    public URI getNext() {
        if (links == null) {
            return null;
        }
        for (Link link : links) {
            if ("next".equals(link.getRel())) {
                return link.getUri();
            }
        }
        return null;
    }

    @XmlTransient
    public URI getPrevious() {
        if (links == null) {
            return null;
        }
        for (Link link : links) {
            if ("previous".equals(link.getRel())) {
                return link.getUri();
            }
        }
        return null;
    }
}
