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

    /**
     * Exécute le traitement init.
     */
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

    /**
     * Exécute le traitement preparer creation.
     */
    public void preparerCreation() {
        newBadgeContent = null;
        annulerSelection();
    }

    /**
     * Exécute le traitement annuler selection.
     */
    public void annulerSelection() {
        selectedBadge = null;
        associationsSuppression = new ArrayList<>();
        conducteursSuppression = new ArrayList<>();
        vehiculesSuppression = new ArrayList<>();
        supprimerConducteursLies = false;
        supprimerVehiculesLies = false;
    }

    /**
     * Exécute le traitement selectionner badge.
     *
     * @param badge : paramètre utilisé par la méthode
     */
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

    /**
     * Exécute le traitement trouver associations badge.
     *
     * @param badge : paramètre utilisé par la méthode
     * @param associateService : paramètre utilisé par la méthode
     * @return List<Associate> : valeur retournée par la méthode
     */
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

    /**
     * Exécute le traitement extraire conducteurs.
     *
     * @param associations : paramètre utilisé par la méthode
     * @return List<Driver> : valeur retournée par la méthode
     */
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

    /**
     * Exécute le traitement extraire vehicules.
     *
     * @param associations : paramètre utilisé par la méthode
     * @return List<Vehicle> : valeur retournée par la méthode
     */
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
     * Retourne badges.
     *
     * @return List<Badge> : valeur retournée par la méthode
     */
    public List<Badge> getBadges() {
        return badges;
    }

    /**
     * Retourne selected badge.
     *
     * @return Badge : valeur retournée par la méthode
     */
    public Badge getSelectedBadge() {
        return selectedBadge;
    }

    /**
     * Modifie selected badge.
     *
     * @param selectedBadge : paramètre utilisé par la méthode
     */
    public void setSelectedBadge(Badge selectedBadge) {
        this.selectedBadge = selectedBadge;
    }

    /**
     * Retourne new badge content.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNewBadgeContent() {
        return newBadgeContent;
    }

    /**
     * Modifie new badge content.
     *
     * @param newBadgeContent : paramètre utilisé par la méthode
     */
    public void setNewBadgeContent(String newBadgeContent) {
        this.newBadgeContent = newBadgeContent;
    }

    /**
     * Retourne associations suppression.
     *
     * @return List<Associate> : valeur retournée par la méthode
     */
    public List<Associate> getAssociationsSuppression() {
        return associationsSuppression;
    }

    /**
     * Retourne conducteurs suppression.
     *
     * @return List<Driver> : valeur retournée par la méthode
     */
    public List<Driver> getConducteursSuppression() {
        return conducteursSuppression;
    }

    /**
     * Retourne vehicules suppression.
     *
     * @return List<Vehicle> : valeur retournée par la méthode
     */
    public List<Vehicle> getVehiculesSuppression() {
        return vehiculesSuppression;
    }

    /**
     * Indique si supprimer conducteurs lies.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isSupprimerConducteursLies() {
        return supprimerConducteursLies;
    }

    /**
     * Modifie supprimer conducteurs lies.
     *
     * @param supprimerConducteursLies : paramètre utilisé par la méthode
     */
    public void setSupprimerConducteursLies(boolean supprimerConducteursLies) {
        this.supprimerConducteursLies = supprimerConducteursLies;
    }

    /**
     * Indique si supprimer vehicules lies.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isSupprimerVehiculesLies() {
        return supprimerVehiculesLies;
    }

    /**
     * Modifie supprimer vehicules lies.
     *
     * @param supprimerVehiculesLies : paramètre utilisé par la méthode
     */
    public void setSupprimerVehiculesLies(boolean supprimerVehiculesLies) {
        this.supprimerVehiculesLies = supprimerVehiculesLies;
    }
}
