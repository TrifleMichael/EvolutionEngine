import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class BoardController {
    private Stage stage;
    private Scene scene;
    private Parent parent;

    @FXML
    private GridPane boardGrid;

    public BoardController() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/SimulationGrid.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = (Parent) fxmlLoader.load();
            // set height and width here for this login scene
            scene = new Scene(parent, 1000, 800);
        } catch (IOException ex) {
            System.out.println("Error displaying GUI");
            throw new RuntimeException(ex);
        }
    }

    public void launchScene(Stage stage){
            this.stage = stage;
            stage.setScene(scene);
            stage.setResizable(false);
            stage.hide();
            stage.show();
    }
}
