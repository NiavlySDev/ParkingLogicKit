package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class DashboardBean implements Serializable {

    private List<Parking> parkings = new ArrayList<>();

    private long totalPlaces;
    private long placesLibres;
    private long placesOccupees;
    private boolean parkingPlein;

    @PostConstruct
    public void init() {
        try {
            parkings = MetierFactory.getParkingService().getAll();

            totalPlaces = parkings.size();
            placesOccupees = parkings.stream().filter(Parking::isIsFull).count();
            placesLibres = totalPlaces - placesOccupees;

            if (!parkings.isEmpty()) {
                Parking p = parkings.get(0);
                parkingPlein = p.isIsFull();
            }
        } catch (Exception e) {
            System.err.println("DashboardBean.init() error: " + e.getMessage());
        }
    }

    public void refresh() {
        init();
    }

    public List<Parking> getParkings() {
        return parkings;
    }


    public long getTotalPlaces() {
        return totalPlaces;
    }

    public long getPlacesLibres() {
        return placesLibres;
    }

    public long getPlacesOccupees() {
        return placesOccupees;
    }

    public boolean isParkingPlein() {
        return parkingPlein;
    }
}
