package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 * Bean de gestion des associations entre conducteur, badge et véhicule. Cette
 * page a été séparée de la gestion des badges pour rendre l'application plus
 * claire pendant l'utilisation et la présentation du projet.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class AssociationsBean implements Serializable {

    private List<Associate> associations = new ArrayList<>();
    private List<Driver> drivers = new ArrayList<>();
    private List<Badge> badges = new ArrayList<>();
    private List<Vehicle> vehicules = new ArrayList<>();

    private Associate selectedAssociation;
    private Long newDriverId;
    private Long newBadgeId;
    private Long newVehicleId;

    @PostConstruct
    public void init() {
        charger();
    }

    /**
     * Recharge les listes nécessaires au tableau et au formulaire de création.
     */
    public void charger() {
        try {
            associations = MetierFactory.getAssociateService().getAll();
            drivers = MetierFactory.getDriverService().getAll();
            badges = MetierFactory.getBadgeService().getAll();
            vehicules = MetierFactory.getVehicleService().getAll();
        } catch (Exception e) {
            addError("Erreur lors du chargement des associations : " + e.getMessage());
        }
    }

    /**
     * Crée une association complète entre un conducteur, un badge et un
     * véhicule.
     */
    public void creerAssociation() {
        if (newDriverId == null || newBadgeId == null || newVehicleId == null) {
            addError("Tous les champs de l'association sont obligatoires.");
            return;
        }

        try {
            Associate association = new Associate();
            association.setDriver(MetierFactory.getDriverService().getById(newDriverId));
            association.setBadge(MetierFactory.getBadgeService().getById(newBadgeId));
            association.setVehicle(MetierFactory.getVehicleService().getById(newVehicleId));

            MetierFactory.getAssociateService().add(association);
            addInfo("Association créée.");
            resetForm();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la création de l'association : " + e.getMessage());
        }
    }

    /**
     * Supprime l'association sélectionnée dans le tableau.
     */
    public void supprimerAssociation() {
        if (selectedAssociation == null) {
            return;
        }

        try {
            MetierFactory.getAssociateService().remove(selectedAssociation);
            addInfo("Association supprimée.");
            annulerSelection();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la suppression de l'association : " + e.getMessage());
        }
    }

    public void preparerCreation() {
        resetForm();
        annulerSelection();
    }

    public void annulerSelection() {
        selectedAssociation = null;
    }

    public void selectionnerAssociation(Associate association) {
        this.selectedAssociation = association;
    }

    private void resetForm() {
        newDriverId = null;
        newBadgeId = null;
        newVehicleId = null;
    }

    private void addInfo(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private void addError(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public List<Associate> getAssociations() {
        return associations;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public List<Vehicle> getVehicules() {
        return vehicules;
    }

    public Associate getSelectedAssociation() {
        return selectedAssociation;
    }

    public void setSelectedAssociation(Associate selectedAssociation) {
        this.selectedAssociation = selectedAssociation;
    }

    public Long getNewDriverId() {
        return newDriverId;
    }

    public void setNewDriverId(Long newDriverId) {
        this.newDriverId = newDriverId;
    }

    public Long getNewBadgeId() {
        return newBadgeId;
    }

    public void setNewBadgeId(Long newBadgeId) {
        this.newBadgeId = newBadgeId;
    }

    public Long getNewVehicleId() {
        return newVehicleId;
    }

    public void setNewVehicleId(Long newVehicleId) {
        this.newVehicleId = newVehicleId;
    }
}
