package lml.snir.parkinglogickit.metier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sylvain Crocquevieille
 */
@Entity
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numberPlate;
    private String brand;
    private VehicleType type;

    /**
     * Récupérer l'identifiant unique de la Voiture (Vehicle)
     *
     * @return id : L'identifiant unique de la Voiture (Vehicle)
     */
    public Long getId() {
        return id;
    }

    /**
     * Modifier l'identifiant unique de la Voiture (Vehicle)
     *
     * @param id : L'identifiant unique de la Voiture (Vehicle) à modifier.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupérer la Plaque de la Voiture (Vehicle)
     *
     * @return numberPlate : la Plaque de la Voiture (Vehicle)
     */
    public String getNumberPlate() {
        return numberPlate;
    }

    /**
     * Modifier la Plaque de la Voiture (Vehicle)
     *
     * @param numberPlate : la Plaque de la Voiture (Vehicle) à modifier.
     */
    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    /**
     * Récupérer le type de la Voiture (Vehicle)
     *
     * @return type : le type de la Voiture (Vehicle)
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * Modifier le type de la Voiture (Vehicle)
     *
     * @param type : le type de la Voiture (Vehicle) à modifier.
     */
    public void setType(VehicleType type) {
        this.type = type;
    }

    /**
     * Retourne un identifiant unique correspondant à la Voiture (Vehicle)
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
     * Vérifier si un objet est égal à la Voiture (Vehicle)
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
        final Vehicle other = (Vehicle) object;
        if (!Objects.equals(this.numberPlate, other.numberPlate)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return this.type == other.type;
    }

    /**
     * Retourne le Voiture (Vehicle) sous forme de String
     *
     * @return Voiture (Vehicle) sous forme de String.
     */
    @Override
    public String toString() {
        String str = "Identifiant Voiture : " + id + " | ";
        str += "Plaque : " + numberPlate + " | ";
        str += "Type de Voiture : " + type + " | ";
        return str;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
