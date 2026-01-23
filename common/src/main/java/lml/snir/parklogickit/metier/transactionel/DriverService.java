/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Driver;
import lml.snir.persistence.CrudService;


/**
 *
 * @author phily
 */
   public interface DriverService extends CrudService<Driver> {

    public Driver getById(long id) throws Exception;
    public Driver getByUsername(String contenu) throws Exception;
    public Driver getByFirstname(String contenu) throws Exception;
    public Driver getBySurname(String contenu) throws Exception;
    public Driver getByCountPlace(int contenu) throws Exception;
    public Driver getByIsMale(boolean attribue) throws Exception;
    public Driver getByPassword(String contenu) throws Exception;
   
   }