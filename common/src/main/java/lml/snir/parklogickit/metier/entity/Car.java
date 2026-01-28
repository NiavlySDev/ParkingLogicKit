package lml.snir.parklogickit.metier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author sylvain
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String plaque;
    private CarType type;

    /**
     * Récupérer l'identifiant unique de la Voiture (Car)
     *
     * @return id : L'identifiant unique de la Voiture (Car)
     */
    public Long getId() {
        return id;
    }

    /**
     * Modifier l'identifiant unique de la Voiture (Car)
     *
     * @param id : L'identifiant unique de la Voiture (Car) à modifier.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupérer la Plaque de la Voiture (Car)
     *
     * @return plaque : la Plaque de la Voiture (Car)
     */
    public String getPlaque() {
        return plaque;
    }

    /**
     * Modifier la Plaque de la Voiture (Car)
     *
     * @param plaque : la Plaque de la Voiture (Car) à modifier.
     */
    public void setPlaque(String plaque) {
        this.plaque = plaque;
    }

    /**
     * Récupérer le type de la Voiture (Car)
     *
     * @return type : le type de la Voiture (Car)
     */
    public CarType getType() {
        return type;
    }

    /**
     * Modifier le type de la Voiture (Car)
     *
     * @param type : le type de la Voiture (Car) à modifier.
     */
    public void setType(CarType type) {
        this.type = type;
    }

    /**
     * Retourne un identifiant unique correspondant à la Voiture (Car)
     * uniquement
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
     * Vérifier si un objet est égal à la Voiture (Car)
     *
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
        final Car other = (Car) object;
        if (!Objects.equals(this.plaque, other.plaque)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return this.type == other.type;
    }

    /**
     * Retourne le Voiture (Car) sous forme de String
     *
     * @return Voiture (Car) sous forme de String.
     */
    @Override
    public String toString() {
        String str = "Identifiant Voiture : " + id + " | ";
        str += "Plaque : " + plaque + " | ";
        str += "Type de Voiture : " + type + " | ";
        return str;
    }
}
