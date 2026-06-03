package lml.snir.parkinglogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author Virgile Alari
 */
public class AccessDataServiceJPAImpl extends AbstracCrudServiceJPA<Access> implements AccessDataService {

    public AccessDataServiceJPAImpl(String PU) {
        super(PU);
    }

    @Override
    public List<Access> getByDriver(String driver) throws Exception {
        List<Access> access;
        try {
            this.open();
            Query query = em.createQuery("SELECT a FROM Access a WHERE a.driver_id = :fdriver");
            query.setParameter("fdriver", driver);
            access = query.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        return access;
    }

    @Override
    public List<Access> getByDate(String date) throws Exception {
        List<Access> access;
        try {
            this.open();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateJ = sdf.parse(date);

            Query query = em.createQuery("SELECT a FROM Access a WHERE a.date = :fdate");
            query.setParameter("fdate", dateJ);
            access = query.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        return access;
    }

    @Override
    public List<Access> getByIsOpen(boolean attribue) throws Exception {
        List<Access> access;
        try {
            this.open();
            Query query = em.createQuery("SELECT a FROM Access a WHERE a.isOpen = :fisOpen");
            query.setParameter("fisOpen", attribue);
            access = query.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        return access;
    }
}
