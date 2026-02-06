/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Vehicle;

/**
 *
 * @author Viralu
 */
public interface CarService {
    
    public Vehicle getByContent(String contenu) throws Exception;
    public List<Vehicle> getByAssociate(boolean attribue) throws Exception;
}
