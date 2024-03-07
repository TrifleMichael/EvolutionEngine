import java.util.HashMap;
import java.util.Random;

public class Genome {

    public HashMap<String, Double> geneticCode = new HashMap<>();

    public Genome(double strength, double sight, double camouflage, double speed, double digestion) {
        geneticCode.put("strength", strength);
        geneticCode.put("sight", sight);
        geneticCode.put("camouflage", camouflage);
        geneticCode.put("speed", speed);
        geneticCode.put("digestion", digestion);
    }

    public Genome(HashMap<String, Double> geneticCode) {
        this.geneticCode = geneticCode;
    }

    private double getGenomeSum() {
        return geneticCode.values().stream().reduce(0.0, Double::sum);
    }

    private void normalizeGenome() {
        double sum = getGenomeSum();
        for (var entry : geneticCode.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            geneticCode.put(key, value/sum);
        }
    }

    public Genome mutateGeneticCode() {
        Genome newGenome = new Genome(geneticCode); // Clone
        Random random = new Random();
        for (var entry : newGenome.geneticCode.entrySet()) { // Mutate
            String key = entry.getKey();
            double value = entry.getValue();
            value += random.nextGaussian() * Settings.mutationStandardDeviation;
            value = value < 0 ? 0 : value; // Not less than 0
            newGenome.geneticCode.put(key, value);
        }
        newGenome.normalizeGenome(); // Normalize
        return newGenome;
    }

    public Genome combineGenomes(Genome other) {
        // Mutate parent DNA (without affecting parent)
        Genome g1 = mutateGeneticCode();
        Genome g2 = other.mutateGeneticCode();
        // Average out parent DNA
        HashMap<String, Double> newGeneticCode = new HashMap<>();
        for (String stat : Settings.animalStats) {
            newGeneticCode.put(stat, (g1.geneticCode.get(stat) + g2.geneticCode.get(stat)) / 2);
        }
        // Normalize
        Genome newGenome = new Genome(newGeneticCode);
        newGenome.normalizeGenome();
        return newGenome;
    }

    public void printGenome() {
        for (var entry : geneticCode.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            System.out.println(key + " - " + value);
        }
    }
}
