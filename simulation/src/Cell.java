import java.util.ArrayList;
import java.util.LinkedList;

public class Cell {
    LinkedList<Animal> animals = new LinkedList<>();

    LinkedList<Animal> waitingList = new LinkedList<>();

    LinkedList<Plant> plants = new LinkedList<>();
    int x, y;
    Grid grid;

    public Cell(int x, int y, Grid grid) {
        this.x = x;
        this.y = y;
        this.grid = grid;
    }
}
