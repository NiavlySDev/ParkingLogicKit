package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.transactionel.AssociateService;
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
    private Long editBadgeId;
    private Long editVehicleId;
    private boolean supprimerConducteurLie;
    private boolean supprimerBadgeLie;
    private boolean supprimerVehiculeLie;

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
        if (badgeEstAssocie(newBadgeId) || vehiculeEstAssocie(newVehicleId)) {
            addError("Ce badge ou ce véhicule est déjà associé.");
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
     * Modifie uniquement le badge et le véhicule de l'association sélectionnée.
     */
    public void modifierAssociation() {
        if (selectedAssociation == null) {
            return;
        }
        if (editBadgeId == null || editVehicleId == null) {
            addError("Le badge et le véhicule sont obligatoires.");
            return;
        }
        if (badgeEstAssocieParUneAutreAssociation(editBadgeId)
                || vehiculeEstAssocieParUneAutreAssociation(editVehicleId)) {
            addError("Ce badge ou ce véhicule est déjà associé.");
            return;
        }

        try {
            selectedAssociation.setBadge(MetierFactory.getBadgeService().getById(editBadgeId));
            selectedAssociation.setVehicle(MetierFactory.getVehicleService().getById(editVehicleId));
            MetierFactory.getAssociateService().update(selectedAssociation);
            addInfo("Association mise à jour.");
            annulerSelection();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la modification de l'association : " + e.getMessage());
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
            AssociateService associateService = MetierFactory.getAssociateService();
            Driver driver = selectedAssociation.getDriver();
            Badge badge = selectedAssociation.getBadge();
            Vehicle vehicle = selectedAssociation.getVehicle();

            associateService.remove(selectedAssociation);

            if (supprimerConducteurLie && driver != null) {
                supprimerAssociationsConducteur(driver, associateService);
                MetierFactory.getDriverService().remove(driver);
            }
            if (supprimerBadgeLie && badge != null) {
                supprimerAssociationsBadge(badge, associateService);
                MetierFactory.getBadgeService().remove(badge);
            }
            if (supprimerVehiculeLie && vehicle != null) {
                supprimerAssociationsVehicule(vehicle, associateService);
                MetierFactory.getVehicleService().remove(vehicle);
            }

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
        editBadgeId = null;
        editVehicleId = null;
        supprimerConducteurLie = false;
        supprimerBadgeLie = false;
        supprimerVehiculeLie = false;
    }

    public void selectionnerAssociation(Associate association) {
        this.selectedAssociation = association;
        this.editBadgeId = association != null && association.getBadge() != null
                ? association.getBadge().getId() : null;
        this.editVehicleId = association != null && association.getVehicle() != null
                ? association.getVehicle().getId() : null;
        supprimerConducteurLie = false;
        supprimerBadgeLie = false;
        supprimerVehiculeLie = false;
    }

    private void supprimerAssociationsConducteur(Driver driver, AssociateService associateService) throws Exception {
        for (Associate association : associateService.getAll()) {
            if (association.getDriver() != null
                    && Objects.equals(association.getDriver().getId(), driver.getId())) {
                associateService.remove(association);
            }
        }
    }

    private void supprimerAssociationsBadge(Badge badge, AssociateService associateService) throws Exception {
        for (Associate association : associateService.getAll()) {
            if (association.getBadge() != null
                    && Objects.equals(association.getBadge().getId(), badge.getId())) {
                associateService.remove(association);
            }
        }
    }

    private void supprimerAssociationsVehicule(Vehicle vehicle, AssociateService associateService) throws Exception {
        for (Associate association : associateService.getAll()) {
            if (association.getVehicle() != null
                    && Objects.equals(association.getVehicle().getId(), vehicle.getId())) {
                associateService.remove(association);
            }
        }
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

    public List<Badge> getBadgesDisponiblesCreation() {
        return filtrerBadgesDisponibles(null);
    }

    public List<Vehicle> getVehiculesDisponiblesCreation() {
        return filtrerVehiculesDisponibles(null);
    }

    public List<Badge> getBadgesDisponiblesModification() {
        return filtrerBadgesDisponibles(editBadgeId);
    }

    public List<Vehicle> getVehiculesDisponiblesModification() {
        return filtrerVehiculesDisponibles(editVehicleId);
    }

    private List<Badge> filtrerBadgesDisponibles(Long idBadgeConserve) {
        List<Badge> result = new ArrayList<>();
        for (Badge badge : badges) {
            if (badge.getId() == null) {
                continue;
            }
            if (Objects.equals(badge.getId(), idBadgeConserve) || !badgeEstAssocie(badge.getId())) {
                result.add(badge);
            }
        }
        return result;
    }

    private List<Vehicle> filtrerVehiculesDisponibles(Long idVehiculeConserve) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : vehicules) {
            if (vehicle.getId() == null) {
                continue;
            }
            if (Objects.equals(vehicle.getId(), idVehiculeConserve) || !vehiculeEstAssocie(vehicle.getId())) {
                result.add(vehicle);
            }
        }
        return result;
    }

    private boolean badgeEstAssocie(Long badgeId) {
        for (Associate association : associations) {
            if (association.getBadge() != null
                    && Objects.equals(association.getBadge().getId(), badgeId)) {
                return true;
            }
        }
        return false;
    }

    private boolean vehiculeEstAssocie(Long vehiculeId) {
        for (Associate association : associations) {
            if (association.getVehicle() != null
                    && Objects.equals(association.getVehicle().getId(), vehiculeId)) {
                return true;
            }
        }
        return false;
    }

    private boolean badgeEstAssocieParUneAutreAssociation(Long badgeId) {
        for (Associate association : associations) {
            if (selectedAssociation != null
                    && Objects.equals(association.getId(), selectedAssociation.getId())) {
                continue;
            }
            if (association.getBadge() != null
                    && Objects.equals(association.getBadge().getId(), badgeId)) {
                return true;
            }
        }
        return false;
    }

    private boolean vehiculeEstAssocieParUneAutreAssociation(Long vehiculeId) {
        for (Associate association : associations) {
            if (selectedAssociation != null
                    && Objects.equals(association.getId(), selectedAssociation.getId())) {
                continue;
            }
            if (association.getVehicle() != null
                    && Objects.equals(association.getVehicle().getId(), vehiculeId)) {
                return true;
            }
        }
        return false;
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

    public Long getEditBadgeId() {
        return editBadgeId;
    }

    public void setEditBadgeId(Long editBadgeId) {
        this.editBadgeId = editBadgeId;
    }

    public Long getEditVehicleId() {
        return editVehicleId;
    }

    public void setEditVehicleId(Long editVehicleId) {
        this.editVehicleId = editVehicleId;
    }

    public boolean isSupprimerConducteurLie() {
        return supprimerConducteurLie;
    }

    public void setSupprimerConducteurLie(boolean supprimerConducteurLie) {
        this.supprimerConducteurLie = supprimerConducteurLie;
    }

    public boolean isSupprimerBadgeLie() {
        return supprimerBadgeLie;
    }

    public void setSupprimerBadgeLie(boolean supprimerBadgeLie) {
        this.supprimerBadgeLie = supprimerBadgeLie;
    }

    public boolean isSupprimerVehiculeLie() {
        return supprimerVehiculeLie;
    }

    public void setSupprimerVehiculeLie(boolean supprimerVehiculeLie) {
        this.supprimerVehiculeLie = supprimerVehiculeLie;
    }
}
