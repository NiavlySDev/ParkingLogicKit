package lml.snir.parkinglogickit.client.beans.comptegestion;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import lml.snir.parkinglogickit.metier.entity.Admin;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;
import org.primefaces.PrimeFaces;

/**
 * Bean de session utilisé pour la connexion.
 * Il conserve l'utilisateur connecté et permet aux pages JSF de savoir si
 * l'utilisateur est simple conducteur ou administrateur.
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

}
