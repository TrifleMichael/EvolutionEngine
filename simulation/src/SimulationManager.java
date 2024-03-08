import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class SimulationManager {

    Grid grid;

    public SimulationManager(int grid_x, int grid_y) {
        grid = new Grid(grid_x, grid_y);
    }

    public void populateGrid(ArrayList<Genome> genomes) {
        Random random = new Random();
        for(Genome genome : genomes) {
            grid.addAnimal(new Animal(genome), random.nextInt() % grid.x, random.nextInt() % grid.y);
        }
    }

    public void addPlants(int number) {
        Random random = new Random();
        for(int i = 0; i < number; i++) {
            grid.addPlants(new Plant(), random.nextInt() % grid.x, random.nextInt() % grid.y);
        }
    }

    public void iterate() {
        for(int i = 0; i < grid.x; i++) {
            for(int j = 0; j < grid.y; j++) {
                Cell cell = grid.cells[i][j];
                var neighbours = grid.getCellNeighbors(i, j);
                cell.animals.sort(Comparator.comparing((Animal a) -> Math.random())); // Shuffle animals

                // Eating
                for(Animal animal : cell.animals) {
                    for (Plant plant : cell.plants) // Each animal tries to eat the plants, finishes after eating either 0 or 1
                        if (plant.bitterness < animal.getGene("digestion")) {
                            animal.satiety += plant.foodValue;
                            cell.plants.remove(plant);
                            break;
                    }
                }

                // TODO: Implement animal on animal interactions

                // Moving
                for(Animal animal : cell.animals) {
                    cell.animals.remove(animal);
                    Cell destination = neighbours.get((int)(Math.random() * (neighbours.size() + 1)));
                    destination.animals.add(animal);
                }

            }
        }
    }

}
