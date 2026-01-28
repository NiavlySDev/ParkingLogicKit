package lml.snir.parklogickit.metier.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;

/**
 *
 * @author ethan
 */
@Entity
@DiscriminatorValue("Admin")
public class Admin extends Driver implements Serializable {

}
