package lml.snir.parkinglogickit.client.beans.comptegestion;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import lml.snir.parkinglogickit.client.fakedata.Admin;
import lml.snir.parkinglogickit.client.fakedata.Driver;
import org.primefaces.PrimeFaces;

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

    public Driver getUser() {
        return driver;
    }
    
    public boolean isAdmin(){
        return driver instanceof Admin;
    }
    
    public void login(){
        setLogged(true);
        PrimeFaces.current().executeScript("location.reload();");
    }
    
    public void logout(){
        this.driver = null;
        this.setLogged(false);
        this.setUsername("");
        this.setPassword("");
        PrimeFaces.current().executeScript("location.reload();");
    }
    
}