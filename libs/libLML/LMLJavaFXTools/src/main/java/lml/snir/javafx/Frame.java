package lml.snir.javafx;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author fanou
 */
public class Frame {
    private static Scene scene;
    private static Stage stage;
    public static List<Object> arguments;

    public static Scene createScene(String fxmlFile, int sizeX, int sizeY, String cssFile, Class clazz) throws IOException {
        System.err.println("Loading FXML for main view from : " + fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(clazz.getResourceAsStream(fxmlFile));

        System.err.println("Showing JFX scene");
        Frame.scene = new Scene(rootNode, sizeX, sizeY);
        if (cssFile != null) {
            Frame.scene.getStylesheets().add(cssFile);
        }

        return Frame.scene;
    }
    
    public static void show() {
        Frame.getMainStage().setScene(Frame.getScene());
        Frame.getMainStage().show();
    }
    
    public static Scene getScene() {
        return Frame.scene;
    }
    
    public static void setMainStage(Stage stage) {
        Frame.stage = stage;
    }
    
    public static Stage getMainStage() {
        return Frame.stage;
    }
}
