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
 * Bean du tableau de bord. Il récupère les parkings et prépare les données
 * nécessaires à l'affichage des cartes de disponibilité.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class DashboardBean implements Serializable {

    private List<Parking> parkings = new ArrayList<>();

    private List<ParkingCard> parkingsCard = new ArrayList<>();

    private long totalPlaces;
    private long placesLibres;
    private long placesOccupees;
    private boolean parkingPlein;

    @PostConstruct
    public void init() {
        charger();
    }

    /**
     * Recharge les parkings et recalcule les totaux affichés dans la page.
     */
    public void charger() {
        try {
            parkings = MetierFactory.getParkingService().getAll();
            totalPlaces = 0;
            placesLibres = 0;
            placesOccupees = 0;
            parkingPlein = false;
            parkingsCard.clear();

            for (Parking parking : parkings) {
                totalPlaces += parking.getTotalPlace();
                placesLibres += parking.getPlaceCount();
                parkingsCard.add(new ParkingCard(
                        "Parking " + parking.getId(),
                        parking.getTotalPlace(),
                        parking.getPlaceCount()
                ));
            }

            placesOccupees = totalPlaces - placesLibres;
            if (!parkings.isEmpty()) {
                parkingPlein = parkings.stream().allMatch(Parking::isIsFull);
            }

        } catch (Exception e) {
            System.err.println("DashboardBean.init() error: " + e.getMessage());
        }
    }

    public void refresh() {
        charger();
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

    public List<ParkingCard> getParkingsCard() {
        return parkingsCard;
    }
}
