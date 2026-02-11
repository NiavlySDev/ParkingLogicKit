package lml.snir.parkinglogickit.client.fakedata;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;

/**
 *
 * @author sylvain
 */
@Entity
@DiscriminatorValue("Admin")
public class Admin extends Driver implements Serializable {

}
