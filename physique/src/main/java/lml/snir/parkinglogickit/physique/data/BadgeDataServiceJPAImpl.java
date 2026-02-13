package lml.snir.parkinglogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author Virgile Alari
 */
public class BadgeDataServiceJPAImpl extends AbstracCrudServiceJPA<Badge> implements BadgeDataService {

    public BadgeDataServiceJPAImpl(String PU) {
        super(PU);
    }

    @Override
    public Badge getByContent(String contenu) throws Exception {
        Badge badge;
        try {
            this.open();
            Query query = em.createQuery("SELECT b FROM Badge b WHERE b.contenu = fcontenu");
            query.setParameter("fcontenu", contenu);
            badge = (Badge) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }

        return badge;
    }

}
