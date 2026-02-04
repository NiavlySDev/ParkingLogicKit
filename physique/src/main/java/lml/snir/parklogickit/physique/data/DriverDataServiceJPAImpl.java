/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parklogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lml.snir.parklogickit.metier.entity.Driver;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author virgile
 */
public class DriverDataServiceJPAImpl extends AbstracCrudServiceJPA<Driver> implements DriverDataService {

    public DriverDataServiceJPAImpl(String PU) {
        super(PU);
    }

    public Driver getUsername(String username) throws Exception {
        Driver user = null;
        try {
            this.open();
            Query query = em.createQuery("SELECT d FROM Driver d WHERE u.driver = :fusername");
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
