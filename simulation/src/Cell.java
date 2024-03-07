import java.util.ArrayList;

public class Cell {
    ArrayList<Animal> animals = new ArrayList<>();
    ArrayList<Plant> plants = new ArrayList<>();
    int x, y;
    Grid grid;

    public Cell(int x, int y, Grid grid) {
        this.x = x;
        this.y = y;
        this.grid = grid;
    }
}
