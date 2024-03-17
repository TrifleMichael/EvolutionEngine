import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class BoardController {
    private Stage stage;
    private Scene scene;
    private Parent parent;
    private SimulationEngine simulation;
    Thread simulationThread;
    @FXML
    private GridPane boardGrid;

    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button stepButton;

    @FXML
    private Button endButton;



    public BoardController() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/SimulationGrid.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = (Parent) fxmlLoader.load();
            // set height and width here for this login scene
            scene = new Scene(parent, 1000, 800);

            simulation = new SimulationEngine(this);

            simulationThread = new Thread(simulation);
            simulationThread.start();
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

    public void hasChanges(SimulationManager simulationManager){
        Grid grid = simulationManager.grid;
        ArrayList<Genome> genomes = simulationManager.getGenomes();
        drawGrid(grid);
    }

    private void drawGrid(Grid grid){
        boardGrid.getChildren().clear();
        for (int i = 0; i < Settings.boardSize; i++) {
            for (int j = 0; j < Settings.boardSize;  j++) {
                HBox box = createField(grid.cells[j][i], i, j);
                boardGrid.add(box, i, j);
            }
        }
    }

    private HBox createField(Cell cell, int i, int j){
        String cellAsString = cellToString(cell);
        Label label = new Label(cellAsString);
        HBox box = new HBox(label);
        box.getStyleClass().add("gridField");
        box.setPrefWidth((double) 900/(double) Settings.boardSize);
        box.setPrefHeight((double) 700/(double) Settings.boardSize);
        return box;

    }

    static String cellToString(Cell cell) {
        return cell.animals.size() + " " + cell.plants.size();
    }

    @FXML
    private void handleStartClick(ActionEvent event) throws Exception {
        simulation.start();
        startButton.setDisable(true);
        stepButton.setDisable(true);
        stopButton.setDisable(false);
    }
    @FXML
    private void handleStopClick(ActionEvent event) throws Exception {
        simulation.stop();
        startButton.setDisable(false);
        stepButton.setDisable(false);
        stopButton.setDisable(true);
    }
    @FXML
    private void handleStepClick(ActionEvent event) throws Exception {
        simulation.step();
    }

    @FXML
    private void handleEndClick(ActionEvent event) throws Exception {
        simulation.end();
        startButton.setDisable(true);
        stepButton.setDisable(true);
        stopButton.setDisable(true);
        endButton.setDisable(true);
    }
    public void stopSimulation() {
        startButton.setDisable(true);
        stepButton.setDisable(false);
        stopButton.setDisable(true);
    }

}
