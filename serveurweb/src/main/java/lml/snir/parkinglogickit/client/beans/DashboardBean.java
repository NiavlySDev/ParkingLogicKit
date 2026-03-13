package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class DashboardBean implements Serializable {

    private List<Parking> parkings = new ArrayList<>();
    private List<Places> places = new ArrayList<>();

    private long totalPlaces;
    private long placesLibres;
    private long placesOccupees;
    private long placesHandicap;
    private boolean parkingPlein;

    @PostConstruct
    public void init() {
        try {
            parkings = MetierFactory.getParkingService().getAll();
            places = MetierFactory.getPlacesService().getAll();

            totalPlaces = places.size();
            placesOccupees = places.stream().filter(Places::isIsOccuped).count();
            placesLibres = totalPlaces - placesOccupees;

            if (!parkings.isEmpty()) {
                Parking p = parkings.get(0);
                placesHandicap = p.getTotalHandicap();
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

    public List<Places> getPlaces() {
        return places;
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

    public long getPlacesHandicap() {
        return placesHandicap;
    }

    public boolean isParkingPlein() {
        return parkingPlein;
    }
}
