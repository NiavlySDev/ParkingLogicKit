package lml.snir.parkinglogickit.client.beans.comptegestion;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 * Bean utilisé pour changer le mot de passe du compte connecté. Les messages
 * sont gardés dans le Bean afin que la page puisse afficher un retour simple à
 * l'utilisateur après la tentative de changement.
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

    /**
     * Retourne login bean.
     *
     * @return LoginBean : valeur retournée par la méthode
     */
    public LoginBean getLoginBean() {
        return loginBean;
    }

    /**
     * Modifie login bean.
     *
     * @param loginBean : paramètre utilisé par la méthode
     */
    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    /**
     * Retourne current password.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Modifie current password.
     *
     * @param currentPassword : paramètre utilisé par la méthode
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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
     * @param newPassword : paramètre utilisé par la méthode
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Retourne new password confirmation.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    /**
     * Modifie new password confirmation.
     *
     * @param newPasswordConfirmation : paramètre utilisé par la méthode
     */
    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    /**
     * Retourne error message.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Modifie error message.
     *
     * @param errorMessage : paramètre utilisé par la méthode
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Indique si error.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isError() {
        return error;
    }

    /**
     * Modifie error.
     *
     * @param error : paramètre utilisé par la méthode
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * Retourne validation message.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getValidationMessage() {
        return validationMessage;
    }

    /**
     * Modifie validation message.
     *
     * @param validationMessage : paramètre utilisé par la méthode
     */
    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    /**
     * Indique si validation.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isValidation() {
        return validation;
    }

    /**
     * Modifie validation.
     *
     * @param validation : paramètre utilisé par la méthode
     */
    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    /**
     * Vérifie l'ancien mot de passe, contrôle la confirmation, puis sauvegarde
     * le nouveau mot de passe du conducteur connecté.
     */
    public void change() throws NoSuchAlgorithmException {
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
                    this.setValidationMessage("Votre mot de passe a bien été changé.");
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
                this.setErrorMessage("Les nouveaux mots de passe ne correspondent pas.");
            }
        } else {
            this.setError(true);
            this.setErrorMessage("Votre mot de passe actuel n'est pas correct.");
        }
    }

}
