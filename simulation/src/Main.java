import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(windowEvent -> closeWindow());
        new SettingsController().launchScene(primaryStage);
    }

    private void closeWindow() {
        Platform.exit();
        System.exit(0);
    }

}
