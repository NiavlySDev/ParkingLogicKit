/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parklogickit.physique.data;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lml.snir.parkinglogickit.physique.data.DriverDataService;
import lml.snir.parkinglogickit.metier.entity.Driver;
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

    @Override
    public Driver getById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Driver getByUsername(String contenu) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Driver getByFirstname(String contenu) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Driver getBySurname(String contenu) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Driver getByCountPlace(int contenu) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Driver getByIsMale(boolean attribue) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Driver getByPassword(String contenu) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Driver getById(Driver driver) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
