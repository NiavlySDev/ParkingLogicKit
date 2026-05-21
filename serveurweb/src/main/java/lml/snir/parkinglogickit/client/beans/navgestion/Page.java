package lml.snir.parkinglogickit.client.beans.navgestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lml.snir.parkinglogickit.client.beans.comptegestion.LoggedType;

/**
 * Enumération des pages disponibles dans le menu principal.
 * Le but est de pouvoir déplacer ou désactiver une page sans devoir recalculer
 * tous les index à la main dans le reste du projet.
 * Pour masquer une page depuis le code, il suffit d'utiliser un constructeur
 * avec le paramètre disabled à true.
 *
 * @author Sylvain Crocquevieille
 */
public enum Page {
    Accueil(
            "/accueil.xhtml"
    ),
    Dashboard(
            "/dashboard.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            )
    ),
//    Journal(
//            "/journal.xhtml",
//            Arrays.asList(
//                    LoggedType.LoggedInOnly
//            )
//    ),
    Separateur1(
            "|",
            "",
            Arrays.asList(
                    LoggedType.Affichage
            ),
            true
    ),
    Conducteurs(
            "/admin/conducteurs.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Vehicules(
            "/admin/vehicules.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Badges(
            "/admin/badges.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Associations(
            "/admin/associations.xhtml",
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
            "/compte/login.xhtml",
            Arrays.asList(
                    LoggedType.LoggedOutOnly
            ),
            false,
            "Compte"
    ),
    Compte(
            "/compte/compte.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            ),
            false,
            "Compte"
    ),;

    private final String nom;
    private final String path;
    private final List<LoggedType> loggedTypes;
    private final boolean separator;
    private final boolean disabled;
    private final String activeGroup;

    private Page(String path) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = new ArrayList<>();
        this.separator = false;
        this.disabled = false;
        this.activeGroup = this.name();
    }

    private Page(String path, boolean disabled) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = new ArrayList<>();
        this.separator = false;
        this.disabled = disabled;
        this.activeGroup = this.name();
    }

    private Page(String path, List<LoggedType> loggedTypes) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = false;
        this.disabled = false;
        this.activeGroup = this.name();
    }

    private Page(String path, List<LoggedType> loggedTypes, boolean disabled) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = false;
        this.disabled = disabled;
        this.activeGroup = this.name();
    }

    private Page(String path, List<LoggedType> loggedTypes, boolean disabled, String activeGroup) {
        this.nom = this.name();
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = false;
        this.disabled = disabled;
        this.activeGroup = activeGroup;
    }

    private Page(String nom, String path, List<LoggedType> loggedTypes, boolean separator) {
        this.nom = nom;
        this.path = path;
        this.loggedTypes = loggedTypes;
        this.separator = separator;
        this.disabled = false;
        this.activeGroup = this.name();
    }

    public String getNom() {
        return nom;
    }

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

    public List<LoggedType> getLoggedTypes() {
        return loggedTypes;
    }

    public boolean isSeparator() {
        return separator;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getActiveGroup() {
        return activeGroup;
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
