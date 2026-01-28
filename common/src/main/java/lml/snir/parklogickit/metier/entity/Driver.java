package lml.snir.parklogickit.metier.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ethan, sylvain
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public class Driver implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String surname;
    private String firstName;
    @Column(unique = true)
    private String username;
    private String password;
    private boolean isMale;

    private Car car;

    /**
     * Retourne l'identifiant unique du Conducteur (Driver)
     *
     * @return id : L'identifiant unique du Conducteur (Driver)
     */
    public long getId() {
        return id;
    }

    /**
     * Modifier l'identifiant unique du Conducteur (Driver)
     *
     * @param id : L'identifiant du Conducteur (Driver) à modifier
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retourne le Nom du Conducteur (Driver)
     *
     * @return surname : Le Nom du Conducteur (Driver)
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Modifier le Nom du Conducteur (Driver)
     *
     * @param surname : Le Nom du Conducteur (Driver) à modifier.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Retourne le Prénom du Conducteur (Driver)
     *
     * @return firstName : Le Prénom du Conducteur (Driver)
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Modifier le Prénom du Conducteur (Driver)
     *
     * @param firstName : Le Prénom du Conducteur (Driver) à modifier
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retourne si le Conducteur (Driver) est un Homme ou une Femme
     *
     * @return isMale : True si Homme, False si Femme
     */
    public boolean isIsMale() {
        return isMale;
    }

    /**
     * Modifier si le Conducteur (Driver) est un Homme ou une Femme
     *
     * @param isMale : True si Homme, False si Femme
     */
    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Récupérer l'objet Voiture (Car)
     *
     * @return car : l'objet Voiture (Car)
     */
    public Car getCar() {
        return car;
    }

    /**
     * Retourne un identifiant unique correspondant au Conducteur (Driver)
     * uniquement
     *
     * @return Identifiant unique correspondant au Conducteur (Driver)
     * uniquement.
     */
    @Override
    public int hashCode() {
        return super.hashCode(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    /**
     * Retourne le Conducteur (Driver) sous forme de string
     *
     * @return driverStr : Le Conducteur (Driver) sous forme de string.
     */
    @Override
    public String toString() {
        String genre = "M.";
        if (!this.isIsMale()) {
            genre = "Mme";
        }

        String str = genre + " " + this.getFirstName() + " " + this.getSurname();

        if (this.getClass() == Admin.class) {
            str += " (Admin)";
        }
        return str;
    }

    /**
     * Vérifier si un objet est égal au Conducteur (Driver)
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
        final Driver other = (Driver) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.isMale != other.isMale) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return Objects.equals(this.password, other.password);
    }
}
