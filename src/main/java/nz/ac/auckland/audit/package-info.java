@org.hibernate.annotations.GenericGenerator(
        name = "ID_GENERATOR",
        strategy = "enhanced-sequence"
)

@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type=LocalDate.class,
                value=LocalDateAdapter.class),
        @XmlJavaTypeAdapter(type=LocalTime.class,
                value=LocalTimeAdapter.class),
        @XmlJavaTypeAdapter(type=DateTime.class,
                value=DateTimeAdapter.class)
})

package nz.ac.auckland.audit;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import nz.ac.auckland.pokemon.jaxb.DateTimeAdapter;
import nz.ac.auckland.pokemon.jaxb.LocalDateAdapter;
import nz.ac.auckland.pokemon.jaxb.LocalTimeAdapter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;