/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Access;
import lml.snir.persistence.CrudService;
/**
 *
 * @author phily
 */
   public interface AccessService extends CrudService<Access> {

    public Access getById(long id) throws Exception;
    public Access getByDriver(String contenu) throws Exception;
    public Access getByBadge(String contenu) throws Exception;
    public Access getByDateTime(String contenu) throws Exception;
    public Access getByEvent(String contenu) throws Exception;
   }