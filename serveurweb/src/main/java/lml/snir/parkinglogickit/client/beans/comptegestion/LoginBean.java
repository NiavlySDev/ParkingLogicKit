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

    /**
     * Retourne username.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getUsername() {
        return username;
    }

    /**
     * Modifie username.
     *
     * @param username : paramètre utilisé par la méthode
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retourne password.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getPassword() {
        return password;
    }

    /**
     * Modifie password.
     *
     * @param password : paramètre utilisé par la méthode
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Indique si logged.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * Modifie logged.
     *
     * @param logged : paramètre utilisé par la méthode
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * Retourne driver.
     *
     * @return Driver : valeur retournée par la méthode
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Indique si admin.
     *
     * @return boolean : valeur retournée par la méthode
     */
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
            if (!driverDS.isValid(password)) {
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

    /**
     * Recharge les données nécessaires à l'affichage.
     */
    private void chargerChampsModificationCompte() {
        if (driver == null) {
            return;
        }
        editFirstName = driver.getFirstName();
        editLastName = driver.getLastName();
        editUsername = driver.getUsername();
        editAge = driver.getAge();
    }

    /**
     * Exécute le traitement preparer modification compte.
     */
    public void preparerModificationCompte() {
        chargerChampsModificationCompte();
    }

    /**
     * Exécute le traitement changer prenom.
     */
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

    /**
     * Exécute le traitement changer nom.
     */
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

    /**
     * Exécute le traitement changer identifiant.
     */
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

    /**
     * Exécute le traitement changer age.
     */
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

    /**
     * Exécute le traitement sauvegarder compte.
     *
     * @param message : paramètre utilisé par la méthode
     */
    private void sauvegarderCompte(String message) throws Exception {
        /*
         * Toutes les modifications du compte passent par MetierFactory pour
         * rester sur la même couche métier que l'administration des utilisateurs.
         */
        MetierFactory.getDriverService().update(driver);
        chargerChampsModificationCompte();
        addInfo(message);
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
     * Retourne edit first name.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getEditFirstName() {
        return editFirstName;
    }

    /**
     * Modifie edit first name.
     *
     * @param editFirstName : paramètre utilisé par la méthode
     */
    public void setEditFirstName(String editFirstName) {
        this.editFirstName = editFirstName;
    }

    /**
     * Retourne edit last name.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getEditLastName() {
        return editLastName;
    }

    /**
     * Modifie edit last name.
     *
     * @param editLastName : paramètre utilisé par la méthode
     */
    public void setEditLastName(String editLastName) {
        this.editLastName = editLastName;
    }

    /**
     * Retourne edit username.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getEditUsername() {
        return editUsername;
    }

    /**
     * Modifie edit username.
     *
     * @param editUsername : paramètre utilisé par la méthode
     */
    public void setEditUsername(String editUsername) {
        this.editUsername = editUsername;
    }

    /**
     * Retourne edit age.
     *
     * @return int : valeur retournée par la méthode
     */
    public int getEditAge() {
        return editAge;
    }

    /**
     * Modifie edit age.
     *
     * @param editAge : paramètre utilisé par la méthode
     */
    public void setEditAge(int editAge) {
        this.editAge = editAge;
    }

}
