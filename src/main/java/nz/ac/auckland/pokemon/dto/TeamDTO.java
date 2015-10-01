package nz.ac.auckland.pokemon.dto;

import nz.ac.auckland.pokemon.domain.Pokemon;
import nz.ac.auckland.pokemon.domain.Trainer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

/** Class to represent a Team Data Transmission Object.
 *
 * An instance of this class represents a DTO Team, for being translated into XML and being passed between client/server
 *
 * Created by Wesley on 29/09/2015.
 */

@XmlRootElement(name="team")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamDTO {

    @XmlAttribute(name = "id")
    private long id;

    @XmlElement(name = "teamName")
    private String teamName;

    @XmlElement(name = "teamGrade")
    private String teamGrade;

    @XmlTransient
    private Trainer trainer;

    @XmlTransient
    private Set<Pokemon> pokemon = new HashSet<Pokemon>();

    protected TeamDTO() {
    }

    public TeamDTO(String teamName, String teamGrade, Trainer trainer, Set<Pokemon> pokemon) {
        this.teamName = teamName;
        this.teamGrade = teamGrade;
        this.pokemon = pokemon;
        this.trainer = trainer;
    }

    public TeamDTO(long id, String teamName, String teamGrade, Trainer trainer, Set<Pokemon> pokemon) {
        this.id = id;
        this.teamName = teamName;
        this.teamGrade = teamGrade;
        this.pokemon = pokemon;
        this.trainer = trainer;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamGrade() {
        return teamGrade;
    }

    public void setTeamGrade(String teamGrade) {
        this.teamGrade = teamGrade;
    }

    public Set<Pokemon> getPokemon() {
        return pokemon;
    }

    public void setPokemon(Set<Pokemon> pokemon) {
        this.pokemon = pokemon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pokemon))
            return false;
        if (obj == this)
            return true;

        nz.ac.auckland.pokemon.dto.TeamDTO rhs = (TeamDTO) obj;
        return new EqualsBuilder().
                append(teamName, rhs.teamName).
                append(teamGrade, rhs.teamGrade).
                append(trainer, rhs.trainer).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(teamName).
                append(teamGrade).
                append(trainer).
                toHashCode();
    }
}