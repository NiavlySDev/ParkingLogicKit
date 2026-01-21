package lml.snir.parklogickit.metier.entity;

import java.text.ParseException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import lml.snir.tools.DateConverter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
public class Access implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Driver driver;
    private Badge badge;
    private Date date;
    private Event event;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Access)) {
            return false;
        }
        Access other = (Access) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("(Access) ");

        // Formatage de la date
        String strDate = "";
        try {
            strDate = DateConverter.formatTimeStamp(this.date);
        } catch (ParseException ex) {
            Logger.getLogger(Access.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Construction de la chaîne finale
        builder.append("ID = ").append(this.id)
                .append(" | ")
                .append(this.driver != null ? this.driver : "Driver inconnu")
                .append(" est ")
                .append(this.event != null ? this.event.getAccessType() : "Action inconnue")
                .append(" le ")
                .append(strDate);

        return builder.toString();
    }
}
