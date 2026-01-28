package lml.snir.parkinglogickit.client.interfaces;

/**
 * 
 * @author sylvain
 */
public interface AMSService<T, U> {
    
    public T getService();
    public void ajouter();
    public void add(U u);
    public void modifier();
    public void update(U u);
    public void supprimer();
    public void remove(U u);
    public U getById(Long id);
    
}