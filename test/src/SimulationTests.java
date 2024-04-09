import java.util.ArrayList;

public class SimulationTests {
    public static void main(String[] args) {
        test1();
    }

    static void test1() {
        // Setup
        var simulationManager = new SimulationManager(new Grid(10,10));
        ArrayList<Genome> genomes = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            genomes.add(new Genome(0.1, 0.1, 0.1, 0.1, 0.5, 0));
        }
        simulationManager.populateGrid(genomes);
        simulationManager.addPlants(30);

        for (int i = 1; i < 1000; i++) {
            // Simulation
            simulationManager.iterate();
            simulationManager.addPlants(2);

            // Analysis
            System.out.println("----- Iteration:" + i + " ------");
            printGridInConsole(simulationManager);
            var newGenomes = simulationManager.getGenomes();
            System.out.println("Number of animals: "+newGenomes.size());
            System.out.println("Number of plants: "+simulationManager.getPlants().size());
            System.out.println("Average digestion: "+ averageDigestion(newGenomes));
        }
    }

    static double averageDigestion(ArrayList<Genome> genomes) {
        double avgDigestion = 0;
        for(var genome : genomes) {
            avgDigestion += genome.geneticCode.get(GenomCode.DIGESTION);
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
