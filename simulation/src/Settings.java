import java.util.ArrayList;
import java.util.List;

public class Settings {

    static public int iterations = 1000;

    static public int boardSize = 10;

    static public int initialPlantsNo = 30;

    static public double mutationStandardDeviation = 0.02;

    static public double satietyLostPerIteration = 0.01;

    static public double satietyLostOnBirth = 0.2;

    static public double satietyRequiredForBirth = 0.8;

    static public double startingSatiety = 1;

    static ArrayList<String> animalStats = new ArrayList<>(List.of("strength", "sight", "camouflage", "speed", "digestion")); // Hardcoded in other places

    public static void setIterations(int iterations) {
        Settings.iterations = iterations;
    }

    public static void setBoardSize(int boardSize) {
        Settings.boardSize = boardSize;
    }

    public static void setInitialPlantsNo(int initialPlantsNo) {
        Settings.initialPlantsNo = initialPlantsNo;
    }

    public static void setMutationStandardDeviation(double mutationStandardDeviation) {
        Settings.mutationStandardDeviation = mutationStandardDeviation;
    }

    public static void setSatietyLostPerIteration(double satietyLostPerIteration) {
        Settings.satietyLostPerIteration = satietyLostPerIteration;
    }

    public static void setSatietyLostOnBirth(double satietyLostOnBirth) {
        Settings.satietyLostOnBirth = satietyLostOnBirth;
    }

    public static void setSatietyRequiredForBirth(double satietyRequiredForBirth) {
        Settings.satietyRequiredForBirth = satietyRequiredForBirth;
    }

    public static void setStartingSatiety(double startingSatiety) {
        Settings.startingSatiety = startingSatiety;
    }
}
