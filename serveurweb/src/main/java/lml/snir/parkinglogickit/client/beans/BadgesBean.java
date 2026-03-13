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
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class BadgesBean implements Serializable {

    private List<Badge> badges = new ArrayList<>();
    private List<Associate> associates = new ArrayList<>();
    private List<Driver> drivers = new ArrayList<>();
    private List<Vehicle> vehicules = new ArrayList<>();

    private Badge selectedBadge;
    private Associate selectedAssociate;

    private String newBadgeContent;

    private Long newAssocDriverId;
    private Long newAssocBadgeId;
    private Long newAssocVehicleId;

    @PostConstruct
    public void init() {
        charger();
    }

    public void charger() {
        try {
            badges = MetierFactory.getBadgeService().getAll();
            associates = MetierFactory.getAssociateService().getAll();
            drivers = MetierFactory.getDriverService().getAll();
            vehicules = MetierFactory.getVehicleService().getAll();
        } catch (Exception e) {
            addError("Erreur chargement badges : " + e.getMessage());
        }
    }

    public void creerBadge() {
        try {
            Badge b = new Badge();
            b.setContent(newBadgeContent);
            MetierFactory.getBadgeService().add(b);
            addInfo("Badge créé.");
            newBadgeContent = null;
            charger();
        } catch (Exception e) {
            addError("Erreur création badge : " + e.getMessage());
        }
    }

    public void supprimerBadge() {
        if (selectedBadge == null) {
            return;
        }
        try {
            MetierFactory.getBadgeService().remove(selectedBadge);
            addInfo("Badge supprimé.");
            selectedBadge = null;
            charger();
        } catch (Exception e) {
            addError("Erreur suppression badge : " + e.getMessage());
        }
    }

    public void creerAssociation() {
        try {
            if (newAssocDriverId == null || newAssocBadgeId == null || newAssocVehicleId == null) {
                addError("Tous les champs de l'association sont obligatoires.");
                return;
            }
            Associate a = new Associate();
            a.setDriver(MetierFactory.getDriverService().getById(newAssocDriverId));
            a.setBadge(MetierFactory.getBadgeService().getById(newAssocBadgeId));
            a.setVehicle(MetierFactory.getVehicleService().getById(newAssocVehicleId));
            MetierFactory.getAssociateService().add(a);
            addInfo("Association créée.");
            newAssocDriverId = null;
            newAssocBadgeId = null;
            newAssocVehicleId = null;
            charger();
        } catch (Exception e) {
            addError("Erreur création association : " + e.getMessage());
        }
    }

    public void supprimerAssociation() {
        if (selectedAssociate == null) {
            return;
        }
        try {
            MetierFactory.getAssociateService().remove(selectedAssociate);
            addInfo("Association supprimée.");
            selectedAssociate = null;
            charger();
        } catch (Exception e) {
            addError("Erreur suppression association : " + e.getMessage());
        }
    }

    public void selectionnerBadge(Badge b) {
        this.selectedBadge = b;
    }

    public void selectionnerAssociation(Associate a) {
        this.selectedAssociate = a;
    }

    private void addInfo(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    private void addError(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public List<Associate> getAssociates() {
        return associates;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public List<Vehicle> getVehicules() {
        return vehicules;
    }

    public Badge getSelectedBadge() {
        return selectedBadge;
    }

    public void setSelectedBadge(Badge b) {
        this.selectedBadge = b;
    }

    public Associate getSelectedAssociate() {
        return selectedAssociate;
    }

    public void setSelectedAssociate(Associate a) {
        this.selectedAssociate = a;
    }

    public String getNewBadgeContent() {
        return newBadgeContent;
    }

    public void setNewBadgeContent(String v) {
        this.newBadgeContent = v;
    }

    public Long getNewAssocDriverId() {
        return newAssocDriverId;
    }

    public void setNewAssocDriverId(Long v) {
        this.newAssocDriverId = v;
    }

    public Long getNewAssocBadgeId() {
        return newAssocBadgeId;
    }

    public void setNewAssocBadgeId(Long v) {
        this.newAssocBadgeId = v;
    }

    public Long getNewAssocVehicleId() {
        return newAssocVehicleId;
    }

    public void setNewAssocVehicleId(Long v) {
        this.newAssocVehicleId = v;
    }
}
