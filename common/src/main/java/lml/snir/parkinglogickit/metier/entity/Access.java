package lml.snir.parkinglogickit.metier.entity;

import java.text.ParseException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;
import lml.snir.tools.DateConverter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ethan Chandebois, Sylvain Crocquevieille, Virgile Alari
 */
@Entity
public class Access implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Driver driver;
    private boolean badge;
    private boolean plate;
    private boolean digicode;
    private Date date;
    private boolean isOpen;

    /**
     * Retourne l'identifiant unique de l'accès
     *
     * @return id : L'identifiant unique de l'accès
     */
    public Long getId() {
        return id;
    }

    /**
     * Modifier l'identifiant unique de l'accès
     *
     * @param id : L'identifiant à modifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne l'objet Conducteur lié à l'accès (Driver)
     *
     * @return driver : Le Conducteur lié à l'accès (Driver)
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Modifier le Conducteur lié a l'accès (Driver)
     *
     * @param driver : Le Conducteur à Modifier
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Retourne la Date liée à l'accès
     *
     * @return date : La Date liée à l'accès
     */
    public Date getDate() {
        return date;
    }

    /**
     * Modifier la date liée à l'accès
     *
     * @param date : La Date à modifier
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retourne un identifiant unique correspondant au Badge uniquement
     *
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
     *
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
     *
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
                .append(" le ")
                .append(strDate);

        return builder.toString();
    }

    public boolean isIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isBadge() {
        return badge;
    }

    public void setBadge(boolean badge) {
        this.badge = badge;
    }

    public boolean isPlate() {
        return plate;
    }

    public void setPlate(boolean plate) {
        this.plate = plate;
    }

    public boolean isDigicode() {
        return digicode;
    }

    public void setDigicode(boolean digicode) {
        this.digicode = digicode;
    }
}
