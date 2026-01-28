package lml.snir.parkinglogickit.client.beans.navgestion;

import lml.snir.parkinglogickit.client.beans.comptegestion.LoggedType;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author sylvain
 */
@Named
@ViewScoped
public enum Page {
    Accueil(
        0,
        "/accueil.xhtml"
    ),
    Separateur1(
            "|",
            1,
            "",
            Arrays.asList(
                    LoggedType.Affichage
            )
    ),
    Connexion(
        2,
        "/compte/login.xhtml",
        Arrays.asList(
                LoggedType.LoggedOutOnly
        )
    ),
    Consignes(
        2,
        "/consignesView.xhtml",
        Arrays.asList(LoggedType.LoggedInOnly)
    ),
    Temperatures(
        3,
        "/temperaturesView.xhtml",
        Arrays.asList(LoggedType.LoggedInOnly)
    ),
    Separateur2(
            "|",
            4,
            "",
            Arrays.asList(
                    LoggedType.Affichage,
                    LoggedType.LoggedInOnly
            )
    ),
    Compte(
        5,
        "/compte/compte.xhtml",
        Arrays.asList(
                LoggedType.LoggedInOnly
        )
    ),
    
    Utilisateurs(
        6,
        "/admin/user_gestion.xhtml",
        Arrays.asList(
                LoggedType.LoggedInOnly,
                LoggedType.AdminOnly
        )
    ),
    Locaux(
        7,
        "/admin/local_gestion.xhtml",
        Arrays.asList(
                LoggedType.LoggedInOnly,
                LoggedType.AdminOnly
        )
    ),
    Badges(
        8,
        "/admin/badges_gestion.xhtml",
        Arrays.asList(
                LoggedType.LoggedInOnly,
                LoggedType.AdminOnly
        )
    ),
    Attributions(
        9,
        "/admin/attributions_gestion.xhtml",
        Arrays.asList(
                LoggedType.LoggedInOnly,
                LoggedType.AdminOnly
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
    
    public boolean verifLoggedType(LoggedType type){
        return this.getLoggedTypes().contains(type);
    }
    
}