package lml.snir.parkinglogickit.metier.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;

/**
 *
 * @author ethan
 */
@Entity
@DiscriminatorValue("Maintenance")
public class Maintenance extends Driver implements Serializable {

}
