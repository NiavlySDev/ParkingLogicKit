package lml.snir.parkinglogickit.metier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;

/**
 *
 * @author Virgile Alari, Sylvain Crocquevieille
 */
@Entity
public class Parking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isFull;
    private int placeCount;
    private int totalPlace;
    private int handicapCount;
    private int totalHandicap;

    /**
     * Retourne L'Identifiant unique du Parking
     *
     * @return id : L'Identifiant unique du Parking
     */
    public Long getId() {
        return id;
    }

    /**
     * Modifier L'Identifiant unique du Parking
     *
     * @param id : L'Identifiant unique du Parking à modifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retourne si le Parking est Plein
     *
     * @return isFull : True si le Parking est plein, False sinon
     */
    public boolean isIsFull() {
        return isFull;
    }

    /**
     * Modifier si le Parking est Plein ou non.
     *
     * @param isFull : True si le Parking est plein, False sinon
     */
    public void setIsFull(boolean isFull) {
        this.isFull = isFull;
    }

    /**
     * Retourne Le Nombre de Places disponibles dans le Parking
     *
     * @return
     */
    public int getPlaceCount() {
        return placeCount;
    }

    /**
     * Modifier Le Nombre de Places disponibles dans le Parking
     *
     * @param placeCount : Le Nombre de Places disponibles dans le Parking
     */
//    public void setPlaceCount(int placeCount) {
//        if(placeCount <= totalPlace){
//            this.placeCount = placeCount;
//        }
//    }
    /**
     * Augmenter de 1 Le Nombre de Places disponibles dans le Parking
     */
    public void incrementPlaceCount() {
        if (this.placeCount < totalPlace) {
            this.placeCount++;
        }
    }

    /**
     * Diminuer de 1 Le Nombre de Places disponibles dans le Parking
     */
    public void decrementPlaceCount() {
        if (this.placeCount > 0) {
            this.placeCount--;
        }
    }

    /**
     * Ajouter {amount} Nombre de Places disponibles dans le Parking
     *
     * @param amount : Le Nombre de Places disponible à Ajouter dans le Parking
     */
    public void addPlaceCount(Integer amount) {
        if (placeCount + amount <= totalPlace) {
            this.placeCount += amount;
        }
    }

    /**
     * Retirer {amount} Nombre de Places disponibles dans le Parking
     *
     * @param amount : Le Nombre de Places disponibles à Ajouter dans le Parking
     */
    public void removePlaceCount(Integer amount) {
        if (this.placeCount - amount > 0) {
            this.placeCount -= amount;
        }
    }

    /**
     * Retourne Le Nombre Maximum de Places dans le Parking
     *
     * @return totalPlace : Le Nombre Maximum de Places dans le Parking
     */
    public int getTotalPlace() {
        return totalPlace;
    }

    /**
     * Modifier Le Nombre Maximum de Places dans le Parking
     *
     * @param totalPlace : Le Nombre Maximum de Places dans le Parking à
     * modifier
     */
    public void setTotalPlace(int totalPlace) {
        this.totalPlace = totalPlace;
    }

    /**
     * Retourne un identifiant unique correspondant au Parking uniquement
     *
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
     *
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
     *
     * @return Parking sous forme de String.
     */
    @Override
    public String toString() {
        String str = "Identifiant du Parking : " + id + " | ";
        str += "Place Count : " + placeCount + " | ";
        str += "Max Place : " + totalPlace + " | ";
        str += "isFull? " + isFull;
        return str;
    }

    public int getHandicapCount() {
        return handicapCount;
    }

    public void setHandicapCount(int handicapCount) {
        this.handicapCount = handicapCount;
    }

    public int getTotalHandicap() {
        return totalHandicap;
    }

    public void setTotalHandicap(int totalHandicap) {
        this.totalHandicap = totalHandicap;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

}
