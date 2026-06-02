package lml.snir.parkinglogickit.client.beans.navgestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lml.snir.parkinglogickit.client.beans.comptegestion.LoggedType;

/**
 * Enumération des pages disponibles dans le menu principal. Le but est de
 * pouvoir déplacer ou désactiver une page sans devoir recalculer tous les index
 * à la main dans le reste du projet. Pour masquer une page depuis le code, il
 * suffit d'utiliser un constructeur avec le paramètre disabled à true.
 *
 * @author Sylvain Crocquevieille
 */
public enum Page {
    Accueil(
            "/pages/public/accueil/accueil.xhtml"
    ),
    Dashboard(
            "/pages/dashboard/dashboard.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            )
    ),
    Journal(
            "/pages/journal/journal.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            ),
            true // Disabled
    ),
    Separateur1(
            "|",
            "",
            Arrays.asList(
                    LoggedType.Affichage
            ),
            true
    ),
    Conducteurs(
            "/pages/admin/conducteurs/conducteurs.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Vehicules(
            "/pages/admin/vehicules/vehicules.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Badges(
            "/pages/admin/badges/badges.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Associations(
            "/pages/admin/associations/associations.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Separateur2(
            "|",
            "",
            Arrays.asList(
                    LoggedType.Affichage,
                    LoggedType.LoggedInOnly
            ),
            true
    ),
    Connexion(
            "/pages/compte/login/login.xhtml",
            Arrays.asList(
                    LoggedType.LoggedOutOnly
            ),
            false,
            "Compte"
    ),
    Compte(
            "Mon profil",
            "/pages/compte/compte/compte.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            ),
            false,
            "Compte"
    ),
    Informations(
            "/pages/compte/informations/informations.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            )
    ),
    Guide(
            "/pages/public/guide/guide.xhtml"
    ),
    Themes(
            "/pages/compte/themes/themes.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            )
    ),;

    private final String nom;
    private final String path;
    private final List<LoggedType> loggedTypes;
    private final boolean separator;
    private final boolean disabled;
    private final String activeGroup;

    /**
     * Construit une instance de Page.
     *
     * @param path : paramètre utilisé par la méthode
     */
    private Page(String path) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = new ArrayList<>();
        this.separator = false;
        this.disabled = false;
        this.activeGroup = this.name();
    }

    /**
     * Construit une instance de Page.
     *
     * @param path : paramètre utilisé par la méthode
     * @param disabled : paramètre utilisé par la méthode
     */
    private Page(String path, boolean disabled) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = new ArrayList<>();
        this.separator = false;
        this.disabled = disabled;
        this.activeGroup = this.name();
    }

    /**
     * Construit une instance de Page.
     *
     * @param path : paramètre utilisé par la méthode
     * @param loggedTypes : paramètre utilisé par la méthode
     */
    private Page(String path, List<LoggedType> loggedTypes) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = false;
        this.disabled = false;
        this.activeGroup = this.name();
    }

    /**
     * Construit une instance de Page.
     *
     * @param path : paramètre utilisé par la méthode
     * @param loggedTypes : paramètre utilisé par la méthode
     * @param disabled : paramètre utilisé par la méthode
     */
    private Page(String path, List<LoggedType> loggedTypes, boolean disabled) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = false;
        this.disabled = disabled;
        this.activeGroup = this.name();
    }

    /**
     * Construit une instance de Page.
     *
     * @param path : paramètre utilisé par la méthode
     * @param loggedTypes : paramètre utilisé par la méthode
     * @param disabled : paramètre utilisé par la méthode
     * @param activeGroup : paramètre utilisé par la méthode
     */
    private Page(String path, List<LoggedType> loggedTypes, boolean disabled, String activeGroup) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = false;
        this.disabled = disabled;
        this.activeGroup = activeGroup;
    }

    /**
     * Construit une instance de Page.
     *
     * @param nom : paramètre utilisé par la méthode
     * @param path : paramètre utilisé par la méthode
     * @param loggedTypes : paramètre utilisé par la méthode
     * @param disabled : paramètre utilisé par la méthode
     * @param activeGroup : paramètre utilisé par la méthode
     */
    private Page(String nom, String path, List<LoggedType> loggedTypes, boolean disabled, String activeGroup) {
        this.nom = nom;
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = false;
        this.disabled = disabled;
        this.activeGroup = activeGroup;
    }

    /**
     * Construit une instance de Page.
     *
     * @param nom : paramètre utilisé par la méthode
     * @param path : paramètre utilisé par la méthode
     * @param loggedTypes : paramètre utilisé par la méthode
     * @param separator : paramètre utilisé par la méthode
     */
    private Page(String nom, String path, List<LoggedType> loggedTypes, boolean separator) {
        this.nom = nom;
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = separator;
        this.disabled = false;
        this.activeGroup = this.name();
    }

    /**
     * Retourne nom.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne path.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getPath() {
        return path;
    }

    /**
     * Ancien accesseur conservé pour éviter les erreurs lors d'un redéploiement
     * si une ancienne version de NavBean est encore chargée par le serveur.
     *
     * @return position de la page dans l'enum
     */
    public Integer getId() {
        return this.ordinal();
    }

    /**
     * Retourne logged types.
     *
     * @return List<LoggedType> : valeur retournée par la méthode
     */
    public List<LoggedType> getLoggedTypes() {
        return loggedTypes;
    }

    /**
     * Indique si separator.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isSeparator() {
        return separator;
    }

    /**
     * Indique si disabled.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Retourne active group.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getActiveGroup() {
        return activeGroup;
    }

    /**
     * Retourne icon.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getIcon() {
        switch (this) {
            case Guide:
                return "pi pi-book";
            case Dashboard:
                return "pi pi-chart-line";
            case Conducteurs:
                return "pi pi-users";
            case Vehicules:
                return "pi pi-car";
            case Badges:
                return "pi pi-id-card";
            case Associations:
                return "pi pi-link";
            case Connexion:
                return "pi pi-sign-in";
            case Compte:
                return "pi pi-user";
            case Informations:
                return "pi pi-cog";
            case Themes:
                return "pi pi-palette";
            default:
                return "pi pi-home";
        }
    }

    /**
     * Retourne description.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getDescription() {
        switch (this) {
            case Guide:
                return "Consulter le guide complet d'utilisation de ParkingLogicKit.";
            case Dashboard:
                return "Consulter les places disponibles et l'état des parkings.";
            case Conducteurs:
                return "Gérer les conducteurs et leurs informations de connexion.";
            case Vehicules:
                return "Ajouter, modifier ou supprimer les véhicules enregistrés.";
            case Badges:
                return "Créer et suivre les badges RFID disponibles.";
            case Associations:
                return "Associer un conducteur à un badge et à un véhicule.";
            case Connexion:
                return "Se connecter pour accéder aux fonctionnalités du parking.";
            case Compte:
                return "Consulter votre profil, vos badges et vos véhicules personnels.";
            case Informations:
                return "Afficher les informations du Projet.";
            case Themes:
                return "Choisir un thème de couleurs pour personnaliser l'application.";
            default:
                return "Retourner à la page d'accueil de ParkingLogicKit.";
        }
    }

    /**
     * Indique si la page demande un type de connexion particulier.
     *
     * @param type règle d'affichage à vérifier
     * @return true si la règle est présente sur la page
     */
    public boolean verifLoggedType(LoggedType type) {
        return this.getLoggedTypes().contains(type);
    }

}
