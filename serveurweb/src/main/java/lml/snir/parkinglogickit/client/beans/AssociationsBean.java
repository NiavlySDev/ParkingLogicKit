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

    /**
     * Exécute le traitement init.
     */
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

    /**
     * Exécute le traitement preparer creation.
     */
    public void preparerCreation() {
        resetForm();
        annulerSelection();
    }

    /**
     * Exécute le traitement annuler selection.
     */
    public void annulerSelection() {
        selectedAssociation = null;
        editBadgeId = null;
        editVehicleId = null;
        supprimerConducteurLie = false;
        supprimerBadgeLie = false;
        supprimerVehiculeLie = false;
    }

    /**
     * Exécute le traitement selectionner association.
     *
     * @param association : paramètre utilisé par la méthode
     */
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

    /**
     * Supprime associations conducteur.
     *
     * @param driver : paramètre utilisé par la méthode
     * @param associateService : paramètre utilisé par la méthode
     */
    private void supprimerAssociationsConducteur(Driver driver, AssociateService associateService) throws Exception {
        for (Associate association : associateService.getAll()) {
            if (association.getDriver() != null
                    && Objects.equals(association.getDriver().getId(), driver.getId())) {
                associateService.remove(association);
            }
        }
    }

    /**
     * Supprime associations badge.
     *
     * @param badge : paramètre utilisé par la méthode
     * @param associateService : paramètre utilisé par la méthode
     */
    private void supprimerAssociationsBadge(Badge badge, AssociateService associateService) throws Exception {
        for (Associate association : associateService.getAll()) {
            if (association.getBadge() != null
                    && Objects.equals(association.getBadge().getId(), badge.getId())) {
                associateService.remove(association);
            }
        }
    }

    /**
     * Supprime associations vehicule.
     *
     * @param vehicle : paramètre utilisé par la méthode
     * @param associateService : paramètre utilisé par la méthode
     */
    private void supprimerAssociationsVehicule(Vehicle vehicle, AssociateService associateService) throws Exception {
        for (Associate association : associateService.getAll()) {
            if (association.getVehicle() != null
                    && Objects.equals(association.getVehicle().getId(), vehicle.getId())) {
                associateService.remove(association);
            }
        }
    }

    /**
     * Exécute le traitement reset form.
     */
    private void resetForm() {
        newDriverId = null;
        newBadgeId = null;
        newVehicleId = null;
    }

    /**
     * Ajoute info.
     *
     * @param message : paramètre utilisé par la méthode
     */
    private void addInfo(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    /**
     * Ajoute error.
     *
     * @param message : paramètre utilisé par la méthode
     */
    private void addError(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    /**
     * Retourne associations.
     *
     * @return List<Associate> : valeur retournée par la méthode
     */
    public List<Associate> getAssociations() {
        return associations;
    }

    /**
     * Retourne drivers.
     *
     * @return List<Driver> : valeur retournée par la méthode
     */
    public List<Driver> getDrivers() {
        return drivers;
    }

    /**
     * Retourne badges.
     *
     * @return List<Badge> : valeur retournée par la méthode
     */
    public List<Badge> getBadges() {
        return badges;
    }

    /**
     * Retourne vehicules.
     *
     * @return List<Vehicle> : valeur retournée par la méthode
     */
    public List<Vehicle> getVehicules() {
        return vehicules;
    }

    /**
     * Retourne badges disponibles creation.
     *
     * @return List<Badge> : valeur retournée par la méthode
     */
    public List<Badge> getBadgesDisponiblesCreation() {
        return filtrerBadgesDisponibles(null);
    }

    /**
     * Retourne vehicules disponibles creation.
     *
     * @return List<Vehicle> : valeur retournée par la méthode
     */
    public List<Vehicle> getVehiculesDisponiblesCreation() {
        return filtrerVehiculesDisponibles(null);
    }

    /**
     * Retourne badges disponibles modification.
     *
     * @return List<Badge> : valeur retournée par la méthode
     */
    public List<Badge> getBadgesDisponiblesModification() {
        return filtrerBadgesDisponibles(editBadgeId);
    }

    /**
     * Retourne vehicules disponibles modification.
     *
     * @return List<Vehicle> : valeur retournée par la méthode
     */
    public List<Vehicle> getVehiculesDisponiblesModification() {
        return filtrerVehiculesDisponibles(editVehicleId);
    }

    /**
     * Exécute le traitement filtrer badges disponibles.
     *
     * @param idBadgeConserve : paramètre utilisé par la méthode
     * @return List<Badge> : valeur retournée par la méthode
     */
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

    /**
     * Exécute le traitement filtrer vehicules disponibles.
     *
     * @param idVehiculeConserve : paramètre utilisé par la méthode
     * @return List<Vehicle> : valeur retournée par la méthode
     */
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

    /**
     * Exécute le traitement badge est associe.
     *
     * @param badgeId : paramètre utilisé par la méthode
     * @return boolean : valeur retournée par la méthode
     */
    private boolean badgeEstAssocie(Long badgeId) {
        for (Associate association : associations) {
            if (association.getBadge() != null
                    && Objects.equals(association.getBadge().getId(), badgeId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exécute le traitement vehicule est associe.
     *
     * @param vehiculeId : paramètre utilisé par la méthode
     * @return boolean : valeur retournée par la méthode
     */
    private boolean vehiculeEstAssocie(Long vehiculeId) {
        for (Associate association : associations) {
            if (association.getVehicle() != null
                    && Objects.equals(association.getVehicle().getId(), vehiculeId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exécute le traitement badge est associe par une autre association.
     *
     * @param badgeId : paramètre utilisé par la méthode
     * @return boolean : valeur retournée par la méthode
     */
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

    /**
     * Exécute le traitement vehicule est associe par une autre association.
     *
     * @param vehiculeId : paramètre utilisé par la méthode
     * @return boolean : valeur retournée par la méthode
     */
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

    /**
     * Retourne selected association.
     *
     * @return Associate : valeur retournée par la méthode
     */
    public Associate getSelectedAssociation() {
        return selectedAssociation;
    }

    /**
     * Modifie selected association.
     *
     * @param selectedAssociation : paramètre utilisé par la méthode
     */
    public void setSelectedAssociation(Associate selectedAssociation) {
        this.selectedAssociation = selectedAssociation;
    }

    /**
     * Retourne new driver id.
     *
     * @return Long : valeur retournée par la méthode
     */
    public Long getNewDriverId() {
        return newDriverId;
    }

    /**
     * Modifie new driver id.
     *
     * @param newDriverId : paramètre utilisé par la méthode
     */
    public void setNewDriverId(Long newDriverId) {
        this.newDriverId = newDriverId;
    }

    /**
     * Retourne new badge id.
     *
     * @return Long : valeur retournée par la méthode
     */
    public Long getNewBadgeId() {
        return newBadgeId;
    }

    /**
     * Modifie new badge id.
     *
     * @param newBadgeId : paramètre utilisé par la méthode
     */
    public void setNewBadgeId(Long newBadgeId) {
        this.newBadgeId = newBadgeId;
    }

    /**
     * Retourne new vehicle id.
     *
     * @return Long : valeur retournée par la méthode
     */
    public Long getNewVehicleId() {
        return newVehicleId;
    }

    /**
     * Modifie new vehicle id.
     *
     * @param newVehicleId : paramètre utilisé par la méthode
     */
    public void setNewVehicleId(Long newVehicleId) {
        this.newVehicleId = newVehicleId;
    }

    /**
     * Retourne edit badge id.
     *
     * @return Long : valeur retournée par la méthode
     */
    public Long getEditBadgeId() {
        return editBadgeId;
    }

    /**
     * Modifie edit badge id.
     *
     * @param editBadgeId : paramètre utilisé par la méthode
     */
    public void setEditBadgeId(Long editBadgeId) {
        this.editBadgeId = editBadgeId;
    }

    /**
     * Retourne edit vehicle id.
     *
     * @return Long : valeur retournée par la méthode
     */
    public Long getEditVehicleId() {
        return editVehicleId;
    }

    /**
     * Modifie edit vehicle id.
     *
     * @param editVehicleId : paramètre utilisé par la méthode
     */
    public void setEditVehicleId(Long editVehicleId) {
        this.editVehicleId = editVehicleId;
    }

    /**
     * Indique si supprimer conducteur lie.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isSupprimerConducteurLie() {
        return supprimerConducteurLie;
    }

    /**
     * Modifie supprimer conducteur lie.
     *
     * @param supprimerConducteurLie : paramètre utilisé par la méthode
     */
    public void setSupprimerConducteurLie(boolean supprimerConducteurLie) {
        this.supprimerConducteurLie = supprimerConducteurLie;
    }

    /**
     * Indique si supprimer badge lie.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isSupprimerBadgeLie() {
        return supprimerBadgeLie;
    }

    /**
     * Modifie supprimer badge lie.
     *
     * @param supprimerBadgeLie : paramètre utilisé par la méthode
     */
    public void setSupprimerBadgeLie(boolean supprimerBadgeLie) {
        this.supprimerBadgeLie = supprimerBadgeLie;
    }

    /**
     * Indique si supprimer vehicule lie.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isSupprimerVehiculeLie() {
        return supprimerVehiculeLie;
    }

    /**
     * Modifie supprimer vehicule lie.
     *
     * @param supprimerVehiculeLie : paramètre utilisé par la méthode
     */
    public void setSupprimerVehiculeLie(boolean supprimerVehiculeLie) {
        this.supprimerVehiculeLie = supprimerVehiculeLie;
    }
}
