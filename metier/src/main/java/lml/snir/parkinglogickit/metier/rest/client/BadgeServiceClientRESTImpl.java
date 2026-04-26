package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.transactionel.BadgeService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class BadgeServiceClientRESTImpl extends ClientRest<Badge> implements BadgeService {

    public BadgeServiceClientRESTImpl() {
        super.init("BadgeService", new RestServerLocalConfiguration());
    }

    /**
     * Ajoute un Badge de la Base de Données
     *
     * @param badge : Badge a ajouter
     * @return : Logs Méthode Parente
     * @throws java.lang.Exception
     */
    @Override
    public Badge add(Badge badge) throws Exception {
        super.setPath("");
        return super.addEntity(badge);
    }

    /**
     * Retire une Badge de la Base de Données
     *
     * @param badge : Badge a retirer
     * @throws java.lang.Exception
     */
    @Override
    public void remove(Badge badge) throws Exception {
        super.setPath("");
        super.removeEntity(badge);
    }

    /**
     * Modifie une Badge de la Base de Données
     *
     * @param badge : Badge a retirer
     * @throws java.lang.Exception
     */
    @Override
    public void update(Badge badge) throws Exception {
        super.setPath("");
        super.updateEntity(badge);
    }

    /**
     * Retourne les Badges reliés à un Contenu
     *
     * @param content : Contenu
     * @return : Liste de tous les Associate associé au Contenu
     * @throws java.lang.Exception
     */
    @Override
    public Badge getByContent(String content) throws Exception {
        super.setPath("getByContent/" + content);
        return super.getEntity();
    }

    /**
     * Retourne une Badge à partir de son ID
     *
     * @param id : ID
     * @return : le Badges à partir de son ID
     * @throws java.lang.Exception
     */
    @Override
    public Badge getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    /**
     * Retourne le nombre de Badge dans la Base de Données
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
     * Retourne tous les Badges dans la Base de Données
     *
     * @return : Liste de tous les Badges
     * @throws java.lang.Exception
     */
    @Override
    public List<Badge> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    /**
     * Retourne tous les Badges dans la Base de Données, paginé
     *
     * @param begin : Début de la Pagination
     * @param count : Nombre à paginer
     * @return : Liste de tous les Badges
     * @throws java.lang.Exception
     */
    @Override
    public List<Badge> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

}
