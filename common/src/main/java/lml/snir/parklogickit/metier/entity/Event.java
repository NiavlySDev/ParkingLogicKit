package lml.snir.parklogickit.metier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ethan, sylvain
 */
@Entity
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private AccessType accessType;

    /**
     * Retourne l'identifiant unique de L'Evènement (Event)
     * @return id : L'identifiant unique de l'Evènement
     */
    public Long getId() {
        return id;
    }
    /**
     * Modifier l'identifiant unique de l'Evènement (Event)
     * @param id : L'identifiant de l'Evènement (Event) à modifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne si l'évènement (Event) est une "Entrée" ou une "Sortie"
     * @return accessType : "Entrée" ou "Sortie"
     */
    public AccessType getAccessType() {
        return accessType;
    }
    /**
     * Modifier si l'évènement (Event) est une "Entrée" ou une "Sortie"
     * @param accessType : Le type d'accès à modifier : "Entrée" ou "Sortie"
     */
    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    /**
     * Retourne un identifiant unique correspondant à L'Evènement (Event) uniquement
     * @return Identifiant unique correspondant à L'Evènement (Event) uniquement.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /**
     * Vérifier si un objet est égal à L'Evènement (Event)
     * @param object : L'objet a vérifier
     * @return True si égal, False si non.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Event other = (Event) object;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return this.accessType == other.accessType;
    }
    /**
     * Retourne L'Evènement (Event) sous forme de String
     * @return L'Evènement (Event) sous forme de String.
     */
    @Override
    public String toString() {
        String str = "";
        str+="Event n°"+id+" | ";
        str+="";
        return str;
    }

}
