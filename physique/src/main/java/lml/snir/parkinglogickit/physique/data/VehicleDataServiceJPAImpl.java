/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parkinglogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author Virgile
 */
public class VehicleDataServiceJPAImpl extends AbstracCrudServiceJPA<Vehicle> implements VehicleDataService {
        
    public VehicleDataServiceJPAImpl(String PU) {
        super(PU);
    }
    
    @Override
    public Vehicle getByContent(String content) throws Exception {
        Vehicle vehicle;
        try {
            this.open();
            Query query = em.createQuery("SELECT v FROM Vehicle v WHERE v.content = fcontent");                       //PAS FINI
            query.setParameter("fcontent", content);
            vehicle = (Vehicle) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        
        return vehicle;
    }

    @Override
    public List<Vehicle> getByAssociate(boolean associate) throws Exception {
        List<Vehicle> vehicle;
        try {
            Query query;
            this.open();
            if (associate) {
                query = em.createQuery("SELECT a.vehicle FROM Associate a");
            } else {
                query = em.createQuery("SELECT v FROM Vehicle v WHERE v NOT IN (SELECT a.vehicle FROM Associate a)");
            }
            
            vehicle = query.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {
            this.close();
        }
        
        return vehicle;
    }
}
