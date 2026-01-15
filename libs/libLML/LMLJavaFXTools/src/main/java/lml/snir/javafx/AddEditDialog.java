package lml.snir.javafx;

import java.lang.reflect.ParameterizedType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author fanou
 * @param <T>
 */
public abstract class AddEditDialog<T> implements AddEditDlgControllable {
    protected Stage dialogStage;

    protected T data;
    private Class<T> clazz;
    protected boolean okClicked;

    public AddEditDialog() {
       System.out.print("lml.snir.javafx.AddEditDialog.<init>() for ");

        try {
            ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
            String cl = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

            Class c = Class.forName(cl);
            this.clazz = (Class<T>) c;

        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createDialogAddEdit(LMLModel model, Class main, String fxmlUrl, Stage owner) throws Exception {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(main.getResource(fxmlUrl));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();

        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(owner);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddEditDlgControllable controller = loader.getController();
        AddEditDialog dlg = (AddEditDialog) controller;
        controller.setDialogStage(dialogStage);

        if (model != null) {
            dialogStage.setTitle("Edit " + this.clazz.getSimpleName());
            controller.setModelData(model);
        } else {
            dialogStage.setTitle("Add " + this.clazz.getSimpleName());
        }

        try {
            dialogStage.showAndWait();
        } catch (Exception ex) {

        }

        this.data = (T) dlg.data;
    }

    public T getData() {
        return this.data;
    }
    
    @Override
    public void setDialogStage(Stage dlgStage) {
        this.dialogStage = dlgStage;
    }
    
    @Override
    public boolean isOkClicked() {
        return this.okClicked;
    }
}
