package lml.snir.javafx;

//import com.sun.javafx.beans.event.AbstractNotifyListener;
import javafx.scene.control.Pagination;

/**
 *
 * @author (c) 2021 S.ALONSO LML
 */
public abstract class LMLController /*extends AbstractNotifyListener*/ {
    protected Pagination pagination;
    public final void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
    
    public abstract void init() throws Exception ;
    public abstract void add() throws Exception ;
    public abstract void update() throws Exception ;
    public abstract void remove() throws Exception ;
    public abstract void search(Object criteria) throws Exception ;
    public void export() throws Exception {
        // do nothing
    }
}
