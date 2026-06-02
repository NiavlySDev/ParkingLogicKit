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
import lml.snir.parkinglogickit.metier.transactionel.AssociateService;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 * Bean de gestion des badges RFID. Cette classe alimente la page
 * d'administration des badges et regroupe les actions simples : afficher, créer
 * et supprimer un badge.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class BadgesBean implements Serializable {

    private List<Badge> badges = new ArrayList<>();
    private Badge selectedBadge;
    private List<Associate> associationsSuppression = new ArrayList<>();
    private List<Driver> conducteursSuppression = new ArrayList<>();
    private List<Vehicle> vehiculesSuppression = new ArrayList<>();
    private String newBadgeContent;
    private boolean supprimerConducteursLies;
    private boolean supprimerVehiculesLies;

    @PostConstruct
    public void init() {
        charger();
    }

    /**
     * Recharge la liste des badges depuis la couche métier.
     */
    public void charger() {
        try {
            badges = MetierFactory.getBadgeService().getAll();
        } catch (Exception e) {
            addError("Erreur lors du chargement des badges : " + e.getMessage());
        }
    }

    /**
     * Crée un badge avec le contenu RFID saisi dans la fenêtre de création.
     */
    public void creerBadge() {
        try {
            Badge badge = new Badge();
            badge.setContent(newBadgeContent);
            MetierFactory.getBadgeService().add(badge);
            addInfo("Badge créé.");
            preparerCreation();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la création du badge : " + e.getMessage());
        }
    }

    /**
     * Supprime le badge sélectionné dans le tableau.
     */
    public void supprimerBadge() {
        if (selectedBadge == null) {
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
            if (supprimerVehiculesLies) {
                for (Vehicle vehicle : vehiculesSuppression) {
                    supprimerAssociationsVehicule(vehicle, associateService);
                    MetierFactory.getVehicleService().remove(vehicle);
                }
            }
            MetierFactory.getBadgeService().remove(selectedBadge);
            addInfo("Badge supprimé.");
            annulerSelection();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la suppression du badge : " + e.getMessage());
        }
    }

    public void preparerCreation() {
        newBadgeContent = null;
        annulerSelection();
    }

    public void annulerSelection() {
        selectedBadge = null;
        associationsSuppression = new ArrayList<>();
        conducteursSuppression = new ArrayList<>();
        vehiculesSuppression = new ArrayList<>();
        supprimerConducteursLies = false;
        supprimerVehiculesLies = false;
    }

    public void selectionnerBadge(Badge badge) {
        this.selectedBadge = badge;
        this.supprimerConducteursLies = false;
        this.supprimerVehiculesLies = false;
        try {
            associationsSuppression = trouverAssociationsBadge(badge, MetierFactory.getAssociateService());
            conducteursSuppression = extraireConducteurs(associationsSuppression);
            vehiculesSuppression = extraireVehicules(associationsSuppression);
        } catch (Exception e) {
            associationsSuppression = new ArrayList<>();
            conducteursSuppression = new ArrayList<>();
            vehiculesSuppression = new ArrayList<>();
            addError("Erreur lors de la préparation de la suppression : " + e.getMessage());
        }
    }

    private List<Associate> trouverAssociationsBadge(Badge badge, AssociateService associateService) throws Exception {
        List<Associate> result = new ArrayList<>();
        for (Associate association : associateService.getAll()) {
            if (association.getBadge() != null
                    && Objects.equals(association.getBadge().getId(), badge.getId())) {
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

    private List<Vehicle> extraireVehicules(List<Associate> associations) {
        Map<Long, Vehicle> vehicules = new LinkedHashMap<>();
        for (Associate association : associations) {
            Vehicle vehicle = association.getVehicle();
            if (vehicle != null && vehicle.getId() != null) {
                vehicules.putIfAbsent(vehicle.getId(), vehicle);
            }
        }
        return new ArrayList<>(vehicules.values());
    }

    private void supprimerAssociationsConducteur(Driver driver, AssociateService associateService) throws Exception {
        for (Associate association : associateService.getAll()) {
            if (association.getDriver() != null
                    && Objects.equals(association.getDriver().getId(), driver.getId())) {
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

    private void addInfo(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private void addError(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public Badge getSelectedBadge() {
        return selectedBadge;
    }

    public void setSelectedBadge(Badge selectedBadge) {
        this.selectedBadge = selectedBadge;
    }

    public String getNewBadgeContent() {
        return newBadgeContent;
    }

    public void setNewBadgeContent(String newBadgeContent) {
        this.newBadgeContent = newBadgeContent;
    }

    public List<Associate> getAssociationsSuppression() {
        return associationsSuppression;
    }

    public List<Driver> getConducteursSuppression() {
        return conducteursSuppression;
    }

    public List<Vehicle> getVehiculesSuppression() {
        return vehiculesSuppression;
    }

    public boolean isSupprimerConducteursLies() {
        return supprimerConducteursLies;
    }

    public void setSupprimerConducteursLies(boolean supprimerConducteursLies) {
        this.supprimerConducteursLies = supprimerConducteursLies;
    }

    public boolean isSupprimerVehiculesLies() {
        return supprimerVehiculesLies;
    }

    public void setSupprimerVehiculesLies(boolean supprimerVehiculesLies) {
        this.supprimerVehiculesLies = supprimerVehiculesLies;
    }
}
