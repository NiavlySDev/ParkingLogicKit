package lml.snir.parkinglogickit.metier.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;

/**
 *
 * @author Ethan Chandebois
 */
@Entity
@DiscriminatorValue("Admin")
public class Admin extends Driver implements Serializable {

}
