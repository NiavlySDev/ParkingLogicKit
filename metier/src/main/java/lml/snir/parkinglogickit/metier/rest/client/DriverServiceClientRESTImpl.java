package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class DriverServiceClientRESTImpl extends ClientRest<Driver> implements DriverService {

    public DriverServiceClientRESTImpl() {
        super.init("DriverService", new RestServerLocalConfiguration());
    }

    /**
     * Ajoute un Driver de la Base de Données
     *
     * @param driver : Driver a ajouter
     * @return : Logs Méthode Parente
     * @throws java.lang.Exception
     */
    @Override
    public Driver add(Driver driver) throws Exception {
        super.setPath("");
        return super.addEntity(driver);
    }

    /**
     * Retire un Driver de la Base de Données
     *
     * @param driver : Driver a retirer
     * @throws java.lang.Exception
     */
    @Override
    public void remove(Driver driver) throws Exception {
        super.setPath("");
        super.removeEntity(driver);
    }

    /**
     * Modifie un Driver de la Base de Données
     *
     * @param driver : Driver a retirer
     * @throws java.lang.Exception
     */
    @Override
    public void update(Driver driver) throws Exception {
        super.setPath("");
        super.updateEntity(driver);
    }

    /**
     * Retourne les Driver reliés à leurs Login
     *
     * @param login : Login
     * @return : Liste de tous les Driver associé leurs Login
     * @throws java.lang.Exception
     */
    @Override
    public Driver getByUsername(String login) throws Exception {
        super.setPath("getByUsername/" + login);
        return super.getEntity();
    }

    /**
     * Retourne un Driver à partir de son ID
     *
     * @param id : ID
     * @return : le Driver à partir de son ID
     * @throws java.lang.Exception
     */
    @Override
    public Driver getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    /**
     * Retourne le nombre de Driver dans la Base de Données
     *
     * @return : Le nombre
     * @throws java.lang.Exception
     */
    @Override
    public long getCount() throws Exception {
        super.setPath("Count");
        return super.getCountEntity();
    }

    /**
     * Retourne tous les Drivers dans la Base de Données
     *
     * @return : Liste de tous les Drivers
     * @throws java.lang.Exception
     */
    @Override
    public List<Driver> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    /**
     * Retourne tous les Driver dans la Base de Données, paginé
     *
     * @param begin : Début de la Pagination
     * @param count : Nombre à paginer
     * @return : Liste de tous les Driver
     * @throws java.lang.Exception
     */
    @Override
    public List<Driver> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }
}
