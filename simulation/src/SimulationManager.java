import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.*;
import java.util.stream.Collectors;
public class SimulationManager {

    Grid grid;

    public SimulationManager(Grid grid) {

        this.grid = grid;

    }


    public void populateGrid(ArrayList<Genome> genomes) {
        Random random = new Random();
        for (Genome genome : genomes) {
            grid.addAnimal(new Animal(genome), Math.abs(random.nextInt()) % grid.x, Math.abs(random.nextInt()) % grid.y);
        }
    }

    public void addPlants(int number) {
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            grid.addPlants(new Plant(), Math.abs(random.nextInt()) % grid.x, Math.abs(random.nextInt()) % grid.y);
        }
    }

    public void iterate() {
        for (int i = 0; i < grid.x; i++) {
            for (int j = 0; j < grid.y; j++) {
                Cell cell = grid.cells[i][j];
                var neighbours = grid.getCellNeighbors(i, j);
                // cell.animals.sort(Comparator.comparing((Animal a) -> (int)(Math.random() * 1000))); // Shuffle animals This throws an error after a longer time
                // TODO Shuffle animals

                // Eating
                for (Animal animal : cell.animals) {
                    for (Plant plant : cell.plants) // Each animal tries to eat the plants, finishes after eating either 0 or 1
                        if (plant.bitterness < animal.getGene("digestion")) {
                            animal.satiety += plant.foodValue;
                            cell.plants.remove(plant); // TODO This may cause error due to iterating and removing at the same time
                            break;
                        }
                }

                // Starving
                cell.animals.forEach(animal -> animal.getHungry(Settings.satietyLostPerIteration));
                int deleted = 0;
                for (int k = 0; k < cell.animals.size() - deleted; k++) { // Hack na to że nie da się sensownie iterować i usuwać elementów z collection
                    if (cell.animals.get(k).satiety < 0) {
                        cell.animals.remove(k);
                        deleted++;
                        k--;
                    }
                }


                // TODO: Implement other animal on animal interactions

                // Multiplying
                for (int k = 1; k < cell.animals.size(); k += 2) {
                    Animal animal1 = cell.animals.get(k - 1);
                    Animal animal2 = cell.animals.get(k);
                    if (animal1.satiety > Settings.satietyRequiredForBirth && animal2.satiety > Settings.satietyRequiredForBirth) { // TODO: Hardcoded but should not be
                        animal1.satiety -= Settings.satietyLostOnBirth; // TODO Also remove hard-code
                        animal2.satiety -= Settings.satietyLostOnBirth;
                        Genome newGenome = animal1.genome.combineGenomes(animal2.genome);
                        cell.animals.add(new Animal(newGenome));
                    }
                }

                // Moving
                for (Animal animal : cell.animals) {
                    Cell destination = neighbours.get((int) (Math.random() * neighbours.size()));
                    destination.animals.add(animal);
                }
                cell.animals.clear();
            }
        }
    }

    public Cell getCell(int x, int y) {
        return grid.cells[x][y];
    }

    public ArrayList<Genome> getGenomes() {
        ArrayList<Genome> genomes = new ArrayList<>();
        for (int i = 0; i < grid.x; i++) {
            for (int j = 0; j < grid.y; j++) {
                Cell cell = getCell(i, j);
                for (Animal animal : cell.animals) {
                    genomes.add(animal.genome);
                }
            }
        }
        return genomes;
    }

    public ArrayList<Plant> getPlants() {
        ArrayList<Plant> plants = new ArrayList<>();
        for (int i = 0; i < grid.x; i++) {
            for (int j = 0; j < grid.y; j++) {
                Cell cell = getCell(i, j);
                plants.addAll(cell.plants);
            }
        }
        return plants;
    }

    public double averageDigestion() {
        double avgDigestion = 0;
        ArrayList<Genome> genomes = getGenomes();
        for(var genome : genomes) {
            avgDigestion += genome.geneticCode.get("digestion");
        }
        avgDigestion /= genomes.size();
        return avgDigestion;
    }

    public void printGridInConsole() {
        for(int i = 0; i < grid.x; i++) {
            for(int j = 0; j < grid.y; j++) {
                String cellAsString = cellToString(getCell(i, j));
                System.out.print("|" + cellAsString + "|\t");
            }
            System.out.println();
        }
    }

    public String cellToString(Cell cell) {
        return cell.animals.size() + " " + cell.plants.size();
    }

}
