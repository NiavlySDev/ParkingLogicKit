package lml.snir.parkinglogickit.metier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Virgile Alari
 */
@Entity
public class Associate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Badge badge;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Driver driver;

    @OneToOne
    @JoinColumn(nullable = false)
    private Vehicle vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Associate)) {
            return false;
        }
        Associate other = (Associate) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.badge);
        hash = 79 * hash + Objects.hashCode(this.driver);
        return hash;
    }

    @Override
    public String toString() {
        return "lml.snir.parkinglogickit.metier.entity.Attribution[ id=" + getId() + " ]";
    }

    /**
     * @return the badge
     */
    public Badge getBadge() {
        return badge;
    }

    /**
     * @param badge the badge to set
     */
    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    /**
     * @return the utilisateur
     */
    public Driver getUtilisateur() {
        return driver;
    }

    /**
     * @param utilisateur the utilisateur to set
     */
    public void setUtilisateur(Driver utilisateur) {
        this.driver = utilisateur;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

}
