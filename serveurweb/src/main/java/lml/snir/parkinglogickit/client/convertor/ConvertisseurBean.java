package lml.snir.parkinglogickit.client.convertor;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author fanou
 */
@Named
@ViewScoped
public class ConvertisseurBean implements Serializable {
    private final double TAUX = 6.55957;
    private double euro;
    private double franc;

    /**
     * @return the euro
     */
    public double getEuro() {
        return euro;
    }

    /**
     * @param euro the euro to set
     */
    public void setEuro(double euro) {
        this.euro = euro;
    }

    /**
     * @return the franc
     */
    public double getFranc() {
        return franc;
    }

    /**
     * @param franc the franc to set
     */
    public void setFranc(double franc) {
        this.concertFrancToEuro();
    }
    
    public void concertFrancToEuro() {
        this.franc = this.euro * this.TAUX;
    }
    
}
