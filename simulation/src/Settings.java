import java.util.ArrayList;
import java.util.List;

public class Settings {
    static public double mutationStandardDeviation = 0.02;

    static public double satietyLostPerIteration = 0.01;

    static public double satietyLostOnBirth = 0.2;

    static public double satietyRequiredForBirth = 0.8;

    static public double startingSatiety = 1;

    static ArrayList<String> animalStats = new ArrayList<>(List.of("strength", "sight", "camouflage", "speed", "digestion")); // Hardcoded in other places
}
