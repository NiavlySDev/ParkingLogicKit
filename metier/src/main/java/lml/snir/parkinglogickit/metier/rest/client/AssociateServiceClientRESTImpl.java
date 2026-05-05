package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.AssociateService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class AssociateServiceClientRESTImpl extends ClientRest<Associate> implements AssociateService {

    public AssociateServiceClientRESTImpl() {
        super.init("AssociateService", new RestServerLocalConfiguration());
    }

    /**
     * Ajoute une Associate de la Base de Données
     *
     * @param associate : Associate a ajouter
     * @return : Logs Méthode Parente
     * @throws java.lang.Exception
     */
    @Override
    public Associate add(Associate associate) throws Exception {
        super.setPath("");
        return super.addEntity(associate);
    }

    /**
     * Retire une Associate de la Base de Données
     *
     * @param associate : Associate a retirer
     * @throws java.lang.Exception
     */
    @Override
    public void remove(Associate associate) throws Exception {
        super.setPath("");
        super.removeEntity(associate);
    }

    /**
     * Modifie une Associate de la Base de Données
     *
     * @param associate : Associate a retirer
     * @throws java.lang.Exception
     */
    @Override
    public void update(Associate associate) throws Exception {
        super.setPath("");
        super.updateEntity(associate);
    }

    /**
     * Retourne une Associate à partir de son ID
     *
     * @param id : ID
     * @return : l'Associate à partir de son ID
     * @throws java.lang.Exception
     */
    @Override
    public Associate getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    /**
     * Retourne le nombre d'Associate dans la Base de Données
     *
     * @return : Le nombre
     * @throws java.lang.Exception
     */
    @Override
    public long getCount() throws Exception {
        super.setPath("getCount");
        return super.getCountEntity();
    }

    /**
     * Retourne tous les Associate dans la Base de Données
     *
     * @return : Liste de tous les Associate
     * @throws java.lang.Exception
     */
    @Override
    public List<Associate> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    /**
     * Retourne tous les Associate dans la Base de Données, paginé
     *
     * @param begin : Début de la Pagination
     * @param count : Nombre à paginer
     * @return : Liste de tous les Associate
     * @throws java.lang.Exception
     */
    @Override
    public List<Associate> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

    /**
     * Retourne les Associates reliés à un Badge
     *
     * @param badge : Badge
     * @return : Liste de tous les Associate associé au Badge
     * @throws java.lang.Exception
     */
    @Override
    public Associate getByBadge(Badge badge) throws Exception {
        super.setPath("getByBadge/" + badge);
        return super.getEntity();
    }

    /**
     * Retourne les Associates reliés à un Driver
     *
     * @param driver : Driver
     * @return : Liste de tous les Associate associé au Driver
     * @throws java.lang.Exception
     */
    @Override
    public Associate getByUtilisateur(Driver driver) throws Exception {
        super.setPath("getByUtilisateur/" + driver);
        return super.getEntity();
    }

}
