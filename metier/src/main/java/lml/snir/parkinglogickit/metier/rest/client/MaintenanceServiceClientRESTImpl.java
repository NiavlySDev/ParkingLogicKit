package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author fanou
 */
public class MaintenanceServiceClientRESTImpl extends ClientRest<Driver> implements DriverService {

    public MaintenanceServiceClientRESTImpl() {
        super.init("DriverService", new RestServerLocalConfiguration());
    }

    @Override
    public Driver add(Driver t) throws Exception {
        super.setPath("");
        return super.addEntity(t);
    }

    @Override
    public void remove(Driver t) throws Exception {
        super.setPath("");
        super.removeEntity(t);
    }

    @Override
    public void update(Driver t) throws Exception {
        super.setPath("");
        super.updateEntity(t);
    }

    @Override
    public Driver getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("Count");
        return super.getCountEntity();
    }

    @Override
    public List<Driver> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    @Override
    public List<Driver> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

    @Override
    public Driver getById(long id) throws Exception {
         super.setPath("getById/" );
        return super.getEntity();
    }
    @Override
    public Driver getByUsername(String contenu) throws Exception {
         super.setPath("getByUsername/" );
        return super.getEntity();
    }

    @Override
    public Driver getByFirstname(String contenu) throws Exception {
         super.setPath("getByFirstname/");
        return super.getEntity();
    }

    @Override
    public Driver getBySurname(String contenu) throws Exception {
       super.setPath("getBySurname/" );
        return super.getEntity();
    }

    @Override
    public Driver getByCountPlace(int contenu) throws Exception {
        super.setPath("getByCountPLace" );
        return super.getEntity();
    }

    @Override
    public Driver getByIsMale(boolean attribue) throws Exception {
         super.setPath("getByIsMale/"  );
        return super.getEntity();
    }

    @Override
    public Driver getByPassword(String contenu) throws Exception {
      super.setPath("getByPassword/" );
        return super.getEntity();
    }

    @Override
    public Driver getById(Driver driver) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
    
}
