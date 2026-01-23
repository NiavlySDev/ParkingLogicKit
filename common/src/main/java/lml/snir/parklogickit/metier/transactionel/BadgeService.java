/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import java.util.List;
import lml.snir.parklogickit.metier.entity.Badge;
import lml.snir.persistence.CrudService;

/**
 *
 * @author phily
 */
   public interface BadgeService extends CrudService<Badge> {

    public Badge getById(long id) throws Exception;
    public Badge getByContent(boolean attribue) throws Exception;
   }