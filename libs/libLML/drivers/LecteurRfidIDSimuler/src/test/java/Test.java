
import java.util.Observable;
import java.util.Observer;
import lml.snir.drivers.LMLDriverObservable;
import lml.snir.drivers.lecteurrfid.LecteurRfidIDSimuler;


public class Test implements Observer {

    public static void main(String[] args) throws Exception {
        new Test();
    }
    
    public Test() throws Exception {
        LMLDriverObservable driver = new LecteurRfidIDSimuler();
        driver.addObserver(this);
        driver.open();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.err.println(arg);
    }
}
