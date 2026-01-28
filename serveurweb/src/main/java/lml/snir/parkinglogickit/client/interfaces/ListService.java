package lml.snir.parkinglogickit.client.interfaces;

import jakarta.faces.component.UIComponent;

/**
 * 
 * @author sylvain
 */
public interface ListService {
    
    default UIComponent getUIComponentOfId(UIComponent root, String id) {   
        String rootId = root.getId();
        if (rootId.equals(id)) {
            return root;
        }
        if (root.getChildCount() > 0) {
            for (UIComponent subUiComponent : root.getChildren()) {
                UIComponent returnComponent = getUIComponentOfId(subUiComponent, id);
                if (returnComponent != null) {
                    return returnComponent;
                }
            }
        }
        return null;
    }
}
