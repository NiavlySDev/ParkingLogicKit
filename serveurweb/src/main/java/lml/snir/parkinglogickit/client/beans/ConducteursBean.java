package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import lml.snir.parkinglogickit.metier.entity.Admin;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.entity.VehicleType;
import lml.snir.parkinglogickit.metier.transactionel.AssociateService;
import lml.snir.parkinglogickit.metier.transactionel.BadgeService;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.parkinglogickit.metier.transactionel.VehicleService;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 * Bean de gestion des conducteurs. Il permet d'afficher, créer, modifier et
 * supprimer les conducteurs depuis l'interface d'administration. Il peut aussi
 * créer automatiquement un badge, un véhicule et leur association lors de la
 * création d'un conducteur.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class ConducteursBean implements Serializable {

    private static final String BADGE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private List<Driver> conducteurs = new ArrayList<>();
    private Driver selectedDriver;
    private List<Associate> associationsSuppression = new ArrayList<>();
    private List<Badge> badgesSuppression = new ArrayList<>();
    private List<Vehicle> vehiculesSuppression = new ArrayList<>();
    private boolean supprimerBadgesLies;
    private boolean supprimerVehiculesLies;

    private String newFirstName;
    private String newLastName;
    private String newUsername;
    private String newPassword;
    private int newAge;
    private boolean newIsMale = true;
    private boolean newIsAdmin;
    private boolean selectedIsAdmin;

    private boolean creationBadge;
    private boolean creationVehicule;
    private boolean creationAssocation;

    private String newBrand;
    private String newNumberPlate;
    private VehicleType newVehicleType = VehicleType.Voiture;

    /**
     * Exécute le traitement init.
     */
    @PostConstruct
    public void init() {
        charger();
    }

    /**
     * Recharge la liste des conducteurs affichés dans le tableau.
     */
    public void charger() {
        try {
            conducteurs = MetierFactory.getDriverService().getAll();
        } catch (Exception e) {
            addError("Erreur lors du chargement des conducteurs : " + e.getMessage());
        }
    }

    /**
     * Crée un conducteur et, si demandé, les éléments liés à son accès parking.
     */
    public void creer() {
        try {
            DriverService ds = MetierFactory.getDriverService();
            BadgeService bs = MetierFactory.getBadgeService();
            AssociateService as = MetierFactory.getAssociateService();
            VehicleService vs = MetierFactory.getVehicleService();

            Driver d = newIsAdmin ? new Admin() : new Driver();
            d.setFirstName(newFirstName);
            d.setLastName(newLastName);
            d.setUsername(newUsername);
            d.setPassword(newPassword);
            d.setAge(newAge);
            d.setIsMale(newIsMale);
            ds.add(d);

            Badge b = null;
            if (creationBadge) {
                b = new Badge();
                b.setContent(genererContenuBadge());
                bs.add(b);
            }

            Vehicle v = null;
            if (creationVehicule) {
                v = new Vehicle();
                v.setBrand(newBrand);
                v.setNumberPlate(newNumberPlate);
                v.setType(newVehicleType);
                vs.add(v);
            }

            if (creationAssocation && creationBadge && creationVehicule) {
                Associate a = new Associate();
                a.setBadge(b);
                a.setDriver(d);
                a.setVehicle(v);
                as.add(a);
            }

            addInfo("Conducteur " + newUsername + " créé avec succès.");
            resetForm();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la création : " + e.getMessage());
        }
    }

    /**
     * Enregistre les modifications faites sur le conducteur sélectionné.
     */
    public void modifier() {
        if (selectedDriver == null) {
            return;
        }
        try {
            DriverService driverService = MetierFactory.getDriverService();
            AssociateService associateService = MetierFactory.getAssociateService();

            selectedDriver = sauvegarderConducteurAvecRole(driverService, associateService, selectedDriver);

            addInfo("Conducteur mis à jour.");
            annulerSelection();
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Supprime le conducteur sélectionné.
     */
    public void supprimer() {
        if (selectedDriver == null) {
            return;
        }
        try {
            String username = selectedDriver.getUsername();
            AssociateService associateService = MetierFactory.getAssociateService();

            for (Associate association : associationsSuppression) {
                associateService.remove(association);
            }
            if (supprimerBadgesLies) {
                for (Badge badge : badgesSuppression) {
                    supprimerAssociationsBadge(badge, associateService);
                    MetierFactory.getBadgeService().remove(badge);
                }
            }
            if (supprimerVehiculesLies) {
                for (Vehicle vehicle : vehiculesSuppression) {
                    supprimerAssociationsVehicule(vehicle, associateService);
                    MetierFactory.getVehicleService().remove(vehicle);
                }
            }

            MetierFactory.getDriverService().remove(selectedDriver);
            annulerSelection();
            charger();
            addInfo("Conducteur " + username + " supprimé.");
        } catch (Exception e) {
            addError("Erreur lors de la suppression : " + e.getMessage());
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
        selectedDriver = null;
        selectedIsAdmin = false;
        associationsSuppression = new ArrayList<>();
        badgesSuppression = new ArrayList<>();
        vehiculesSuppression = new ArrayList<>();
        supprimerBadgesLies = false;
        supprimerVehiculesLies = false;
    }

    /**
     * Exécute le traitement selectionner.
     *
     * @param d : paramètre utilisé par la méthode
     */
    public void selectionner(Driver d) {
        this.selectedDriver = d;
        this.selectedIsAdmin = d instanceof Admin;
    }

    /**
     * Exécute le traitement preparer suppression.
     *
     * @param d : paramètre utilisé par la méthode
     */
    public void preparerSuppression(Driver d) {
        selectionner(d);
        supprimerBadgesLies = false;
        supprimerVehiculesLies = false;
        try {
            associationsSuppression = trouverAssociations(d, MetierFactory.getAssociateService());
            badgesSuppression = extraireBadges(associationsSuppression);
            vehiculesSuppression = extraireVehicules(associationsSuppression);
        } catch (Exception e) {
            associationsSuppression = new ArrayList<>();
            badgesSuppression = new ArrayList<>();
            vehiculesSuppression = new ArrayList<>();
            addError("Erreur lors de la préparation de la suppression : " + e.getMessage());
        }
    }

    /**
     * Exécute le traitement sauvegarder conducteur avec role.
     *
     * @param driverService : paramètre utilisé par la méthode
     * @param associateService : paramètre utilisé par la méthode
     * @param source : paramètre utilisé par la méthode
     * @return Driver : valeur retournée par la méthode
     */
    private Driver sauvegarderConducteurAvecRole(DriverService driverService,
            AssociateService associateService, Driver source) throws Exception {
        if ((source instanceof Admin) == selectedIsAdmin) {
            driverService.update(source);
            return source;
        }

        /*
         * Avec l'héritage JPA, un simple update ne change pas le type réel
         * Driver/Admin. On remplace donc l'objet par un nouveau du bon type,
         * puis on déplace ses associations avant de supprimer l'ancien.
         */
        List<Associate> associations = trouverAssociations(source, associateService);
        String usernameFinal = source.getUsername();
        String usernameTemporaire = usernameFinal + "_ancien_role_" + System.currentTimeMillis();

        source.setUsername(usernameTemporaire);
        driverService.update(source);

        Driver target = creerConducteurAvecTypeChoisi(source, usernameFinal);
        target = driverService.add(target);

        for (Associate association : associations) {
            association.setDriver(target);
            associateService.update(association);
        }

        driverService.remove(source);
        return target;
    }

    /**
     * Crée conducteur avec type choisi.
     *
     * @param source : paramètre utilisé par la méthode
     * @param usernameFinal : paramètre utilisé par la méthode
     * @return Driver : valeur retournée par la méthode
     */
    private Driver creerConducteurAvecTypeChoisi(Driver source, String usernameFinal) throws NoSuchAlgorithmException {
        Driver target = selectedIsAdmin ? new Admin() : new Driver();
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setUsername(usernameFinal);
        target.setPassword(source.getPassword());
        target.setAge(source.getAge());
        target.setIsMale(source.isIsMale());
        return target;
    }

    /**
     * Exécute le traitement trouver associations.
     *
     * @param conducteur : paramètre utilisé par la méthode
     * @param associateService : paramètre utilisé par la méthode
     * @return List<Associate> : valeur retournée par la méthode
     */
    private List<Associate> trouverAssociations(Driver conducteur, AssociateService associateService) throws Exception {
        List<Associate> associationsConducteur = new ArrayList<>();
        for (Associate association : associateService.getAll()) {
            Driver driverAssociation = association.getDriver();
            if (driverAssociation != null && Objects.equals(driverAssociation.getId(), conducteur.getId())) {
                associationsConducteur.add(association);
            }
        }
        return associationsConducteur;
    }

    /**
     * Exécute le traitement extraire badges.
     *
     * @param associations : paramètre utilisé par la méthode
     * @return List<Badge> : valeur retournée par la méthode
     */
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
        newFirstName = null;
        newLastName = null;
        newUsername = null;
        newPassword = null;
        newAge = 0;
        newIsMale = true;
        newIsAdmin = false;
        selectedIsAdmin = false;
        creationBadge = false;
        creationVehicule = false;
        creationAssocation = false;
        newBrand = null;
        newNumberPlate = null;
        newVehicleType = VehicleType.Voiture;
    }

    /**
     * Exécute le traitement generer contenu badge.
     *
     * @return String : valeur retournée par la méthode
     */
    private String genererContenuBadge() {
        StringBuilder result = new StringBuilder();
        Random rng = new Random();
        for (int i = 0; i < 11; i++) {
            result.append(BADGE_CHARS.charAt(rng.nextInt(BADGE_CHARS.length())));
        }
        return result.toString();
    }

    /**
     * Ajoute info.
     *
     * @param msg : paramètre utilisé par la méthode
     */
    private void addInfo(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    /**
     * Ajoute error.
     *
     * @param msg : paramètre utilisé par la méthode
     */
    private void addError(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    /**
     * Retourne conducteurs.
     *
     * @return List<Driver> : valeur retournée par la méthode
     */
    public List<Driver> getConducteurs() {
        return conducteurs;
    }

    /**
     * Retourne selected driver.
     *
     * @return Driver : valeur retournée par la méthode
     */
    public Driver getSelectedDriver() {
        return selectedDriver;
    }

    /**
     * Modifie selected driver.
     *
     * @param d : paramètre utilisé par la méthode
     */
    public void setSelectedDriver(Driver d) {
        this.selectedDriver = d;
    }

    /**
     * Retourne new first name.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNewFirstName() {
        return newFirstName;
    }

    /**
     * Modifie new first name.
     *
     * @param v : paramètre utilisé par la méthode
     */
    public void setNewFirstName(String v) {
        this.newFirstName = v;
    }

    /**
     * Retourne new last name.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNewLastName() {
        return newLastName;
    }

    /**
     * Modifie new last name.
     *
     * @param v : paramètre utilisé par la méthode
     */
    public void setNewLastName(String v) {
        this.newLastName = v;
    }

    /**
     * Retourne new username.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNewUsername() {
        return newUsername;
    }

    /**
     * Modifie new username.
     *
     * @param v : paramètre utilisé par la méthode
     */
    public void setNewUsername(String v) {
        this.newUsername = v;
    }

    /**
     * Retourne new password.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Modifie new password.
     *
     * @param v : paramètre utilisé par la méthode
     */
    public void setNewPassword(String v) {
        this.newPassword = v;
    }

    /**
     * Retourne new age.
     *
     * @return int : valeur retournée par la méthode
     */
    public int getNewAge() {
        return newAge;
    }

    /**
     * Modifie new age.
     *
     * @param v : paramètre utilisé par la méthode
     */
    public void setNewAge(int v) {
        this.newAge = v;
    }

    /**
     * Indique si new is male.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isNewIsMale() {
        return newIsMale;
    }

    /**
     * Modifie new is male.
     *
     * @param v : paramètre utilisé par la méthode
     */
    public void setNewIsMale(boolean v) {
        this.newIsMale = v;
    }

    /**
     * Indique si new is admin.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isNewIsAdmin() {
        return newIsAdmin;
    }

    /**
     * Modifie new is admin.
     *
     * @param newIsAdmin : paramètre utilisé par la méthode
     */
    public void setNewIsAdmin(boolean newIsAdmin) {
        this.newIsAdmin = newIsAdmin;
    }

    /**
     * Indique si selected is admin.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isSelectedIsAdmin() {
        return selectedIsAdmin;
    }

    /**
     * Modifie selected is admin.
     *
     * @param selectedIsAdmin : paramètre utilisé par la méthode
     */
    public void setSelectedIsAdmin(boolean selectedIsAdmin) {
        this.selectedIsAdmin = selectedIsAdmin;
    }

    /**
     * Indique si creation badge.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isCreationBadge() {
        return creationBadge;
    }

    /**
     * Modifie creation badge.
     *
     * @param creationBadge : paramètre utilisé par la méthode
     */
    public void setCreationBadge(boolean creationBadge) {
        this.creationBadge = creationBadge;
    }

    /**
     * Indique si creation vehicule.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isCreationVehicule() {
        return creationVehicule;
    }

    /**
     * Modifie creation vehicule.
     *
     * @param creationVehicule : paramètre utilisé par la méthode
     */
    public void setCreationVehicule(boolean creationVehicule) {
        this.creationVehicule = creationVehicule;
    }

    /**
     * Indique si creation assocation.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isCreationAssocation() {
        return creationAssocation;
    }

    /**
     * Modifie creation assocation.
     *
     * @param creationAssocation : paramètre utilisé par la méthode
     */
    public void setCreationAssocation(boolean creationAssocation) {
        this.creationAssocation = creationAssocation;
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
     * Retourne vehicle types.
     *
     * @return VehicleType[] : valeur retournée par la méthode
     */
    public VehicleType[] getVehicleTypes() {
        return VehicleType.values();
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
     * Retourne badges suppression.
     *
     * @return List<Badge> : valeur retournée par la méthode
     */
    public List<Badge> getBadgesSuppression() {
        return badgesSuppression;
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
     * Indique si supprimer badges lies.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isSupprimerBadgesLies() {
        return supprimerBadgesLies;
    }

    /**
     * Modifie supprimer badges lies.
     *
     * @param supprimerBadgesLies : paramètre utilisé par la méthode
     */
    public void setSupprimerBadgesLies(boolean supprimerBadgesLies) {
        this.supprimerBadgesLies = supprimerBadgesLies;
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
