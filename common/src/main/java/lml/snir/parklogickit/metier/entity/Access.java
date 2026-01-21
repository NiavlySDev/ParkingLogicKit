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

/**
 * @author ethan, sylvain
 */
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
    
    /**
     * Retourne l'identifiant unique de l'accès
     * @return id : L'identifiant unique de l'accès
     */
    public Long getId() {
        return id;
    }
    /**
     * Modifier l'identifiant unique de l'accès
     * @param id : L'identifiant à modifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne l'objet Conducteur lié à l'accès (Driver)
     * @return driver : Le Conducteur lié à l'accès (Driver)
     */
    public Driver getDriver() {
        return driver;
    }
    /**
     * Modifier le Conducteur lié a l'accès (Driver)
     * @param driver : Le Conducteur à Modifier
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Retourne le Badge lié à l'accès
     * @return badge : Le Badge lié à l'accès
     */
    public Badge getBadge() {
        return badge;
    }
    /**
     * Modifier le Badge lié à l'accès
     * @param badge : Le Badge à modifier
     */
    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    /**
     * Retourne la Date liée à l'accès
     * @return date : La Date liée à l'accès
     */
    public Date getDate() {
        return date;
    }
    /**
     * Modifier la date liée à l'accès
     * @param date : La Date à modifier
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retourne l'évènement lié a l'accès
     * @return event : L'évènement lié à l'accès
     */
    public Event getEvent() {
        return event;
    }
    /**
     * Modifier l'évènement lié à l'accès
     * @param event : L'évènement a modifier
     */
    public void setEvent(Event event) {
        this.event = event;
    }
    
    /**
     * Retourne un identifiant unique correspondant au Badge uniquement
     * @return Identifiant unique correspondant au Badge uniquement.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /**
     * Vérifier si un objet est égal à l'accès
     * @param object : L'objet a vérifier
     * @return True si égal, False si non.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Access)) {
            return false;
        }
        Access other = (Access) object;
        return !((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id)));
    }
    /**
     * Retourne l'Accès sous forme de String
     * @return Accès sous forme de String.
     */
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
