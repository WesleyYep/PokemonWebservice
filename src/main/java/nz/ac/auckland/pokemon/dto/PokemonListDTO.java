package nz.ac.auckland.pokemon.dto;

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
 * Created by Wesley on 26/09/2015.
 */
@XmlRootElement(name = "pokemons")
public class PokemonListDTO {

    private List<Link> links;
    private Collection<PokemonDTO> pokemons;

    @XmlElementRef
    public Collection<PokemonDTO> getPokemons() {
        return pokemons;
    }

    public void setPokemons(Collection<PokemonDTO> pokemons) {
        this.pokemons = pokemons;
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
