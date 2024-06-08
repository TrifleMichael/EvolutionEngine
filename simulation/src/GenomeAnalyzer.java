import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.min;

public class GenomeAnalyzer {
    public static int[] bucketCount(ArrayList<Genome> genomes, GenomCode parameter, int bucketNum) {
        int[] valuesInBuckets = new int[bucketNum];
        if (genomes.size() != 0) {
            // Get list of parameter values
            List<Double> parameterValues = genomes.stream().map(g -> g.geneticCode.get(parameter)).sorted().toList();
            // Sort values into buckets
            for (Double value : parameterValues) {
                int bucketIndex = (int)Math.floor(value * bucketNum);
                bucketIndex = bucketIndex == bucketNum ? bucketNum - 1 : bucketIndex; // Avoid going over the bucket count (will happen if parameter = 1)
                valuesInBuckets[bucketIndex]++;
            }
        }
        return valuesInBuckets;
    }
}
