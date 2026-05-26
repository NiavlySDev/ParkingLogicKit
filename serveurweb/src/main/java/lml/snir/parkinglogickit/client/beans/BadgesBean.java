package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 * Bean de gestion des badges RFID. Cette classe alimente la page
 * d'administration des badges et regroupe les actions simples : afficher, créer
 * et supprimer un badge.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class BadgesBean implements Serializable {

    private List<Badge> badges = new ArrayList<>();
    private Badge selectedBadge;
    private String newBadgeContent;

    @PostConstruct
    public void init() {
        charger();
    }

    /**
     * Recharge la liste des badges depuis la couche métier.
     */
    public void charger() {
        try {
            badges = MetierFactory.getBadgeService().getAll();
        } catch (Exception e) {
            addError("Erreur lors du chargement des badges : " + e.getMessage());
        }
    }

    /**
     * Crée un badge avec le contenu RFID saisi dans la fenêtre de création.
     */
    public void creerBadge() {
        try {
            Badge badge = new Badge();
            badge.setContent(newBadgeContent);
            MetierFactory.getBadgeService().add(badge);
            addInfo("Badge créé.");
            newBadgeContent = null;
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la création du badge : " + e.getMessage());
        }
    }

    /**
     * Supprime le badge sélectionné dans le tableau.
     */
    public void supprimerBadge() {
        if (selectedBadge == null) {
            return;
        }
        try {
            MetierFactory.getBadgeService().remove(selectedBadge);
            addInfo("Badge supprimé.");
            selectedBadge = null;
            charger();
        } catch (Exception e) {
            addError("Erreur lors de la suppression du badge : " + e.getMessage());
        }
    }

    public void selectionnerBadge(Badge badge) {
        this.selectedBadge = badge;
    }

    private void addInfo(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    private void addError(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public Badge getSelectedBadge() {
        return selectedBadge;
    }

    public void setSelectedBadge(Badge selectedBadge) {
        this.selectedBadge = selectedBadge;
    }

    public String getNewBadgeContent() {
        return newBadgeContent;
    }

    public void setNewBadgeContent(String newBadgeContent) {
        this.newBadgeContent = newBadgeContent;
    }
}
