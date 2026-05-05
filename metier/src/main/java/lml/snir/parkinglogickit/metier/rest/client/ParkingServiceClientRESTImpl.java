package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metier.transactionel.ParkingService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class ParkingServiceClientRESTImpl extends ClientRest<Parking> implements ParkingService {

    public ParkingServiceClientRESTImpl() {
        super.init("ParkingService", new RestServerLocalConfiguration());
    }

    /**
     * Ajoute un Parking de la Base de Données
     *
     * @param parking : Parking a ajouter
     * @return : Logs Méthode Parente
     * @throws java.lang.Exception
     */
    @Override
    public Parking add(Parking parking) throws Exception {
        super.setPath("");
        return super.addEntity(parking);
    }

    /**
     * Retire un Parking de la Base de Données
     *
     * @param parking : Parking a retirer
     * @throws java.lang.Exception
     */
    @Override
    public void remove(Parking parking) throws Exception {
        super.setPath("");
        super.removeEntity(parking);
    }

    /**
     * Modifie un Parking de la Base de Données
     *
     * @param parking : Parking a retirer
     * @throws java.lang.Exception
     */
    @Override
    public void update(Parking parking) throws Exception {
        super.setPath("");
        super.updateEntity(parking);
    }

    /**
     * Alors, je sais pas ce que ça fous la, mais azy hassoul On y touche pas
     * car si on touche à ça ça pète toute la partie Physique...Phily je sais
 pas ce que t'as foutu mais tu l'as bien foutu Donc azy on touche pas à
 cette méthode, on fais comme si elle existait pas Et puis comme disait un
 proverbe Chinois: Never touch your code if it works, even if you don't
 know how
     *
     * @param username : Le nom d'utilisateur d'un Parking (carrément le Parking
     * il s'appelle ErosDev35)
     * @return 
     * @throws java.lang.Exception
     */
    @Override
    public Parking getByUsername(String username) throws Exception {
        super.setPath("getByUsername/" + username);
        return super.getEntity();
    }

    /**
     * Retourne les Parkings pleins
     *
     * @param isfull : Login
     * @return : Liste de tous les Parkings pleins
     * @throws java.lang.Exception
     */
    @Override
    public Parking getByIsFull(boolean isfull) throws Exception {
        super.setPath("getByIsFull/" + isfull);
        return super.getEntity();
    }

    /**
     * Retourne un Parking à partir de son ID
     *
     * @param id : ID
     * @return : le Parking à partir de son ID
     * @throws java.lang.Exception
     */
    @Override
    public Parking getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    /**
     * Retourne le nombre de Parking dans la Base de Données
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
     * Retourne tous les Parkings dans la Base de Données
     *
     * @return : Liste de tous les Parkings
     * @throws java.lang.Exception
     */
    @Override
    public List<Parking> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    /**
     * Retourne tous les Parkings dans la Base de Données, paginé
     *
     * @param begin : Début de la Pagination
     * @param count : Nombre à paginer
     * @return : Liste de tous les Parkings
     * @throws java.lang.Exception
     */
    @Override
    public List<Parking> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

}
