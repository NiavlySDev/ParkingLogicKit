package lml.snir.drivers.lecteurrfid;

import java.util.Observable;
import java.util.Observer;
import lml.snir.drivers.LMLDriverObservable;

/**
 * Unit test for simple App.
 */
public class AppTest implements Observer {
    private final LMLDriverObservable lecteur;
    
    public static void main(String[] args) throws Exception {
        AppTest test = new AppTest();
        Thread.sleep(10000);
        test.close();
    }
    
    public AppTest() throws Exception {
        this.lecteur = new LecteurRFIDDriverID12Impl("/dev/ttyUSB0");
        this.lecteur.addObserver(this);
        this.lecteur.open();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
    }

    private void close() throws Exception {
        this.lecteur.close();
    }
   
}
