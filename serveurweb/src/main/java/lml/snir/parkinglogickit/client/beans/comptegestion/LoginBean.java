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
    private boolean fallback;

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

    public void login() {
        try {
            DriverService ds = MetierFactory.getDriverService();
            if (ds.getByUsername(username) == null) {
                activateFallback();
                return;
            }
            Driver driverDS = ds.getByUsername(username);
            if (!driverDS.getPassword().equals(password)) {
                activateFallback();
                return;
            }

            setLogged(true);
        } catch (Exception ex) {
            activateFallback();
        }
        setLogged(true);
        PrimeFaces.current().executeScript("location.reload();");
    }

    public void logout() {
        this.driver = null;
        this.setLogged(false);
        this.setUsername("");
        this.setPassword("");
        PrimeFaces.current().executeScript("location.reload();");
    }

    private void activateFallback() {
        String fallbackMode = "[FallBack Mode]";
        this.fallback = true;
        this.driver = new Admin();
        this.driver.setAge(0);
        this.driver.setFirstName(fallbackMode);
        this.driver.setId(0);
        this.driver.setIsMale(true);
        this.driver.setLastName(fallbackMode);
        this.driver.setPassword(fallbackMode);
        this.driver.setUsername(fallbackMode);
        setLogged(true);
        PrimeFaces.current().executeScript("location.reload();");
    }

}
