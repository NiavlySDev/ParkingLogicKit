/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parkinglogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Car;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author Viralu
 */
public class CarDataServiceJPAImpl extends AbstracCrudServiceJPA<Car> implements CarDataService {
        public CarDataServiceJPAImpl(String PU) {
        super(PU);
    }
    
    @Override
    public Car getByContent(String contenu) throws Exception {
        Car badge;
        try {
            this.open();
            Query query = em.createQuery("SELECT b FROM Badge b WHERE b.contenu = fcontenu");                       //PAS FINI
            query.setParameter("fcontenu", contenu);
            badge = (Car) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        
        return badge;
    }

    @Override
    public List<Car> getByAssociate(boolean attribue) throws Exception {
        List<Car> badges;
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
