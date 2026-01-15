package lml.snir.drivers.lecteurrfid;

import java.util.Observable;
import java.util.Observer;
import lml.snir.drivers.LMLDriverObservable;

/**
 *
 * @author fanou
 */
public class LecteurRfidIDSimuler extends LMLDriverObservable implements Observer {

    private IHM ihm;

    public LecteurRfidIDSimuler() {
        
    }

    public LecteurRfidIDSimuler(String dummy) {
        
    }

    
    @Override
    public void open() throws Exception {
        this.ihm = new IHM(this);
        this.ihm.setVisible(true);
    }

    @Override
    public void close() throws Exception {
        this.ihm.dispose();
    }

    @Override
    public void write(byte[] bytes) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] read() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Observable o, Object arg) {
        this.setChanged();
        this.notifyObservers(arg);
    }

}
