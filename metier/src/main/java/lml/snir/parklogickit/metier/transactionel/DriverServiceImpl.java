package lml.snir.parklogickit.metier.transactionel;

import java.util.List;


import lml.snir.parklogickit.metier.entity.Driver;
import lml.snir.parklogickit.data.DriverDataService;

/**
 *
 * @author fanou
 */
public final class DriverServiceImpl implements DriverService {
    private final DriverDataService drvDataSrv;
    
    public DriverServiceImpl() throws Exception {
        this.drvDataSrv = PhysiqueDataFactory.getDriverDataService();
    }
    
    public Driver getByLogin(String login) throws Exception {
        return this.drvDataSrv.getByLogin(login);
    }


    @Override
    public Driver getById(Long id) throws Exception {
        return this.drvDataSrv.getById(id);
    }



    @Override
    public Driver getByUsername(String contenu) throws Exception {
      return this.drvDataSrv.getByUsername();
    }


    @Override
    public Driver getByFirstname(String contenu) throws Exception {
       return this.drvDataSrv.getByFirstname();
    }


    @Override
    public Driver getBySurname(String contenu) throws Exception {
      return this.drvDataSrv.getBySurname();
    }


    @Override
    public Driver getByCountPlace(int contenu) throws Exception {
       return this.drvDataSrv.getByCountPlace();
    }


    @Override
    public Driver getByIsMale(boolean attribue) throws Exception {
         return this.drvDataSrv.getByIsMale();
    }


    @Override
    public Driver getByPassword(String contenu) throws Exception {
     return this.drvDataSrv.getByPassword();
    }

    
}
