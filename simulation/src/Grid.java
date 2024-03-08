import java.util.ArrayList;

public class Grid {

    Cell[][] cells;
    int x, y;

    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
        initCells(x, y);
    }

    private void initCells(int x, int y) {
        cells = new Cell[x][y];
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                cells[i][j] = new Cell(i, j, this);
            }
        }
    }

    public void addAnimal(Animal animal, int x, int y) {
        cells[x][y].animals.add(animal);
    }

    public void addPlants(Plant plant, int x, int y) {
        cells[x][y].plants.add(plant);
    }

    public ArrayList<Cell> getCellNeighbors(int x, int y) {
        ArrayList<Cell> neighbors = new ArrayList<>();
        for(int i = Math.max(x-1, 0); i < Math.min(x+2, this.x) ; i++) {
            for(int j = Math.max(y-1, 0); j < Math.min(y+2, this.y); j++) {
                if (i != x || j != y) {
                    neighbors.add(cells[i][j]);
                }
            }
        }
        return neighbors;
    }
}
