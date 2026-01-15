package lml.snir.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import lml.snir.persistence.CrudService;

/**
 *
 * @author (c) 2025 ALONSO Stéphane LML
 * @param <T>
 */
public abstract class AbstractJFXTableModel<T> extends Observable implements TableService<T>{

    private final TableView table;
    protected String[] header;
    protected String[] propertys;
    protected CrudService service;
    private int nbPerPage = 5; // default value
    private Pagination pagination;
    private final List<T> datas = new ArrayList<>();
    private LMLModel<T> model;
    private final Class clazz;
    private TableService<T> tableService;

    public AbstractJFXTableModel(TableView table, Class clazz) {
        this.table = table;
        this.table.getColumns().clear();
        this.clazz = clazz;
    }

    @Override
    public final long getCount() throws Exception {
        long count;
        if (this.tableService != null) {
            count = this.tableService.getCount();
        } else {
            count = this.service.getCount();
        }
        
        return count;
    }
    
    @Override
    public final List<T> getAll() throws Exception {
        List<T> theData;
        if (this.tableService != null) {
            theData = this.tableService.getAll();
        } else {
            theData = this.service.getAll();
        }
        return theData;
    }
    
    @Override
    public final List<T> getAll(int begin, int count) throws Exception {
        List<T> theData;
        if (this.tableService != null) {
            theData = this.tableService.getAll(begin, count);
        } else {
            theData = this.service.getAll(begin, count);
        }
        return theData;
    }
    
    public final void init() throws Exception {
        if (this.propertys == null) {
            T t = (T) this.clazz.newInstance();
            this.model = (LMLModel) t;
            this.propertys = this.model.getPropertys();
        }

        if (this.header == null) {
            System.err.println("AbstractTableModel.init() : header not set using propertys values");
            this.header = this.propertys;
        }

        if (this.header.length != this.propertys.length) {
            throw new Exception("AbstractTableModel.init() : header and propertys must have same length");
        }

//        if (this.service == null) {
//            throw new Exception("AbstractTableModel.init() : service not set");
//        }

        int index = 0;
        for (String name : header) {
            TableColumn<T, String> col = new TableColumn<>(name);
            col.setCellValueFactory(new PropertyValueFactory<>(propertys[index++]));
            table.getColumns().add(col);
        }

        if (this.pagination != null) {
            long count = this.getCount();
            int nbPage = (int) (count / this.getNbPerPage()) + 1;
            if (this.getPagination() != null) {
                this.getPagination().setPageCount(nbPage);
                this.getPagination().setCurrentPageIndex(0);
                this.getPagination().setPageFactory(this::createPage);
            }
        } else {
            List objects = this.getAll();
            for (Object o : objects) {
                T t = (T) this.clazz.newInstance();
                this.model = (LMLModel) t;
                this.model.setObjectModel((T) o);
                this.datas.add(t);
            }
            table.setItems(FXCollections.observableArrayList(this.datas));
        }
        TableViewSelectionModel<T> selectionModel = this.table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<T> selectedItems = selectionModel.getSelectedItems();
        selectedItems.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> change) {
                setChanged();
                int index = table.getSelectionModel().getSelectedIndex();
                notifyObservers((Integer) index);
            }
        });
    }
    
    //protected abstract T getSelected();

    public final Node createPage(int pageIndex) {
        try {
            int fromIndex = pageIndex * this.getNbPerPage();
            this.datas.clear();
            List objects = this.getAll(fromIndex, this.getNbPerPage());
            for (Object o : objects) {
                T t = (T) this.clazz.newInstance();
                this.model = (LMLModel) t;
                this.model.setObjectModel((T) o);
                this.datas.add(t);
            }
            table.setItems(FXCollections.observableArrayList(this.datas));
        } catch (Exception ex) {
            Logger.getLogger(AbstractJFXTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return table;
    }

    /**
     * @return the service
     */
    public CrudService getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(CrudService service) {
        this.service = service;
    }

    /**
     * @return the nbPerPage
     */
    public int getNbPerPage() {
        return nbPerPage;
    }

    /**
     * @param nbPerPage the nbPerPage to set
     */
    public void setNbPerPage(int nbPerPage) {
        this.nbPerPage = nbPerPage;
    }

    /**
     * @return the pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * @param pagination the pagination to set
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public void update(List objects) {
        this.datas.clear();
        for (Object o : objects) {
            try {
                T t = (T) this.clazz.newInstance();
                this.model = (LMLModel) t;
                this.model.setObjectModel((T) o);
                this.datas.add(t);
                table.setItems(FXCollections.observableArrayList(this.datas));
            } catch (InstantiationException ex) {
                Logger.getLogger(AbstractJFXTableModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(AbstractJFXTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    /**
     * @return the tableService
     */
    public TableService<T> getTableService() {
        return tableService;
    }

    /**
     * @param tableService the tableService to set
     */
    public void setTableService(TableService<T> tableService) {
        this.tableService = tableService;
    }
}
