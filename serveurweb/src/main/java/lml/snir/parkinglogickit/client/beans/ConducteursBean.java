package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
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
            Driver d = new Driver();
            d.setFirstName(newFirstName);
            d.setLastName(newLastName);
            d.setUsername(newUsername);
            d.setPassword(newPassword);
            d.setAge(newAge);
            d.setIsMale(newIsMale);
            ds.add(d);
            addInfo("Conducteur " + newUsername + " créé.");
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
            MetierFactory.getDriverService().remove(selectedDriver);
            addInfo("Conducteur supprimé.");
            selectedDriver = null;
            charger();
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
}
