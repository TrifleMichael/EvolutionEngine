import java.util.HashMap;
import java.util.Random;

public class Genome {

    public HashMap<GenomCode, Double> geneticCode = new HashMap<>();

    public Genome(double strength, double sight, double camouflage, double speed, double digestion, double aggression) {
        geneticCode.put(GenomCode.STRENGTH, strength);
        geneticCode.put(GenomCode.SIGHT, sight);
        geneticCode.put(GenomCode.CAMOUFLAGE, camouflage);
        geneticCode.put(GenomCode.SPEED, speed);
        geneticCode.put(GenomCode.DIGESTION, digestion);
        geneticCode.put(GenomCode.AGGRESSION, aggression);
    }

    public Genome(HashMap<GenomCode, Double> geneticCode) {
        this.geneticCode = geneticCode;
    }

    private double getGenomeSum() {
        return geneticCode.values().stream().reduce(0.0, Double::sum);
    }

    private void normalizeGenome() {
        double sum = getGenomeSum();
        for (var entry : geneticCode.entrySet()) {
            GenomCode key = entry.getKey();
            double value = entry.getValue();
            geneticCode.put(key, value/sum);
        }
    }

    public Genome mutateGeneticCode() {
        Genome newGenome = new Genome(geneticCode); // Clone
        Random random = new Random();
        for (var entry : newGenome.geneticCode.entrySet()) { // Mutate
            GenomCode key = entry.getKey();
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
        HashMap<GenomCode, Double> newGeneticCode = new HashMap<>();
        for (GenomCode stat : GenomCode.class.getEnumConstants()) {
            newGeneticCode.put(stat, (g1.geneticCode.get(stat) + g2.geneticCode.get(stat)) / 2);
        }
        // Normalize
        Genome newGenome = new Genome(newGeneticCode);
        newGenome.normalizeGenome();
        return newGenome;
    }

    public void printGenome() {
        for (var entry : geneticCode.entrySet()) {
            GenomCode key = entry.getKey();
            double value = entry.getValue();
            System.out.println(key + " - " + value);
        }
    }
}
