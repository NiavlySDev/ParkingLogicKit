/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parkinglogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author jupiter
 */
public class AssociateDataServiceImpl extends AbstracCrudServiceJPA<Associate> implements AssociateDataService {

    public AssociateDataServiceImpl(String PU) {
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

    public Boolean isBadgeAttribue(Driver driver) throws Exception {
        return (this.getByUtilisateur(driver) != null);
    }

    public Boolean isBadgeAttribue(Badge badge) throws Exception {
        return (this.getByBadge(badge) != null);
    }

}
