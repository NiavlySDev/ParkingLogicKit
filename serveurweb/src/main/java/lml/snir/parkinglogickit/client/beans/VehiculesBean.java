package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.entity.VehicleType;
import lml.snir.parkinglogickit.metier.transactionel.AssociateService;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 * Bean de gestion des véhicules. Il fournit les actions d'administration
 * classiques pour les véhicules : consultation, création, modification et
 * suppression.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class VehiculesBean implements Serializable {

    private List<Vehicle> vehicules = new ArrayList<>();
    private Vehicle selectedVehicle;
    private List<Associate> associationsSuppression = new ArrayList<>();
    private List<Driver> conducteursSuppression = new ArrayList<>();
    private List<Badge> badgesSuppression = new ArrayList<>();
    private boolean supprimerConducteursLies;
    private boolean supprimerBadgesLies;

    private String newBrand;
    private String newNumberPlate;
    private VehicleType newVehicleType = VehicleType.Voiture;

    @PostConstruct
    public void init() {
        charger();
    }

    /**
     * Recharge les véhicules depuis la couche métier.
     */
    public void charger() {
        try {
            vehicules = MetierFactory.getVehicleService().getAll();
        } catch (Exception e) {
            addError("Erreur lors du chargement des véhicules : " + e.getMessage());
        }
    }

    public VehicleType[] getVehicleTypes() {
        return VehicleType.values();
    }

    /**
     * Crée un véhicule à partir des informations saisies dans le formulaire.
     */
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
            addError("Erreur lors de la création du véhicule : " + e.getMessage());
        }
    }

    /**
     * Enregistre les modifications du véhicule sélectionné.
     */
    public void modifier() {
        if (selectedVehicle == null) {
            return;
        }
        try {
            MetierFactory.getVehicleService().update(selectedVehicle);
            addInfo("Véhicule mis à jour.");
            annulerSelection();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Supprime le véhicule sélectionné dans le tableau.
     */
    public void supprimer() {
        if (selectedVehicle == null) {
            return;
        }
        try {
            AssociateService associateService = MetierFactory.getAssociateService();
            for (Associate association : associationsSuppression) {
                associateService.remove(association);
            }
            if (supprimerConducteursLies) {
                for (Driver driver : conducteursSuppression) {
                    supprimerAssociationsConducteur(driver, associateService);
                    MetierFactory.getDriverService().remove(driver);
                }
            }
            if (supprimerBadgesLies) {
                for (Badge badge : badgesSuppression) {
                    supprimerAssociationsBadge(badge, associateService);
                    MetierFactory.getBadgeService().remove(badge);
                }
            }
            MetierFactory.getVehicleService().remove(selectedVehicle);
            addInfo("Véhicule supprimé.");
            annulerSelection();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public void preparerCreation() {
        resetForm();
        annulerSelection();
    }

    public void annulerSelection() {
        selectedVehicle = null;
        associationsSuppression = new ArrayList<>();
        conducteursSuppression = new ArrayList<>();
        badgesSuppression = new ArrayList<>();
        supprimerConducteursLies = false;
        supprimerBadgesLies = false;
    }

    public void selectionner(Vehicle v) {
        this.selectedVehicle = v;
        this.supprimerConducteursLies = false;
        this.supprimerBadgesLies = false;
        try {
            associationsSuppression = trouverAssociationsVehicule(v, MetierFactory.getAssociateService());
            conducteursSuppression = extraireConducteurs(associationsSuppression);
            badgesSuppression = extraireBadges(associationsSuppression);
        } catch (Exception e) {
            associationsSuppression = new ArrayList<>();
            conducteursSuppression = new ArrayList<>();
            badgesSuppression = new ArrayList<>();
            addError("Erreur lors de la préparation de la suppression : " + e.getMessage());
        }
    }

    private List<Associate> trouverAssociationsVehicule(Vehicle vehicle, AssociateService associateService) throws Exception {
        List<Associate> result = new ArrayList<>();
        for (Associate association : associateService.getAll()) {
            if (association.getVehicle() != null
                    && Objects.equals(association.getVehicle().getId(), vehicle.getId())) {
                result.add(association);
            }
        }
        return result;
    }

    private List<Driver> extraireConducteurs(List<Associate> associations) {
        Map<Long, Driver> conducteurs = new LinkedHashMap<>();
        for (Associate association : associations) {
            Driver driver = association.getDriver();
            if (driver != null) {
                conducteurs.putIfAbsent(driver.getId(), driver);
            }
        }
        return new ArrayList<>(conducteurs.values());
    }

    private List<Badge> extraireBadges(List<Associate> associations) {
        Map<Long, Badge> badges = new LinkedHashMap<>();
        for (Associate association : associations) {
            Badge badge = association.getBadge();
            if (badge != null && badge.getId() != null) {
                badges.putIfAbsent(badge.getId(), badge);
            }
        }
        return new ArrayList<>(badges.values());
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

    public List<Associate> getAssociationsSuppression() {
        return associationsSuppression;
    }

    public List<Driver> getConducteursSuppression() {
        return conducteursSuppression;
    }

    public List<Badge> getBadgesSuppression() {
        return badgesSuppression;
    }

    public boolean isSupprimerConducteursLies() {
        return supprimerConducteursLies;
    }

    public void setSupprimerConducteursLies(boolean supprimerConducteursLies) {
        this.supprimerConducteursLies = supprimerConducteursLies;
    }

    public boolean isSupprimerBadgesLies() {
        return supprimerBadgesLies;
    }

    public void setSupprimerBadgesLies(boolean supprimerBadgesLies) {
        this.supprimerBadgesLies = supprimerBadgesLies;
    }
}
