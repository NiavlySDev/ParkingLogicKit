package lml.snir.parkinglogickit.client.beans.navgestion;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lml.snir.parkinglogickit.client.beans.AssociationsBean;
import lml.snir.parkinglogickit.client.beans.BadgesBean;
import lml.snir.parkinglogickit.client.beans.ConducteursBean;
import lml.snir.parkinglogickit.client.beans.DashboardBean;
import lml.snir.parkinglogickit.client.beans.JournalBean;
import lml.snir.parkinglogickit.client.beans.ProfilBean;
import lml.snir.parkinglogickit.client.beans.VehiculesBean;
import lml.snir.parkinglogickit.client.beans.comptegestion.LoggedType;
import lml.snir.parkinglogickit.client.beans.comptegestion.LoginBean;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 * Bean chargé de construire le menu principal de l'application. Les onglets
 * sont créés à partir de l'enum Page afin de centraliser la navigation et de
 * rendre l'ordre des pages plus simple à maintenir.
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class NavBean implements Serializable {

    @Inject
    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    @Inject
    private DashboardBean dashboardBean;

    @Inject
    private JournalBean journalBean;

    @Inject
    private ConducteursBean conducteursBean;

    @Inject
    private VehiculesBean vehiculesBean;

    @Inject
    private BadgesBean badgesBean;

    @Inject
    private AssociationsBean associationsBean;

    @Inject
    private ProfilBean profilBean;

    private MenuModel model;
    private String path;
    private int activeIndex = 0;
    private Page currentPage = Page.Accueil;
    private List<Page> visiblePages = new ArrayList<>();

    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        path = currentPage.getPath();
        visiblePages = getVisiblePages();

        for (Page page : visiblePages) {
            DefaultMenuItem item;
            if (page.isSeparator()) {
                item = DefaultMenuItem.builder()
                        .id(page.name().toLowerCase())
                        .value(page.getNom())
                        .ajax(true)
                        .process("@none")
                        .update("index:fragmentPanel messageindex message index:tabMenu")
                        .disabled(true)
                        .build();
            } else {
                item = DefaultMenuItem.builder()
                        .id(page.name().toLowerCase())
                        .value(page.getNom())
                        .ajax(true)
                        .process("@this")
                        .update("index:fragmentPanel messageindex message index:tabMenu")
                        .command("#{navBean.onTabChange('" + page.name() + "')}")
                        .disabled(false)
                        .build();
            }
            model.getElements().add(item);
        }

        activeIndex = calculerIndexActif(currentPage);
    }

    /**
     * Change le fragment affiché quand l'utilisateur clique sur un onglet.
     *
     * @param pageName nom de la page dans l'enum Page
     */
    public void onTabChange(String pageName) {
        Page page = Page.valueOf(pageName);
        this.currentPage = page;
        this.path = page.getPath();
        this.activeIndex = calculerIndexActif(page);
    }

    /**
     * Recharge toutes les 30 secondes les données de la page affichée.
     */
    public void refreshCurrentPage() {
        switch (currentPage) {
            case Dashboard:
                dashboardBean.charger();
                break;
            case Journal:
                journalBean.charger();
                break;
            case Conducteurs:
                conducteursBean.charger();
                break;
            case Vehicules:
                vehiculesBean.charger();
                break;
            case Badges:
                badgesBean.charger();
                break;
            case Associations:
                associationsBean.charger();
                break;
            case Compte:
                profilBean.charger();
                break;
            default:
                break;
        }
    }

    /**
     * Limite le rendu Ajax aux zones utiles pour ne pas recréer les dialogues.
     *
     * @return identifiants JSF mis à jour par le rafraîchissement automatique
     */
    public String getRefreshUpdateTargets() {
        switch (currentPage) {
            case Dashboard:
                return "index:dashboardStats index:parkingInfo index:messageindex index:message";
            case Journal:
                return "index:tableauJournal index:messageindex index:message";
            case Conducteurs:
                return "index:tableConducteurs index:messageindex index:message";
            case Vehicules:
                return "index:tableVehicules index:messageindex index:message";
            case Badges:
                return "index:tableBadges index:messageindex index:message";
            case Associations:
                return "index:tableAssociations index:messageindex index:message";
            default:
                return "index:fragmentPanel index:messageindex index:message";
        }
    }

    public MenuModel getModel() {
        return model;
    }

    public String getPath() {
        return path;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    /**
     * Retourne les pages affichées sous forme de cartes sur l'accueil.
     * Les séparateurs et la page Accueil sont exclus pour ne garder que les
     * destinations utiles à l'utilisateur connecté.
     *
     * @return pages accessibles depuis l'accueil
     */
    public List<Page> getAccueilPages() {
        List<Page> pages = new ArrayList<>();
        for (Page page : visiblePages) {
            if (page.isSeparator() || page == Page.Accueil) {
                continue;
            }
            pages.add(page);
        }
        return pages;
    }

    private List<Page> getVisiblePages() {
        List<Page> pages = new ArrayList<>();
        for (Page page : Page.values()) {
            if (!peutAfficher(page) || page == Page.Compte) {
                continue;
            }
            pages.add(page);
        }
        return pages;
    }

    private boolean peutAfficher(Page page) {
        if (page.isDisabled()) {
            return false;
        }
        if (page.verifLoggedType(LoggedType.AdminOnly) && (!loginBean.isLogged() || !loginBean.isAdmin())) {
            return false;
        }
        if (page.verifLoggedType(LoggedType.LoggedInOnly) && !loginBean.isLogged()) {
            return false;
        }
        return !(page.verifLoggedType(LoggedType.LoggedOutOnly) && loginBean.isLogged());
    }

    private int calculerIndexActif(Page pageActive) {
        for (int i = 0; i < visiblePages.size(); i++) {
            Page page = visiblePages.get(i);
            if (page.getActiveGroup().equals(pageActive.getActiveGroup())) {
                return i;
            }
        }
        return -1;
    }
}
