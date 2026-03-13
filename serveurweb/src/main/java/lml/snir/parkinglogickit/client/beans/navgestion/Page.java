package lml.snir.parkinglogickit.client.beans.navgestion;

import lml.snir.parkinglogickit.client.beans.comptegestion.LoggedType;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public enum Page {
    Accueil(
            0,
            "/accueil.xhtml"
    ),
    Dashboard(
            1,
            "/dashboard.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            )
    ),
    Journal(
            2,
            "/journal.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            )
    ),
    Separateur1(
            "|",
            3,
            "",
            Arrays.asList(
                    LoggedType.Affichage
            )
    ),
    Conducteurs(
            4,
            "/admin/conducteurs.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Vehicules(
            5,
            "/admin/vehicules.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Badges(
            6,
            "/admin/badges.xhtml",
            Arrays.asList(
                    LoggedType.AdminOnly
            )
    ),
    Separateur2(
            "|",
            7,
            "",
            Arrays.asList(
                    LoggedType.Affichage,
                    LoggedType.LoggedInOnly
            )
    ),
    Connexion(
            8,
            "/compte/login.xhtml",
            Arrays.asList(
                    LoggedType.LoggedOutOnly
            )
    ),
    Compte(
            8,
            "/compte/compte.xhtml",
            Arrays.asList(
                    LoggedType.LoggedInOnly
            )
    ),;

    private final String nom;
    private final Integer id;
    private final String path;
    private final List<LoggedType> loggedTypes;

    private Page(Integer id, String path) {
        this.nom = this.name();
        this.id = id;
        this.path = path;
        this.loggedTypes = new ArrayList<>();
    }

    private Page(String nom, Integer id, String path) {
        this.nom = nom;
        this.id = id;
        this.path = path;
        this.loggedTypes = new ArrayList<>();
    }

    private Page(Integer id, String path, List<LoggedType> loggedTypes) {
        this.nom = this.name();
        this.id = id;
        this.path = path;
        this.loggedTypes = loggedTypes;
    }

    private Page(String nom, Integer id, String path, List<LoggedType> loggedTypes) {
        this.nom = nom;
        this.id = id;
        this.path = path;
        this.loggedTypes = loggedTypes;
    }

    public Integer getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getNom() {
        return nom;
    }

    public List<LoggedType> getLoggedTypes() {
        return loggedTypes;
    }

    public boolean verifLoggedType(LoggedType type) {
        return this.getLoggedTypes().contains(type);
    }

}
