
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

    SimulationTracker simulationTracker = new SimulationTracker();

    public SimulationEngine(BoardController boardController) {
        this.boardController = boardController;
    }

    @Override
    public void run() {
        initialize();
        simulation();
    }
    private void simulation(){
      try {
            Platform.runLater(() -> boardController.hasChanges(simulationManager, step));
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
                    Platform.runLater(()->boardController.hasChanges(simulationManager, step));
                }
                if(step%10==0)
                    Platform.runLater(() -> boardController.hasChanges(simulationManager, step));
                if(step >= 1000)

                {
                    Platform.runLater(() -> {
                        boardController.hasChanges(simulationManager, step);
                        boardController.stopSimulation();
                    });
                    break;
                }

                sleep(10);
            }
            for(;;) {
                if(endSimulation)
                    break;
                if(oneStepOnly) {
                    oneStepOnly = false;
                    simulationStep();
                    step+=1;
                    Platform.runLater(() -> boardController.hasChanges(simulationManager, step));
                }
            }
            Platform.runLater(() -> boardController.hasChanges(simulationManager, step));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected void initialize() {
        grid =new Grid(Settings.boardSize, Settings.boardSize);
        simulationManager = new SimulationManager(grid);
        //TODO Add all parameters to the settings screen.
        for (int i = 0; i < 500; i++) {
            genomes.add(new Genome(0.1, 0.1, 0.1, 0.1, 0.4, 0.1));
        }
        for (int i = 0; i < 200; i++) {
            genomes.add(new Genome(0.4, 0.1, 0.4, 0.0, 0.0, 1));
        }
        simulationManager.populateGrid(genomes);
        simulationManager.addPlants(Settings.initialPlantsNo);
    }

    protected void simulationStep(){
        simulationManager.iterate();
        simulationManager.addPlants(40);

        System.out.println("----- Iteration:" + step + " ------");
//        simulationManager.printGridInConsole();
//        System.out.println("Number of animals: "+simulationManager.getGenomes().size());
//        System.out.println("Number of plants: "+simulationManager.getPlants().size());
//        System.out.println(simulationManager.averageGenomesValues().toString());
//        System.out.println("Max digestion: " + simulationManager.maxGenomePerCode(GenomCode.DIGESTION).toString());
//        System.out.println("Min digestion: " + simulationManager.minGenomePerCode(GenomCode.DIGESTION).toString());
//        System.out.println("Max strength: " + simulationManager.maxGenomePerCode(GenomCode.STRENGTH).toString());
//        System.out.println("Min strength: " + simulationManager.minGenomePerCode(GenomCode.STRENGTH).toString());

//        System.out.println("Digestion:");

//        System.out.println();
//        System.out.println("Attack:");
//        int[] buckets2 = GenomeAnalyzer.bucketCount(simulationManager.getGenomes(), GenomCode.STRENGTH, 10);
//        for(int i = 0; i < 10; i++) {
//            System.out.print(i*10 + "-" + (i+1)*10 + "%: " + buckets2[i] + ", ");
//        }
//        System.out.println();


        updateLinearData();
        if (step % 10 == 0) {
            ploLineartData();
            createGeneSnapshot(GenomCode.DIGESTION, 100, "DigestionSnapshot", "Digestion Value", "Number of Individuals", "plots/DigestionSnapshot"+step+".png");
            createGeneSnapshot(GenomCode.STRENGTH, 100, "StrengthSnapshot", "Strength Value", "Number of Individuals", "plots/StrengthSnapshot"+step+".png");
        }

    }

    public void createGeneSnapshot(GenomCode genomCode, int bucketNumber, String title, String xAxisLabel, String yAxisLabel, String filePath) {
        int[] yValues = GenomeAnalyzer.bucketCount(simulationManager.getGenomes(), genomCode, bucketNumber);
        float[] xValues = new float[bucketNumber];
        for (int i = 0; i < bucketNumber; i++) {
            xValues[i] = (float) i / bucketNumber;
        }
        PlotTool.createAndSave2DPlot(xValues, PlotTool.toFloatArray(yValues), title, xAxisLabel, yAxisLabel, filePath);
    }

    public void updateLinearData() {
        int animalNum = simulationManager.getGenomes().size();
        simulationTracker.addData(step, animalNum, "animal_number");

        double foodValueOnMap = simulationManager.getPlants().stream().map(p -> p.foodValue).reduce(Double::sum).orElse(0.);
        simulationTracker.addData(step, (float)foodValueOnMap, "food_on_map");
    }

    public void ploLineartData() {
        simulationTracker.plot("animal_number", "Number of Animals", "Step", "Number of Animals", "plots/animal_number"+step+".png");
        simulationTracker.plot("food_on_map", "Total food on map", "Step", "Total food on map", "plots/food_on_map"+step+".png");
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


}
