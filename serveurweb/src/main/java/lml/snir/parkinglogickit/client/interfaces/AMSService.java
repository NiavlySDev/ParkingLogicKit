package lml.snir.parkinglogickit.client.interfaces;

/**
 * @author Sylvain Crocquevieille
 */
public interface AMSService<T, U> {

    /**
     * Retourne service.
     *
     * @return T : valeur retournée par la méthode
     */
    public T getService();

    /**
     * Exécute le traitement ajouter.
     */
    public void ajouter();

    /**
     * Exécute le traitement add.
     *
     * @param u : paramètre utilisé par la méthode
     */
    public void add(U u);

    /**
     * Exécute le traitement modifier.
     */
    public void modifier();

    /**
     * Exécute le traitement update.
     *
     * @param u : paramètre utilisé par la méthode
     */
    public void update(U u);

    /**
     * Exécute le traitement supprimer.
     */
    public void supprimer();

    /**
     * Exécute le traitement remove.
     *
     * @param u : paramètre utilisé par la méthode
     */
    public void remove(U u);

    /**
     * Retourne by id.
     *
     * @param id : paramètre utilisé par la méthode
     * @return U : valeur retournée par la méthode
     */
    public U getById(Long id);

}
