package lml.snir.javafx;

import javafx.stage.Stage;

/**
 *
 * @author fanou
 */
public interface AddEditDlgControllable {
    public void setModelData(LMLModel model);
    public LMLModel getModelData();
    public void setDialogStage(Stage dlgStage);
    public boolean isOkClicked();    
}
