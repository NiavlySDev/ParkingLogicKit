package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.transactionel.VehicleService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class VehicleServiceClientRESTImpl extends ClientRest<Vehicle> implements VehicleService {

    public VehicleServiceClientRESTImpl() {
        super.init("VehicleService", new RestServerLocalConfiguration());
    }

    /**
     * Ajoute un Vehicle de la Base de Données
     *
     * @param vehicle : Vehicle a ajouter
     * @return : Logs Méthode Parente
     * @throws java.lang.Exception
     */
    @Override
    public Vehicle add(Vehicle vehicle) throws Exception {
        super.setPath("");
        return super.addEntity(vehicle);
    }

    @Override
    public void remove(Vehicle vehicle) throws Exception {
        super.setPath("");
        super.removeEntity(vehicle);
    }

    @Override
    public void update(Vehicle vehicle) throws Exception {
        super.setPath("");
        super.updateEntity(vehicle);
    }

    @Override
    public Vehicle getByContent(String contenu) throws Exception {
        super.setPath("getByContent/" + contenu);
        return super.getEntity();
    }

    @Override
    public List<Vehicle> getByAssociate(boolean attribue) throws Exception {
        super.setPath("getByAssociate/" + attribue);
        return (List<Vehicle>) super.getEntity();
    }

    @Override
    public Vehicle getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("Count");
        return super.getCountEntity();
    }

    @Override
    public List<Vehicle> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    @Override
    public List<Vehicle> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }
}
