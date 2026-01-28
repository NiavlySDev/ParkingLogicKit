/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parklogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.List;
import lml.snir.parklogickit.metier.entity.Badge;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author virgile
 */
public class BadgeDataServiceJPAImpl extends AbstracCrudServiceJPA<Badge> implements BadgeDataService {

    public BadgeDataServiceJPAImpl(String PU) {
        super(PU);
    }
    
    public Badge getByContenu(String contenu) throws Exception {
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

    public List<Badge> getByAssociate(boolean attribue) throws Exception {
        List<Badge> badges;
        try {
            Query query;
            this.open();
            if (attribue) {
                query = em.createQuery("SELECT a.badge FROM Associate a");
            } else {
                query = em.createQuery("SELECT b FROM Badge b WHERE b NOT IN (SELECT a.badge FROM Associate a)");
            }
            
            badges = query.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        
        return badges;
    }
}
