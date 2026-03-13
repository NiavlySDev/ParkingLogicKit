package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.entity.VehicleType;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class VehiculesBean implements Serializable {

    private List<Vehicle> vehicules = new ArrayList<>();
    private Vehicle selectedVehicle;

    private String newBrand;
    private String newNumberPlate;
    private VehicleType newVehicleType = VehicleType.Voiture;

    @PostConstruct
    public void init() {
        charger();
    }

    public void charger() {
        try {
            vehicules = MetierFactory.getVehicleService().getAll();
        } catch (Exception e) {
            addError("Erreur chargement véhicules : " + e.getMessage());
        }
    }

    public VehicleType[] getVehicleTypes() {
        return VehicleType.values();
    }

    public void creer() {
        try {
            Vehicle v = new Vehicle();
            v.setBrand(newBrand);
            v.setNumberPlate(newNumberPlate);
            v.setType(newVehicleType);
            MetierFactory.getVehicleService().add(v);
            addInfo("Véhicule " + newNumberPlate + " créé.");
            resetForm();
            charger();
        } catch (Exception e) {
            addError("Erreur création véhicule : " + e.getMessage());
        }
    }

    public void modifier() {
        if (selectedVehicle == null) {
            return;
        }
        try {
            MetierFactory.getVehicleService().update(selectedVehicle);
            addInfo("Véhicule mis à jour.");
            charger();
        } catch (Exception e) {
            addError("Erreur modification : " + e.getMessage());
        }
    }

    public void supprimer() {
        if (selectedVehicle == null) {
            return;
        }
        try {
            MetierFactory.getVehicleService().remove(selectedVehicle);
            addInfo("Véhicule supprimé.");
            selectedVehicle = null;
            charger();
        } catch (Exception e) {
            addError("Erreur suppression : " + e.getMessage());
        }
    }

    public void selectionner(Vehicle v) {
        this.selectedVehicle = v;
    }

    private void resetForm() {
        newBrand = null;
        newNumberPlate = null;
        newVehicleType = VehicleType.Voiture;
    }

    private void addInfo(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    private void addError(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    public List<Vehicle> getVehicules() {
        return vehicules;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(Vehicle v) {
        this.selectedVehicle = v;
    }

    public String getNewBrand() {
        return newBrand;
    }

    public void setNewBrand(String v) {
        this.newBrand = v;
    }

    public String getNewNumberPlate() {
        return newNumberPlate;
    }

    public void setNewNumberPlate(String v) {
        this.newNumberPlate = v;
    }

    public VehicleType getNewVehicleType() {
        return newVehicleType;
    }

    public void setNewVehicleType(VehicleType v) {
        this.newVehicleType = v;
    }
}
