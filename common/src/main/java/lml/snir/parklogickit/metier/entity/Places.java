package lml.snir.parklogickit.metier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author phily, sylvain
 */
@Entity
public class Places implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean isOccuped;
   
    /**
     * Retourne L'Identifiant unique de la Place
     * @return id : L'Identifiant unique de la Place
     */
    public Long getId() {
        return id;
    }
    /**
     * Modifier L'Identifiant unique de la Place
     * @param id : L'Identifiant unique de la Place à modifier.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne si la Place est Occupée ou non
     * @return isOccuped : True si la Place est occuppée, False sinon.
     */
    public boolean isIsOccuped() {
        return isOccuped;
    }
    /**
     * Modifier si la Place est Occupée ou non
     * @param isOccuped  : True si la Place est occuppée, False sinon.
     */
    public void setIsOccuped(boolean isOccuped) {
        this.isOccuped = isOccuped;
    }

    /**
     * Retourne un identifiant unique correspondant à la Place uniquement
     * @return Identifiant unique correspondant à la Place uniquement.
     */
    @Override
    public int hashCode() {
        return super.hashCode(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    /**
     * Vérifier si un objet est égal à la Place
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
        final Places other = (Places) object;
        if (this.isOccuped != other.isOccuped) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }
    /**
     * Retourne la Place sous forme de String
     * @return La Place sous forme de String.
     */
    @Override
    public String toString() {
        String str = "Identifiant de la Place: "+id+" |";
        str+="isOccuped? "+isOccuped;
        return str;
    }
}