
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Set;

import static java.lang.Thread.sleep;

public class SimulationEngine implements Runnable{

    private  SimulationManager simulationManager;
    private Grid grid;
    private ArrayList<Genome> genomes = new ArrayList<>();
    private BoardController boardController;
    private volatile boolean isRunning = false;
    private volatile boolean oneStepOnly = false;
    private volatile boolean endSimulation = false;
//    private final IntegerProperty refresh = new SimpleIntegerProperty(0);
    private int step = 0;


    public SimulationEngine(BoardController boardController) {
        this.boardController = boardController;
    }

    @Override
    public void run() {
        initialize();
        simulation();
    }
    private void simulation(){
//        try {
            Platform.runLater(() -> boardController.hasChanges(simulationManager));
            for(;;) {
                if (!isRunning&&!endSimulation)
                    continue;
                if(endSimulation)
                    break;
                simulationStep();
                step += 1;
                if(oneStepOnly) {
                    oneStepOnly = false;
                    isRunning = false;
                    Platform.runLater(()->boardController.hasChanges(simulationManager));
                }
                if(step%10==0)
                    Platform.runLater(() -> boardController.hasChanges(simulationManager));
                if(step >= 10000)

                {
                    Platform.runLater(() -> {
                        boardController.hasChanges(simulationManager);
                        boardController.stopSimulation();
                    });
                    break;
                }

//                sleep(500);
            }
            for(;;) {
                if(endSimulation)
                    break;
                if(oneStepOnly) {
                    oneStepOnly = false;
                    simulationStep();
                    step+=1;
                    Platform.runLater(() -> boardController.hasChanges(simulationManager));
//                    refresh.setValue(0);
                }
            }
//            refresh.setValue(0);
            Platform.runLater(() -> boardController.hasChanges(simulationManager));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
    protected void initialize() {
        grid =new Grid(Settings.boardSize, Settings.boardSize);
        simulationManager = new SimulationManager(grid);
        //TODO Add all parameters to the settings screen.
        for (int i = 0; i < 30; i++) {
            genomes.add(new Genome(0.1, 0.1, 0.1, 0.1, 0.5));
        }
        simulationManager.populateGrid(genomes);
        simulationManager.addPlants(Settings.initialPlantsNo);
    }

    protected void simulationStep(){
        simulationManager.iterate();
        simulationManager.addPlants(2);

        System.out.println("----- Iteration:" + step + " ------");
        printGridInConsole(simulationManager);
        var newGenomes = simulationManager.getGenomes();
        System.out.println("Number of animals: "+newGenomes.size());
        System.out.println("Number of plants: "+simulationManager.getPlants().size());
        System.out.println("Average digestion: "+ averageDigestion(newGenomes));
    }

    public void start(){
        isRunning=true;
    }
    public void stop(){
        isRunning=false;
    }
    public void step(){
        isRunning=true;
        oneStepOnly=true;
    }
    public void end(){
        endSimulation = true;
    }



    static double averageDigestion(ArrayList<Genome> genomes) {
        double avgDigestion = 0;
        for(var genome : genomes) {
            avgDigestion += genome.geneticCode.get("digestion");
        }
        avgDigestion /= genomes.size();
        return avgDigestion;
    }

    static void printGridInConsole(SimulationManager simulationManager) {
        for(int i = 0; i < simulationManager.grid.x; i++) {
            for(int j = 0; j < simulationManager.grid.y; j++) {
                String cellAsString = cellToString(simulationManager.getCell(i, j));
                System.out.print("|" + cellAsString + "|\t");
            }
            System.out.println();
        }
    }

    static String cellToString(Cell cell) {
        return cell.animals.size() + " " + cell.plants.size();
    }

}
