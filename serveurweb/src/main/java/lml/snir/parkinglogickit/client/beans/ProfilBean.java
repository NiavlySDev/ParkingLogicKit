package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lml.snir.parkinglogickit.client.beans.comptegestion.LoginBean;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.entity.VehicleType;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 * Données personnelles liées au conducteur connecté : badges et véhicules
 * visibles dans l'écran Mon profil.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class ProfilBean implements Serializable {

    private static final int MAX_VEHICULES = 2;

    @Inject
    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    private List<Associate> associations = new ArrayList<>();
    private List<Badge> badges = new ArrayList<>();
    private List<Vehicle> vehicules = new ArrayList<>();

    private String newBrand;
    private String newNumberPlate;
    private VehicleType newVehicleType = VehicleType.Voiture;

    private Associate selectedAssociation;
    private Badge selectedBadge;
    private String editBrand;
    private String editNumberPlate;
    private VehicleType editVehicleType;

    /**
     * Exécute le traitement init.
     */
    @PostConstruct
    public void init() {
        charger();
    }

    /**
     * Ne charge que les associations appartenant au compte actuellement
     * connecté, y compris lorsque plusieurs véhicules lui sont attribués.
     */
    public void charger() {
        associations = new ArrayList<>();
        badges = new ArrayList<>();
        vehicules = new ArrayList<>();

        if (loginBean.getDriver() == null) {
            return;
        }

        try {
            for (Associate association : MetierFactory.getAssociateService().getAll()) {
                if (!estAssociationDuConducteurConnecte(association)) {
                    continue;
                }

                associations.add(association);
                if (association.getBadge() != null && !badges.contains(association.getBadge())) {
                    badges.add(association.getBadge());
                }
                if (association.getVehicle() != null && !vehicules.contains(association.getVehicle())) {
                    vehicules.add(association.getVehicle());
                }
            }
        } catch (Exception e) {
            addError("Erreur lors du chargement du profil : " + e.getMessage());
        }
    }

    /**
     * Le fragment de navigation est rafraîchi en Ajax. Le profil recharge donc
     * ses liaisons au rendu afin d'afficher les associations créées depuis
     * l'administration dans la même vue.
     *
     * @param event événement de rendu JSF
     */
    public void actualiserAuRendu(ComponentSystemEvent event) {
        charger();
    }

    /**
     * Ajoute un véhicule personnel et le relie au conducteur. Si son badge est
     * déjà attribué, le nouveau véhicule réutilise cette attribution.
     */
    public void ajouterVehicule() {
        if (loginBean.getDriver() == null) {
            addError("Vous devez être connecté pour ajouter un véhicule.");
            return;
        }
        if (vehicules.size() >= MAX_VEHICULES) {
            addError("Vous avez déjà atteint la limite de deux véhicules.");
            return;
        }

        Vehicle vehicle = null;
        try {
            vehicle = new Vehicle();
            vehicle.setBrand(newBrand);
            vehicle.setNumberPlate(newNumberPlate != null ? newNumberPlate.toUpperCase() : null);
            vehicle.setType(newVehicleType);
            vehicle = MetierFactory.getVehicleService().add(vehicle);

            Associate association = new Associate();
            association.setDriver(loginBean.getDriver());
            association.setVehicle(vehicle);
            if (!badges.isEmpty()) {
                association.setBadge(badges.get(0));
            }
            MetierFactory.getAssociateService().add(association);

            resetForm();
            charger();
            addInfo("Véhicule enregistré sur votre profil.");
        } catch (Exception e) {
            supprimerVehiculeOrphelin(vehicle);
            addError("Erreur lors de l'ajout du véhicule : " + e.getMessage());
        }
    }

    /**
     * Détache et supprime un véhicule uniquement s'il appartient au conducteur
     * de la session active.
     *
     * @param association liaison sélectionnée depuis la page
     */
    public void supprimerVehicule(Associate association) {
        if (!estAssociationDuConducteurConnecte(association) || association.getVehicle() == null) {
            addError("Ce véhicule n'appartient pas à votre profil.");
            return;
        }

        try {
            Vehicle vehicle = association.getVehicle();
            MetierFactory.getAssociateService().remove(association);
            MetierFactory.getVehicleService().remove(vehicle);
            charger();
            addInfo("Véhicule supprimé de votre profil.");
        } catch (Exception e) {
            addError("Erreur lors de la suppression du véhicule : " + e.getMessage());
            charger();
        }
    }

    /**
     * Prépare le formulaire de modification d'un véhicule du profil.
     *
     * @param association liaison sélectionnée depuis la page
     */
    public void preparerModificationVehicule(Associate association) {
        selectedAssociation = null;
        if (!estAssociationDuConducteurConnecte(association) || association.getVehicle() == null) {
            addError("Ce véhicule n'appartient pas à votre profil.");
            return;
        }

        selectedAssociation = association;
        editBrand = association.getVehicle().getBrand();
        editNumberPlate = association.getVehicle().getNumberPlate();
        editVehicleType = association.getVehicle().getType();
    }

    /**
     * Enregistre les changements du véhicule préalablement sélectionné.
     */
    public void modifierVehicule() {
        if (!estAssociationDuConducteurConnecte(selectedAssociation)
                || selectedAssociation.getVehicle() == null) {
            addError("Ce véhicule n'appartient pas à votre profil.");
            return;
        }

        try {
            Vehicle vehicle = selectedAssociation.getVehicle();
            vehicle.setBrand(editBrand);
            vehicle.setNumberPlate(editNumberPlate != null ? editNumberPlate.toUpperCase() : null);
            vehicle.setType(editVehicleType);
            MetierFactory.getVehicleService().update(vehicle);
            charger();
            addInfo("Véhicule mis à jour.");
        } catch (Exception e) {
            addError("Erreur lors de la modification du véhicule : " + e.getMessage());
        }
    }

    /**
     * Exécute le traitement selectionner badge vole.
     *
     * @param badge : paramètre utilisé par la méthode
     */
    public void selectionnerBadgeVole(Badge badge) {
        selectedBadge = null;
        if (!estBadgeDuConducteurConnecte(badge)) {
            addError("Ce badge n'appartient pas à votre profil.");
            return;
        }
        selectedBadge = badge;
    }

    /**
     * Supprime un badge perdu après avoir retiré ses associations aux véhicules
     * du conducteur. Un badge partagé avec un autre conducteur est refusé pour
     * ne pas couper son accès depuis ce profil.
     */
    public void declarerBadgeVole() {
        if (!estBadgeDuConducteurConnecte(selectedBadge)) {
            addError("Ce badge n'appartient pas à votre profil.");
            return;
        }

        try {
            List<Associate> associationsBadge = new ArrayList<>();
            for (Associate association : MetierFactory.getAssociateService().getAll()) {
                if (association.getBadge() == null
                        || !Objects.equals(association.getBadge().getId(), selectedBadge.getId())) {
                    continue;
                }
                if (!estAssociationDuConducteurConnecte(association)) {
                    addError("Ce badge est aussi attribué à un autre conducteur. Contactez l'administration.");
                    return;
                }
                associationsBadge.add(association);
            }

            for (Associate association : associationsBadge) {
                association.setBadge(null);
                MetierFactory.getAssociateService().update(association);
            }
            boolean conserveDansHistorique = estPresentDansHistorique(selectedBadge);
            if (!conserveDansHistorique) {
                MetierFactory.getBadgeService().remove(selectedBadge);
            }
            selectedBadge = null;
            charger();
            if (conserveDansHistorique) {
                addInfo("Badge déclaré perdu et désactivé. Son historique d'accès est conservé.");
            } else {
                addInfo("Badge déclaré perdu, désactivé et supprimé.");
            }
        } catch (Exception e) {
            addError("Erreur lors de la déclaration du badge perdu : " + e.getMessage());
            charger();
        }
    }

    /**
     * Exécute le traitement est association du conducteur connecte.
     *
     * @param association : paramètre utilisé par la méthode
     * @return boolean : valeur retournée par la méthode
     */
    private boolean estAssociationDuConducteurConnecte(Associate association) {
        Driver connected = loginBean.getDriver();
        return association != null
                && association.getDriver() != null
                && connected != null
                && Objects.equals(association.getDriver().getId(), connected.getId());
    }

    /**
     * Exécute le traitement est badge du conducteur connecte.
     *
     * @param badge : paramètre utilisé par la méthode
     * @return boolean : valeur retournée par la méthode
     */
    private boolean estBadgeDuConducteurConnecte(Badge badge) {
        if (badge == null || badge.getId() == null) {
            return false;
        }
        for (Associate association : associations) {
            if (estAssociationDuConducteurConnecte(association)
                    && association.getBadge() != null
                    && Objects.equals(association.getBadge().getId(), badge.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exécute le traitement est present dans historique.
     *
     * @param badge : paramètre utilisé par la méthode
     * @return boolean : valeur retournée par la méthode
     */
    private boolean estPresentDansHistorique(Badge badge) throws Exception {
        for (Access access : MetierFactory.getAccessService().getAll()) {
            if (access.getBadge() != null
                    && Objects.equals(access.getBadge().getId(), badge.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Supprime vehicule orphelin.
     *
     * @param vehicle : paramètre utilisé par la méthode
     */
    private void supprimerVehiculeOrphelin(Vehicle vehicle) {
        if (vehicle == null || vehicle.getId() == null) {
            return;
        }
        try {
            MetierFactory.getVehicleService().remove(vehicle);
        } catch (Exception ignored) {
            // Le message principal décrit déjà l'échec de création du profil.
        }
    }

    /**
     * Exécute le traitement reset form.
     */
    private void resetForm() {
        newBrand = null;
        newNumberPlate = null;
        newVehicleType = VehicleType.Voiture;
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
     * Retourne max vehicules.
     *
     * @return int : valeur retournée par la méthode
     */
    public int getMaxVehicules() {
        return MAX_VEHICULES;
    }

    /**
     * Indique si peut ajouter vehicule.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isPeutAjouterVehicule() {
        return vehicules.size() < MAX_VEHICULES;
    }

    /**
     * Retourne vehicle types.
     *
     * @return VehicleType[] : valeur retournée par la méthode
     */
    public VehicleType[] getVehicleTypes() {
        return VehicleType.values();
    }

    /**
     * Retourne new brand.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNewBrand() {
        return newBrand;
    }

    /**
     * Modifie new brand.
     *
     * @param newBrand : paramètre utilisé par la méthode
     */
    public void setNewBrand(String newBrand) {
        this.newBrand = newBrand;
    }

    /**
     * Retourne new number plate.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNewNumberPlate() {
        return newNumberPlate;
    }

    /**
     * Modifie new number plate.
     *
     * @param newNumberPlate : paramètre utilisé par la méthode
     */
    public void setNewNumberPlate(String newNumberPlate) {
        this.newNumberPlate = newNumberPlate;
    }

    /**
     * Retourne new vehicle type.
     *
     * @return VehicleType : valeur retournée par la méthode
     */
    public VehicleType getNewVehicleType() {
        return newVehicleType;
    }

    /**
     * Modifie new vehicle type.
     *
     * @param newVehicleType : paramètre utilisé par la méthode
     */
    public void setNewVehicleType(VehicleType newVehicleType) {
        this.newVehicleType = newVehicleType;
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
     * Retourne selected badge.
     *
     * @return Badge : valeur retournée par la méthode
     */
    public Badge getSelectedBadge() {
        return selectedBadge;
    }

    /**
     * Retourne edit brand.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getEditBrand() {
        return editBrand;
    }

    /**
     * Modifie edit brand.
     *
     * @param editBrand : paramètre utilisé par la méthode
     */
    public void setEditBrand(String editBrand) {
        this.editBrand = editBrand;
    }

    /**
     * Retourne edit number plate.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getEditNumberPlate() {
        return editNumberPlate;
    }

    /**
     * Modifie edit number plate.
     *
     * @param editNumberPlate : paramètre utilisé par la méthode
     */
    public void setEditNumberPlate(String editNumberPlate) {
        this.editNumberPlate = editNumberPlate;
    }

    /**
     * Retourne edit vehicle type.
     *
     * @return VehicleType : valeur retournée par la méthode
     */
    public VehicleType getEditVehicleType() {
        return editVehicleType;
    }

    /**
     * Modifie edit vehicle type.
     *
     * @param editVehicleType : paramètre utilisé par la méthode
     */
    public void setEditVehicleType(VehicleType editVehicleType) {
        this.editVehicleType = editVehicleType;
    }
}
