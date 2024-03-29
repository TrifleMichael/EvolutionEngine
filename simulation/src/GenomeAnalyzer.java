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
            // Count how many fall in each bucket
            int bucketSize = parameterValues.size() / bucketNum;
            for (int bucketIndex = 0; bucketIndex < bucketNum; bucketIndex++) {
                for (int dataIndex = bucketIndex * bucketSize; dataIndex < min(bucketIndex * (bucketSize+1), parameterValues.size()); dataIndex++) {
                    valuesInBuckets[bucketIndex] += 1;
                }
            }
        }
        return valuesInBuckets;
    }
}
