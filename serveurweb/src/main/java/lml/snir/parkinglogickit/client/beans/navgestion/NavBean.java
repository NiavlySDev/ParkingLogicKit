package lml.snir.parkinglogickit.client.beans.navgestion;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import org.primefaces.model.menu.MenuModel;

/**
 * 
 * @author sylvain
 */
@Named
@ViewScoped
public class NavBean implements Serializable {
    
//    @Inject 
//    @ManagedProperty("#{loginBean}")
//    private LoginBean loginBean;

    private MenuModel model;
    private String path;
    private int activeIndex = 0;

//    @PostConstruct
//    public void init() {
//        model = new DefaultMenuModel();
//        path = "/accueil.xhtml";
//
//        for(Page page : Page.values()){
//            if(page.verifLoggedType(LoggedType.AdminOnly)){
//                if(loginBean.isLogged()){
//                    if(!(loginBean.getUser() instanceof Administrator)){
//                        continue;
//                    }
//                }
//                else{
//                    continue;
//                }
//            }
//            if(page.verifLoggedType(LoggedType.LoggedInOnly)){
//                if(!loginBean.isLogged()){
//                    continue;
//                }
//            }
//            if(page.verifLoggedType(LoggedType.LoggedOutOnly)){
//                if(loginBean.isLogged()){
//                    continue;
//                }
//            }
//            DefaultMenuItem item;
//            if(page.verifLoggedType(LoggedType.Affichage)){
//                item = DefaultMenuItem.builder()
//                    .id(page.name().toLowerCase())
//                    .value(page.getNom())
//                    .ajax(true)
//                    .update("index:fragmentPanel messageindex message index:tabMenu")
//                    .command("#{navBean.onTabChange(" + page.getId() + ", '" + page.getPath() + "')}")
//                    .disabled(true)
//                    .build();
//            }
//            else{
//                item = DefaultMenuItem.builder()
//                    .id(page.name().toLowerCase())
//                    .value(page.name())
//                    .ajax(true)
//                    .update("index:fragmentPanel messageindex message index:tabMenu")
//                    .command("#{navBean.onTabChange(" + page.getId() + ", '" + page.getPath() + "')}")
//                    .disabled(false)
//                    .build();
//            }
//            model.getElements().add(item);
//        }
//    }

    public void onTabChange(int index, String newPath) {
        this.activeIndex = index;
        this.path = newPath;
    }

    public MenuModel getModel() { return model; }
    public String getPath() { return path; }
    public int getActiveIndex() { return activeIndex; }
}