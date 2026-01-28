package lml.snir.parklogickit.metier.entity;

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
 * @author phily, sylvain
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public class Maintenance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private boolean isAdmin;

    /**
     * Retourne L'Identifiant unique du Technicien
     *
     * @return id : L'Identifiant unique du Technicien
     */
    public long getId() {
        return id;
    }

    /**
     * Modifie L'Identifiant unique du Technicien
     *
     * @param id : L'Identifiant unique du Technicien à modifier
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retourne le Nom d'Utilisateur (Username) du Technicien (Maintenance)
     *
     * @return username : le Nom d'Utilisateur (Username) du Technicient
     * (Maintenance)
     */
    public String getUsername() {
        return username;
    }

    /**
     * Modifier le Nom d'Utilisateur (Username) du Technicien (Maintenance)
     *
     * @param username : le Nom d'Utilisateur (Username) du Technicient
     * (Maintenance) à modifier
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retourne si le Technicien (Maintenance) est admin ou non
     *
     * @return isAdmin : True si admin, False si non
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * Modifier si le Technicien est admin ou non.
     *
     * @param isAdmin : True si admin, False si non
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Retourne le Mot de Passe (Password) du Technicien (Maintenance)
     *
     * @return password : Le Mot de Passe (Password) du Technicien (Maintenance)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Modifier le Mot de Passe (Password) du Technicien (Maintenance)
     *
     * @param password : Le Mot de Passe (Password) du Technicien (Maintenance)
     * à modifier
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Vérifier si un objet est égal au Technicien (Maintenance)
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
        final Maintenance other = (Maintenance) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.isAdmin != other.isAdmin) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return Objects.equals(this.password, other.password);
    }

    /**
     * Retourne un identifiant unique correspondant au Technicien (Maintenance)
     * uniquement
     *
     * @return Identifiant unique correspondant au Technicien (Maintenance)
     * uniquement.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 37 * hash + Objects.hashCode(this.username);
        hash = 37 * hash + Objects.hashCode(this.password);
        hash = 37 * hash + (this.isAdmin ? 1 : 0);
        return hash;
    }

    /**
     * Retourne le Technicien (Maintenance) sous forme de String
     *
     * @return Le Technicien (Maintenance) sous forme de String.
     */
    @Override
    public String toString() {
        String str = "Identifiant Technicien : " + id + " | ";
        str += "IsAdmin? " + isAdmin;
        return str;
    }
}
