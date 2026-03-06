package lml.snir.parkinglogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author Virgile Alari
 */
public class DriverDataServiceJPAImpl extends AbstracCrudServiceJPA<Driver> implements DriverDataService {

    public DriverDataServiceJPAImpl(String PU) {
        super(PU);
    }
    
    @Override
    public Driver getByUsername(String username) throws Exception {
        Driver user = null;
        try {
            this.open();
            Query query = em.createQuery("SELECT d FROM Driver d WHERE d.username = :fusername");
            query.setParameter("fusername", username);
            user = (Driver) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        return user;
    }

}
