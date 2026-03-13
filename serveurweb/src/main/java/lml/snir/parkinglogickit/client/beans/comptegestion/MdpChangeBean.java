package lml.snir.parkinglogickit.client.beans.comptegestion;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 *
 * @author Sylvain Crocquevieille
 */
@Named
@SessionScoped
public class MdpChangeBean implements Serializable {

    @Inject
    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirmation;

    private String errorMessage;
    private boolean error;
    private String validationMessage;
    private boolean validation;

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public void change() {
        this.setValidation(false);
        this.setValidationMessage("");
        this.setError(false);
        this.setErrorMessage("");
        if (loginBean.getDriver().getPassword().equalsIgnoreCase(currentPassword)) {
            if (newPassword.equalsIgnoreCase(newPasswordConfirmation)) {
                loginBean.getDriver().setPassword(newPassword);
                try {
                    DriverService us = MetierFactory.getDriverService();
                    us.update(loginBean.getDriver());
                    this.setValidation(true);
                    this.setValidationMessage("Vous avez changez votre mot de passe!");
                    this.setCurrentPassword("");
                    this.setNewPassword("");
                    this.setNewPasswordConfirmation("");
                } catch (Exception ex) {
                    Logger.getLogger(MdpChangeBean.class.getName()).log(Level.SEVERE, null, ex);
                    this.setError(true);
                    this.setErrorMessage(ex.getLocalizedMessage());
                }
            } else {
                this.setError(true);
                this.setErrorMessage("Les nouveaux mots de passes ne correspondent pas!");
            }
        } else {
            this.setError(true);
            this.setErrorMessage("Votre mot de passe actuel n'est pas le bon!");
        }
    }

}
