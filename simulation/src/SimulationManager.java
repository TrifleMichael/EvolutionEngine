import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.*;
import java.util.stream.Collectors;
public class SimulationManager {

    Grid grid;
    Random randomGenerator = new Random();
    Interaction interaction = new Interaction();

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
                Collections.shuffle(cell.animals);
                // Eating
                for (Animal animal : cell.animals) {
                    ListIterator<Plant> plantIt = cell.plants.listIterator();
                    while(plantIt.hasNext()){
                        Plant plant = plantIt.next();
                        if (plant.bitterness < animal.getGene(GenomCode.DIGESTION)) {
                            animal.satiety += plant.foodValue;
                            plantIt.remove();
                            break;
                        }
                    }
                   // Each animal tries to eat the plants, finishes after eating either 0 or 1
                }

                // Multiplying
                for (int k = 1; k < cell.animals.size(); k += 2) {
                    Animal animal1 = cell.animals.get(k - 1);
                    Animal animal2 = cell.animals.get(k);
                    if (areSameSpecies(animal1, animal2)) {
                        if (animal1.satiety > Settings.satietyRequiredForBirth && animal2.satiety > Settings.satietyRequiredForBirth) {
                            animal1.satiety -= Settings.satietyLostOnBirth;
                            animal2.satiety -= Settings.satietyLostOnBirth;
                            Genome newGenome = animal1.genome.combineGenomes(animal2.genome);
                            Animal newAnimal = new Animal(newGenome);
                            newAnimal.satiety = Settings.satietyLostOnBirth * 2;
                            cell.animals.add(newAnimal);
                        }
                    }
                }

                // Interacting
                for (Animal animal:cell.animals) {
                    double doInteraction = Math.random();
                    if(doInteraction>=0.5){
                        int ind = randomGenerator.nextInt(cell.animals.size());
                        if (cell.animals.get(ind) != animal) { // TODO quickfix to avoid self interaction, do something better
                            if (!animal.dead && !cell.animals.get(ind).dead) {
                                if (!areSameSpecies(cell.animals.get(ind), animal)) { // Interact only with other species
                                    interaction.performInteraction(animal, cell.animals.get(ind));
                                }
                            }
                        }
                    }
                }

                // Starving
                cell.animals.forEach(animal -> animal.getHungry(Settings.satietyLostPerIteration));
                int deleted = 0;
                for (int k = 0; k < cell.animals.size() - deleted; k++) { // Hack na to że nie da się sensownie iterować i usuwać elementów z collection
                    if (cell.animals.get(k).satiety <= 0 || cell.animals.get(k).dead) {
                        cell.animals.remove(k);
                        deleted++;
                        k--;
                    }
                }

                // Moving
                for (Animal animal : cell.animals) {
                    Cell destination = neighbours.get((int) (Math.random() * neighbours.size()));
                    destination.waitingList.add(animal);
                }
            }
        }

        for (int i = 0; i < grid.x; i++) {
            for (int j = 0; j < grid.y; j++) {
                Cell cell = grid.cells[i][j];
                cell.animals.addAll(cell.waitingList);
                cell.waitingList.clear();
            }
        }
    }

    public boolean areSameSpecies(Animal animal1, Animal animal2) {
        double mse = 0;
        for(GenomCode code : GenomCode.values()) {
            double diff = (animal1.genome.geneticCode.get(code) - animal2.genome.geneticCode.get(code));
            diff = diff * diff;
            mse += diff;
        }
        return Math.sqrt(mse) < Settings.maxIntraSpeciesMSE;
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

    public HashMap<GenomCode, Double> maxGenomePerCode(GenomCode genomeCode){
        HashMap<GenomCode, Double> maxGenome= new HashMap<>();
        for(GenomCode code:GenomCode.class.getEnumConstants()){
            maxGenome.put(code,0.0);
        }
        ArrayList<Genome> genomes = getGenomes();
        for(var genome : genomes) {
                if(genome.geneticCode.get(genomeCode)>=maxGenome.get(genomeCode))
                    maxGenome = genome.geneticCode;
        }
        return maxGenome;
    }

    public HashMap<GenomCode, Double> minGenomePerCode(GenomCode genomeCode){
        HashMap<GenomCode, Double> minGenome= new HashMap<>();
        for(GenomCode code:GenomCode.class.getEnumConstants()){
            minGenome.put(code,2.0);
        }
        ArrayList<Genome> genomes = getGenomes();
        for(var genome : genomes) {
            if(genome.geneticCode.get(genomeCode)<=minGenome.get(genomeCode))
                minGenome = genome.geneticCode;
        }
        return minGenome;
    }

    public HashMap<GenomCode, Double> averageGenomesValues() {
        HashMap<GenomCode, Double> genSum = new HashMap<>();
        for(GenomCode code:GenomCode.class.getEnumConstants()){
            genSum.put(code,0.0);
        }
        ArrayList<Genome> genomes = getGenomes();
        for(var genome : genomes) {
            for(GenomCode code:GenomCode.class.getEnumConstants()){
                genSum.put(code,genSum.get(code)+genome.geneticCode.get(code));
            }
        }

        for(GenomCode code:GenomCode.class.getEnumConstants()){
            genSum.put(code,genSum.get(code)/genomes.size());
        }

        return genSum;
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
