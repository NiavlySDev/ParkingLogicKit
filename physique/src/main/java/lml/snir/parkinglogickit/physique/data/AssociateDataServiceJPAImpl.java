package lml.snir.parkinglogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author Virgile Alari
 */
public class AssociateDataServiceJPAImpl extends AbstracCrudServiceJPA<Associate> implements AssociateDataService {

    public AssociateDataServiceJPAImpl(String PU) {
        super(PU);
    }

    @Override
    public Associate getByBadge(Badge badge) throws Exception {
        Associate associate;
        try {
            this.open();
            Query query = em.createQuery("SELECT a FROM Associate a WHERE a.badge = :fbadge");
            query.setParameter("fbadge", badge);
            associate = (Associate) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }

        return associate;
    }

    @Override
    public Associate getByUtilisateur(Driver user) throws Exception {
        Associate associate;
        try {
            this.open();
            Query query = em.createQuery("SELECT a FROM Associate a WHERE a.driver = :fdriver");
            query.setParameter("futilisateur", user);
            associate = (Associate) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }

        return associate;
    }

}
