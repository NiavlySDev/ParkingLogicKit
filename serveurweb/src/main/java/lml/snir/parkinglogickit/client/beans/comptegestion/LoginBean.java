package lml.snir.parkinglogickit.client.beans.comptegestion;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import lml.snir.parkinglogickit.metier.entity.Admin;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;
import org.primefaces.PrimeFaces;

/**
 * Bean de session utilisé pour la connexion. Il conserve l'utilisateur connecté
 * et permet aux pages JSF de savoir si l'utilisateur est simple conducteur ou
 * administrateur.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private Driver driver;
    private boolean logged;

    private String editFirstName;
    private String editLastName;
    private String editUsername;
    private int editAge;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public Driver getDriver() {
        return driver;
    }

    public boolean isAdmin() {
        return driver instanceof Admin;
    }

    /**
     * Vérifie les identifiants saisis et ouvre la session utilisateur.
     */
    public void login() {
        try {
            DriverService ds = MetierFactory.getDriverService();
            if (ds.getByUsername(username) == null) {
                System.out.println("Connexion impossible : utilisateur inconnu");
                return;
            }
            Driver driverDS = ds.getByUsername(username);
            if (!driverDS.getPassword().equals(password)) {
                System.out.println("Connexion impossible : mot de passe incorrect");
                return;
            }

            setLogged(true);
            this.driver = driverDS;
            this.setUsername(this.driver.getUsername());
            this.setPassword(this.driver.getPassword());
            chargerChampsModificationCompte();
        } catch (Exception ex) {
            System.out.println("Erreur pendant la connexion : " + ex);
        }
        PrimeFaces.current().executeScript("location.reload();");
    }

    /**
     * Ferme la session courante et revient à un état déconnecté.
     */
    public void logout() {
        this.driver = null;
        this.setLogged(false);
        this.setUsername("");
        this.setPassword("");
        PrimeFaces.current().executeScript("location.reload();");
    }

    private void chargerChampsModificationCompte() {
        if (driver == null) {
            return;
        }
        editFirstName = driver.getFirstName();
        editLastName = driver.getLastName();
        editUsername = driver.getUsername();
        editAge = driver.getAge();
    }

    public void preparerModificationCompte() {
        chargerChampsModificationCompte();
    }

    public void changerPrenom() {
        if (driver == null) {
            return;
        }
        try {
            driver.setFirstName(editFirstName);
            sauvegarderCompte("Prénom mis à jour.");
        } catch (Exception ex) {
            addError("Erreur lors de la modification du prénom : " + ex.getMessage());
        }
    }

    public void changerNom() {
        if (driver == null) {
            return;
        }
        try {
            driver.setLastName(editLastName);
            sauvegarderCompte("Nom mis à jour.");
        } catch (Exception ex) {
            addError("Erreur lors de la modification du nom : " + ex.getMessage());
        }
    }

    public void changerIdentifiant() {
        if (driver == null) {
            return;
        }
        try {
            DriverService driverService = MetierFactory.getDriverService();
            Driver existingDriver = driverService.getByUsername(editUsername);

            /*
             * L'identifiant est unique en base. On laisse l'utilisateur garder
             * son propre identifiant, mais on bloque celui d'un autre compte.
             */
            if (existingDriver != null && existingDriver.getId() != driver.getId()) {
                addError("Cet identifiant est déjà utilisé.");
                return;
            }

            driver.setUsername(editUsername);
            sauvegarderCompte("Identifiant mis à jour.");
            username = editUsername;
        } catch (Exception ex) {
            addError("Erreur lors de la modification de l'identifiant : " + ex.getMessage());
        }
    }

    public void changerAge() {
        if (driver == null) {
            return;
        }
        try {
            driver.setAge(editAge);
            sauvegarderCompte("Âge mis à jour.");
        } catch (Exception ex) {
            addError("Erreur lors de la modification de l'âge : " + ex.getMessage());
        }
    }

    private void sauvegarderCompte(String message) throws Exception {
        /*
         * Toutes les modifications du compte passent par MetierFactory pour
         * rester sur la même couche métier que l'administration des utilisateurs.
         */
        MetierFactory.getDriverService().update(driver);
        chargerChampsModificationCompte();
        addInfo(message);
    }

    private void addInfo(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private void addError(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public String getEditFirstName() {
        return editFirstName;
    }

    public void setEditFirstName(String editFirstName) {
        this.editFirstName = editFirstName;
    }

    public String getEditLastName() {
        return editLastName;
    }

    public void setEditLastName(String editLastName) {
        this.editLastName = editLastName;
    }

    public String getEditUsername() {
        return editUsername;
    }

    public void setEditUsername(String editUsername) {
        this.editUsername = editUsername;
    }

    public int getEditAge() {
        return editAge;
    }

    public void setEditAge(int editAge) {
        this.editAge = editAge;
    }

}
