import java.util.ArrayList;

public class SimulationTests {
    public static void main(String[] args) {
        test1();
    }

    static void test1() {
        // Setup
        var simulationManager = new SimulationManager(10, 10);
        ArrayList<Genome> genomes = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            genomes.add(new Genome(0.1, 0.1, 0.1, 0.1, 0.5));
        }
        simulationManager.populateGrid(genomes);
        simulationManager.addPlants(30);

        // Simulation
        for (int i = 1; i < 1000; i++) {
            System.out.println("Iteration:" + i);
            printGridInConsole(simulationManager);
            var finalGenomes = simulationManager.getGenomes();
            System.out.println("Number of animals: "+finalGenomes.size());
            System.out.println("Number of plants: "+simulationManager.getPlants().size());
            simulationManager.iterate(0.01);
            simulationManager.addPlants(2);
        }


        // Analysis
        var finalGenomes = simulationManager.getGenomes();
        double avgDigestion = 0;
        for(var genome : finalGenomes) {
            avgDigestion += genome.geneticCode.get("digestion");
        }
        avgDigestion /= finalGenomes.size();
        System.out.println("Number of animals: "+finalGenomes.size());
        System.out.println("Number of plants: "+simulationManager.getPlants().size());
        System.out.println("Average digestion: "+avgDigestion);
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
