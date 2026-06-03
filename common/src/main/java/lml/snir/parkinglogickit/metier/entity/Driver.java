package lml.snir.parkinglogickit.metier.entity;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 *
 * @author Ethan Chandebois, Sylvain Crocquevieille
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public class Driver implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String lastName;
    private String firstName;
    @Column(unique = true)
    private String username;
    private String password;
    private boolean isMale;
    private int age;

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
     * @return lastName : Le Nom du Conducteur (Driver)
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Modifier le Nom du Conducteur (Driver)
     *
     * @param lastName : Le Nom du Conducteur (Driver) à modifier.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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

        String str = genre + " " + this.getFirstName() + " " + this.getLastName();

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
        if (!Objects.equals(this.lastName, other.lastName)) {
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

    /**
     * Retourne l'age du Driver
     *
     * @return age : L'age du Driver
     */
    public int getAge() {
        return age;
    }

    /**
     * Modifie l'age du Driver
     *
     * @param age : L'Age à modifier
     */
    public void setAge(int age) {
        this.age = age;
    }

    
        private String codeMD5(String msg) throws NoSuchAlgorithmException {
        String code = "";
        byte[] b;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            b = md.digest(msg.getBytes());
            for (int i = 0; i < b.length; i++) {
                int x = b[i];

                if (x < 0) {
                    x += 256;
                }

                String s = String.format("%02x", x);
                code += s;
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }

        return code;
    }

    public boolean isValid(String password) throws NoSuchAlgorithmException {
        return this.password.equals(this.codeMD5(password));
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the mdp to set
     */
    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = this.codeMD5(password);
    }
    
    public void setEncodedPassword (String password) {
        this.password = password;
    }
}
