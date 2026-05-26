package lml.snir.parkinglogickit.client.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 * @author Sylvain Crocquevieille
 */
@Named
@ViewScoped
public class JournalBean implements Serializable {

    private List<Access> acces = new ArrayList<>();

    @PostConstruct
    public void init() {
        charger();
    }

    public void charger() {
        try {
            acces = MetierFactory.getAccessService().getAll();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erreur chargement journal : " + e.getMessage(), null));
        }
    }

    public void refresh() {
        charger();
    }

    public List<Access> getAcces() {
        return acces;
    }
}
