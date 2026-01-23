package lml.snir.parklogickit.metier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;

/**
 *
<<<<<<< HEAD
<<<<<<< HEAD
 * @author virgile, sylvain
=======
 * @author virgile
>>>>>>> origin/main
=======
 * @author virgile, sylvain
>>>>>>> ee7b45e77baec9b0234ef035ed3a16da927d2d0b
 */
@Entity
public class Parking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean isFull;
    private int countPlace;
    private int maxPlace;

    /**
     * Retourne L'Identifiant unique du Parking
     * @return id : L'Identifiant unique du Parking
     */
    public Long getId() {
        return id;
    }
    /**
     * Modifier L'Identifiant unique du Parking
     * @param id : L'Identifiant unique du Parking à modifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne si le Parking est Plein
     * @return isFull : True si le Parking est plein, False sinon
     */
    public boolean isIsFull() {
        return isFull;
    }
    /**
     * Modifier si le Parking est Plein ou non.
     * @param isFull : True si le Parking est plein, False sinon
     */
    public void setIsFull(boolean isFull) {
        this.isFull = isFull;
    }

    /**
     * Retourne Le Nombre de Places disponibles dans le Parking
     * @return 
     */
    public int getCountPlace() {
        return countPlace;
    }
    /**
     * Modifier Le Nombre de Places disponibles dans le Parking
     * @param countPlace : Le Nombre de Places disponibles dans le Parking
     */
    public void setCountPlace(int countPlace) {
        if(countPlace <= maxPlace){
            this.countPlace = countPlace;
        }
    }
    /**
     * Augmenter de 1 Le Nombre de Places disponibles dans le Parking
     */
    public void incrementPlaceCount() {
        if(this.countPlace < maxPlace){
            this.countPlace++;
        }
    }
    /**
     * Diminuer de 1 Le Nombre de Places disponibles dans le Parking
     */
    public void decrementPlaceCount() {
        if (this.countPlace > 0) {
            this.countPlace--;
        }
    }
    /**
     * Ajouter {amount} Nombre de Places disponibles dans le Parking
     * @param amount : Le Nombre de Places disponible à Ajouter dans le Parking
     */
    public void addPlaceCount(Integer amount){
        if(countPlace+amount <= maxPlace){
            this.countPlace+=amount;
        }
    }
    /**
     * Retirer {amount} Nombre de Places disponibles dans le Parking
     * @param amount : Le Nombre de Places disponibles à Ajouter dans le Parking
     */
    public void removePlaceCount(Integer amount){
        if(this.countPlace-amount > 0){
            this.countPlace-=amount;
        }
    }
    
    /**
     * Retourne Le Nombre Maximum de Places dans le Parking
     * @return maxPlace : Le Nombre Maximum de Places dans le Parking
     */
    public int getMaxPlace() {
        return maxPlace;
    }
    /**
     * Modifier Le Nombre Maximum de Places dans le Parking
     * @param maxPlace : Le Nombre Maximum de Places dans le Parking à modifier
     */
    public void setMaxPlace(int maxPlace) {
        this.maxPlace = maxPlace;
    }
    
    /**
     * Retourne un identifiant unique correspondant au Parking uniquement
     * @return Identifiant unique correspondant au Parking uniquement.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /**
     * Vérifier si un objet est égal à ce Parking
     * @param object : L'objet a vérifier
     * @return True si égal, False si non.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parking)) {
            return false;
        }
        Parking other = (Parking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /**
     * Retourne le Parking sous forme de String
     * @return Parking sous forme de String.
     */
    @Override
    public String toString() {
        String str = "Identifiant du Parking : "+id+" | ";
        str+="Place Count : "+countPlace+" | ";
        str+="Max Place : "+maxPlace+" | ";
        str+="isFull? "+isFull;
        return str;
    }
}