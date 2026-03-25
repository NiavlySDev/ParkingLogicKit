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
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class ConducteursBean implements Serializable {

    private List<Driver> conducteurs = new ArrayList<>();
    private Driver selectedDriver;

    private String newFirstName;
    private String newLastName;
    private String newUsername;
    private String newPassword;
    private int newAge;
    private boolean newIsMale = true;

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

    public void charger() {
        try {
            conducteurs = MetierFactory.getDriverService().getAll();
        } catch (Exception e) {
            addError("Erreur chargement conducteurs : " + e.getMessage());
        }
    }

    public void creer() {
        try {
            DriverService ds = MetierFactory.getDriverService();
            BadgeService bs = MetierFactory.getBadgeService();
            AssociateService as = MetierFactory.getAssociateService();
            VehicleService vs = MetierFactory.getVehicleService();

            Driver d = new Driver();
            d.setFirstName(newFirstName);
            d.setLastName(newLastName);
            d.setUsername(newUsername);
            d.setPassword(newPassword);
            d.setAge(newAge);
            d.setIsMale(newIsMale);
            ds.add(d);

            Badge b = null;
            if (creationBadge) {
                String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                StringBuilder result = new StringBuilder();
                Random rng = new Random();
                for (int i = 0; i < 11; i++) {
                    result.append(CHARS.charAt(rng.nextInt(CHARS.length())));
                }
                b = new Badge();
                b.setContent(result.toString());
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
            addError("Erreur création : " + e.getMessage());
        }
    }

    public void modifier() {
        if (selectedDriver == null) {
            return;
        }
        try {
            MetierFactory.getDriverService().update(selectedDriver);
            addInfo("Conducteur mis à jour.");
            charger();
        } catch (Exception e) {
            addError("Erreur modification : " + e.getMessage());
        }
    }

    public void supprimer() {
        if (selectedDriver == null) {
            return;
        }
        try {
            DriverService ds = MetierFactory.getDriverService();
            
            ds.remove(selectedDriver);
            
            selectedDriver = null;
            charger();
            addInfo("Conducteur " + newUsername + " supprimés, Badge et Assiciation également.");
        } catch (Exception e) {
            addError("Erreur suppression : " + e.getMessage());
        }
    }

    public void selectionner(Driver d) {
        this.selectedDriver = d;
    }

    private void resetForm() {
        newFirstName = null;
        newLastName = null;
        newUsername = null;
        newPassword = null;
        newAge = 0;
        newIsMale = true;
        creationBadge = false;
        creationVehicule = false;
        creationAssocation = false;
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
