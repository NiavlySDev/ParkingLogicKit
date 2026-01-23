/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Driver;
import lml.snir.persistence.CrudService;


/**
 *
 * @author ethan
 */
public interface DriverService extends CrudService<Driver>  {
    public Driver getByLogin (String login) throws Exception;
}
