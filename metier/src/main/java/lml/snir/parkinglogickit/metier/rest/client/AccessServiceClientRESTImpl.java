package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metier.transactionel.AccessService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class AccessServiceClientRESTImpl extends ClientRest<Access> implements AccessService {

    public AccessServiceClientRESTImpl() {
        super.init("AccessService", new RestServerLocalConfiguration());
    }

    /**
     * Ajoute un accès a la Base de Données
     *
     * @param access: Accès
     * @return : Le retour de la méthode parente
     * @throws java.lang.Exception
     */
    @Override
    public Access add(Access access) throws Exception {
        super.setPath("");
        return super.addEntity(access);
    }

    /**
     * Retire un accès a la Base de Données
     *
     * @param access: Accès
     * @throws java.lang.Exception
     */
    @Override
    public void remove(Access access) throws Exception {
        super.setPath("");
        super.removeEntity(access);
    }

    /**
     * Modifie un accès a la Base de Données
     *
     * @param access: Accès
     * @throws java.lang.Exception
     */
    @Override
    public void update(Access access) throws Exception {
        super.setPath("");
        super.updateEntity(access);
    }

    /**
     * Récupère un Accès à partir de son ID
     *
     * @param id: ID
     * @return : Un accès à partir de son ID
     * @throws java.lang.Exception
     */
    @Override
    public Access getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    /**
     * Récupère le nombre d'accès dans la Base de Données
     *
     * @return : Le nombre d'accès dans la Base de Données
     * @throws java.lang.Exception
     */
    @Override
    public long getCount() throws Exception {
        super.setPath("Count");
        return super.getCountEntity();
    }

    /**
     * Récupère tous les accès dans la Base de Données
     *
     * @return : Tous les accès dans la Base de Données
     * @throws java.lang.Exception
     */
    @Override
    public List<Access> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    /**
     * Récupère tous les accès dans la Base de Données, paginé
     *
     * @param begin: Début de la Pagination
     * @param count: Nombre
     * @return : Tous les accès dans la Base de Données, paginé
     * @throws java.lang.Exception
     */
    @Override
    public List<Access> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

    @Override
    public List<Access> getByDate(String date) throws Exception {
        super.setPath("getByDate/" + date);
        return (List<Access>) super.getEntity();
    }

    @Override
    public List<Access> getByDriver(Long driver) throws Exception {
        super.setPath("getByDriver/" + driver);
        return (List<Access>) super.getEntity();
    }

    @Override
    public List<Access> getByIsOpen(boolean attribue) throws Exception {
        super.setPath("getByIsOpen/" + attribue);
        return (List<Access>) super.getEntity();
    }
}
