package at.ac.fhcampuswien;

import at.ac.fhcampuswien.model.GameSettings;
import javafx.scene.image.Image;

import java.util.*;

public class Board {
    public static final int CELL_SIZE = 15;


    // Add further constants or let the cell keep track of its state.
    private GameSettings gameSettings;
    private Cell cells[][];
    private HashSet<Cell> minedCells = new HashSet<>();
    private HashSet<Cell> flaggedCells = new HashSet<>();
    private HashSet<Cell> revealedCells = new HashSet<>();
    private Image[] images;
    private boolean gameOver;

    /**
     * Constructor preparing the game. Playing a new game means creating a new Board.
     */
    public Board(GameSettings settings) {
        cells = new Cell[settings.getHeight()][settings.getWidth()];
        gameOver = false;
        this.gameSettings = settings;
        // at the beginning every cell is covered
        for (int r = 0; r < settings.getHeight(); r++) {
            for (int c = 0; c < settings.getWidth(); c++) {
                cells[r][c] = new Cell(this);
            }
        }

        for (int r = 0; r < settings.getHeight(); r++) {
            for (int c = 0; c < settings.getWidth(); c++) {
                cells[r][c].setNeighbours(computeNeighbours(r, c));
            }
        }

        // then we place NUM_MINES on the board and adjust the neighbours (1,2,3,4,... if not a mine already)
        ArrayList<Cell> allCells = Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        for (int i = 0; i < gameSettings.getMines() && allCells.size() > 1; i++) {
            Cell target = allCells.get(getRandomNumberInts(0, allCells.size() - 1));
            minedCells.add(target);
            allCells.remove(target);
        }
    }

    private List<Cell> computeNeighbours(int row, int col) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        for (int y = row - 1; y >= row + 1; y ++) {
            for (int x = col - 1; x >= col + 1; x ++) {
                if (x > 0 && y > 0 &&
                        y < gameSettings.getHeight() &&
                        x < gameSettings.getWidth() &&
                        y != row && x != col
                ) {
                    neighbours.add(cells[y][x]);
                }
            }
        }
        return neighbours;
    }

    public boolean uncover(int row, int col) {
        // TODO uncover the cell, check if it is a bomb, if it is an empty cell you may! uncover all adjacent empty cells.
        return true; // could be a void function as well
    }

    public boolean markCell(int row, int col) {
        // TODO mark the cell if it is not already marked.
        return true;
    }

    public void uncoverEmptyCells(Cell cell) {
        // TODO you may implement this function. It's usually implemented by means of a recursive function.
    }


    public void uncoverAllCells() {
        Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .forEach(cell -> {
                    revealedCells.add(cell);
                    cell.refreshImage();
                });

    }

    public HashSet<Cell> getMinedCells() {
        return minedCells;
    }

    public HashSet<Cell> getFlaggedCells() {
        return flaggedCells;
    }

    public HashSet<Cell> getRevealedCells() {
        return revealedCells;
    }

    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Computes a random int number between min and max.
     *
     * @param min the lower bound. inclusive.
     * @param max the upper bound. inclusive.
     * @return a random int.
     */
    private int getRandomNumberInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, (max + 1)).findFirst().getAsInt();
    }

    public int getMinesMarked() {
        return flaggedCells.size();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCellsUncovered() {
        return revealedCells.size();
    }
}
