import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {
    private Stage stage;
    private Scene scene;
    private Parent parent;

    @FXML
    private TextField boardSize;

    @FXML
    private TextField mutationStandardDeviation;
    @FXML
    private TextField satietyLostPerIteration;
    @FXML
    private TextField satietyLostOnBirth;
    @FXML
    private TextField satietyRequiredForBirth;
    @FXML
    private TextField startingSatiety;

    public SettingsController() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/Settings.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = (Parent) fxmlLoader.load();
            // set height and width here for this login scene
            scene = new Scene(parent, 500, 400);
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
    @FXML
    private void handleStartClick(ActionEvent event) throws Exception {
        Settings.setMutationStandardDeviation(Double.parseDouble(mutationStandardDeviation.getText()));
        Settings.setSatietyLostPerIteration(Double.parseDouble(satietyLostPerIteration.getText()));
        Settings.setSatietyLostOnBirth(Double.parseDouble(satietyLostOnBirth.getText()));
        Settings.setSatietyRequiredForBirth(Double.parseDouble(satietyRequiredForBirth.getText()));
        Settings.setStartingSatiety(Double.parseDouble(startingSatiety.getText()));
        new BoardController().launchScene(stage);
    }
}
