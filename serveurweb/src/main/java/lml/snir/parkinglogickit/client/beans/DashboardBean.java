package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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
    private Parking selectedParking;
    private Integer editPlacesPrises;
    private Integer editTotalPlaces;

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

    /**
     * Prépare les valeurs modifiables du parking choisi.
     *
     * @param parking parking sélectionné depuis le tableau
     */
    public void preparerModification(Parking parking) {
        selectedParking = parking;
        editTotalPlaces = parking.getTotalPlace();
        editPlacesPrises = Math.max(0, parking.getTotalPlace() - parking.getPlaceCount());
    }

    /**
     * Met à jour l'occupation du parking depuis les valeurs saisies côté web.
     */
    public void modifierOccupation() {
        if (selectedParking == null) {
            return;
        }
        if (!valeursOccupationValides()) {
            return;
        }

        try {
            int placesLibresParking = editTotalPlaces - editPlacesPrises;
            selectedParking.setTotalPlace(editTotalPlaces);
            selectedParking.setPlaceCount(placesLibresParking);
            selectedParking.setIsFull(placesLibresParking == 0);
            MetierFactory.getParkingService().update(selectedParking);
            addInfo("Occupation du parking mise à jour.");
            annulerModification();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la mise à jour du parking : " + e.getMessage());
        }
    }

    public void annulerModification() {
        selectedParking = null;
        editPlacesPrises = null;
        editTotalPlaces = null;
    }

    private boolean valeursOccupationValides() {
        if (editTotalPlaces == null || editPlacesPrises == null) {
            addError("Le nombre de places prises et le total sont obligatoires.");
            return false;
        }
        if (editTotalPlaces < 0 || editPlacesPrises < 0) {
            addError("Les nombres de places doivent être positifs.");
            return false;
        }
        if (editPlacesPrises > editTotalPlaces) {
            addError("Les places prises ne peuvent pas dépasser le nombre total de places.");
            return false;
        }
        return true;
    }

    private void addInfo(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private void addError(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.validationFailed();
        context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
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

    public Parking getSelectedParking() {
        return selectedParking;
    }

    public void setSelectedParking(Parking selectedParking) {
        this.selectedParking = selectedParking;
    }

    public Integer getEditPlacesPrises() {
        return editPlacesPrises;
    }

    public void setEditPlacesPrises(Integer editPlacesPrises) {
        this.editPlacesPrises = editPlacesPrises;
    }

    public Integer getEditTotalPlaces() {
        return editTotalPlaces;
    }

    public void setEditTotalPlaces(Integer editTotalPlaces) {
        this.editTotalPlaces = editTotalPlaces;
    }
}
