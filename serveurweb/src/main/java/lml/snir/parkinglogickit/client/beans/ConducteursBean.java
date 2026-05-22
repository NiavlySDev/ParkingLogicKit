package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
            MetierFactory.getDriverService().remove(selectedDriver);

            selectedDriver = null;
            charger();
            addInfo("Conducteur " + username + " supprimé.");
        } catch (Exception e) {
            addError("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public void selectionner(Driver d) {
        this.selectedDriver = d;
        this.selectedIsAdmin = d instanceof Admin;
    }

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

    private Driver creerConducteurAvecTypeChoisi(Driver source, String usernameFinal) {
        Driver target = selectedIsAdmin ? new Admin() : new Driver();
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setUsername(usernameFinal);
        target.setPassword(source.getPassword());
        target.setAge(source.getAge());
        target.setIsMale(source.isIsMale());
        return target;
    }

    private List<Associate> trouverAssociations(Driver conducteur, AssociateService associateService) throws Exception {
        List<Associate> associationsConducteur = new ArrayList<>();
        for (Associate association : associateService.getAll()) {
            Driver driverAssociation = association.getDriver();
            if (driverAssociation != null && driverAssociation.getId() == conducteur.getId()) {
                associationsConducteur.add(association);
            }
        }
        return associationsConducteur;
    }

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

    private String genererContenuBadge() {
        StringBuilder result = new StringBuilder();
        Random rng = new Random();
        for (int i = 0; i < 11; i++) {
            result.append(BADGE_CHARS.charAt(rng.nextInt(BADGE_CHARS.length())));
        }
        return result.toString();
    }

    private void addInfo(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    private void addError(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    public List<Driver> getConducteurs() {
        return conducteurs;
    }

    public Driver getSelectedDriver() {
        return selectedDriver;
    }

    public void setSelectedDriver(Driver d) {
        this.selectedDriver = d;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public void setNewFirstName(String v) {
        this.newFirstName = v;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewLastName(String v) {
        this.newLastName = v;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String v) {
        this.newUsername = v;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String v) {
        this.newPassword = v;
    }

    public int getNewAge() {
        return newAge;
    }

    public void setNewAge(int v) {
        this.newAge = v;
    }

    public boolean isNewIsMale() {
        return newIsMale;
    }

    public void setNewIsMale(boolean v) {
        this.newIsMale = v;
    }

    public boolean isNewIsAdmin() {
        return newIsAdmin;
    }

    public void setNewIsAdmin(boolean newIsAdmin) {
        this.newIsAdmin = newIsAdmin;
    }

    public boolean isSelectedIsAdmin() {
        return selectedIsAdmin;
    }

    public void setSelectedIsAdmin(boolean selectedIsAdmin) {
        this.selectedIsAdmin = selectedIsAdmin;
    }

    public boolean isCreationBadge() {
        return creationBadge;
    }

    public void setCreationBadge(boolean creationBadge) {
        this.creationBadge = creationBadge;
    }

    public boolean isCreationVehicule() {
        return creationVehicule;
    }

    public void setCreationVehicule(boolean creationVehicule) {
        this.creationVehicule = creationVehicule;
    }

    public boolean isCreationAssocation() {
        return creationAssocation;
    }

    public void setCreationAssocation(boolean creationAssocation) {
        this.creationAssocation = creationAssocation;
    }

    public String getNewBrand() {
        return newBrand;
    }

    public void setNewBrand(String newBrand) {
        this.newBrand = newBrand;
    }

    public String getNewNumberPlate() {
        return newNumberPlate;
    }

    public void setNewNumberPlate(String newNumberPlate) {
        this.newNumberPlate = newNumberPlate;
    }

    public VehicleType getNewVehicleType() {
        return newVehicleType;
    }

    public void setNewVehicleType(VehicleType newVehicleType) {
        this.newVehicleType = newVehicleType;
    }

    public VehicleType[] getVehicleTypes() {
        return VehicleType.values();
    }

}
