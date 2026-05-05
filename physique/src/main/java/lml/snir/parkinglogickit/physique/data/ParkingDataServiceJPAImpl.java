package lml.snir.parkinglogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author Virgile Alari
 */
public class ParkingDataServiceJPAImpl extends AbstracCrudServiceJPA<Parking> implements ParkingDataService {

    public ParkingDataServiceJPAImpl(String PU) {
        super(PU);
    }

    @Override
    public Parking getByIsFull(boolean isFull) throws Exception {
        Parking parking = null;
        try {
            this.open();
            Query query = em.createQuery("SELECT p FROM Parking p WHERE p.isFull = :fisFull");
            query.setParameter("fisFull", isFull);
            parking = (Parking) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        return parking;
    }

    @Override
    public Parking getByPlaceCount(int placeCount) throws Exception {
        Parking parking = null;
        try {
            this.open();
            Query query = em.createQuery("SELECT p FROM Parking p WHERE p.placeCount = :fplaceCount");
            query.setParameter("fplaceCount", placeCount);
            parking = (Parking) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        return parking;
    }

    @Override
    public Parking getByTotalPlace(int totalPlace) throws Exception {
        Parking parking = null;
        try {
            this.open();
            Query query = em.createQuery("SELECT p FROM Parking p WHERE p.totalPlace = :ftotalPlace");
            query.setParameter("ftotalPlace", totalPlace);
            parking = (Parking) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        return parking;
    }

}
