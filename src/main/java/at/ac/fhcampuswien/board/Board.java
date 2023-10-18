package at.ac.fhcampuswien.board;

import at.ac.fhcampuswien.model.GameSettings;

import java.util.*;

import static java.util.function.Predicate.not;

public class Board {
    public static final int CELL_SIZE = 15;

    private GameSettings gameSettings;
    private Cell cells[][];
    private HashSet<Cell> minedCells = new HashSet<>();
    private HashSet<Cell> flaggedCells = new HashSet<>();
    private HashSet<Cell> revealedCells = new HashSet<>();
    private boolean gameOver;


    /**
     * Constructor preparing the game. Playing a new game means creating a new Board.
     */
    public Board(GameSettings settings) {
        cells = new Cell[settings.getHeight()][settings.getWidth()];
        gameOver = false;
        this.gameSettings = settings;
        // at the beginning every cell is covered
        setupCells();
        calculateCellNeighbours();
        layMines();
    }

    private void setupCells() {
        for (int r = 0; r < gameSettings.getHeight(); r++) {
            for (int c = 0; c < gameSettings.getWidth(); c++) {
                cells[r][c] = new Cell(this, r, c);
            }
        }
    }

    private void calculateCellNeighbours() {
        for (int r = 0; r < gameSettings.getHeight(); r++) {
            for (int c = 0; c < gameSettings.getWidth(); c++) {
                cells[r][c].setNeighbours(getNeighbours(r, c));
            }
        }
    }

    private void layMines() {
        ArrayList<Cell> allCells = Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        for (int i = 0; i < gameSettings.getMines() && allCells.size() > 1; i++) {
            Cell target = allCells.get(getRandomNumberInts(0, allCells.size() - 1));
            minedCells.add(target);
            allCells.remove(target);
        }
    }

    private List<Cell> getNeighbours(int row, int col) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        for (int y = row - 1; y <= row + 1; y++) {
            for (int x = col - 1; x <= col + 1; x++) {
                if (x >= 0 && y >= 0 &&
                        y < gameSettings.getHeight() &&
                        x < gameSettings.getWidth() &&
                        !(y == row && x == col)
                ) {
                    neighbours.add(cells[y][x]);
                }
            }
        }
        return neighbours;
    }

    // Moves a given cell elsewhere if mined or does nothing
    private void moveMineElseWhere(Cell minedCell) {
        if (!minedCell.hasMine()) {
            return;
        }

        Arrays.stream(cells).flatMap(Arrays::stream)
                .filter(not(Cell::hasMine))
                .findAny()
                .ifPresent(cell -> getMinedCells().add(cell));

        getMinedCells().remove(minedCell);
    }

    private void revealCell(Cell cell) {
        getRevealedCells().add(cell);
        cell.refreshImage();
    }

    public boolean uncover(int row, int col) {
        Cell clickedCell = cells[row][col];
        if (clickedCell.isFlagged()) {
            return false;
        }

        if (getRevealedCells().isEmpty()) {
            // You can't die on your first click
            moveMineElseWhere(clickedCell);
        }

        if (clickedCell.hasMine()) {
            gameOver = true;
            revealCell(clickedCell);
            return true;
        }

        uncoverEmptyCells(clickedCell);

        return true; // could be a void function as well
    }

    public boolean markCell(int row, int col) {
        Cell cell = getCells()[row][col];
        if (cell.isFlagged()) {
            getFlaggedCells().remove(cell);
            cell.refreshImage();
            return false;
        }
        getFlaggedCells().add(cell);
        cell.refreshImage();
        return true;
    }

    public void uncoverEmptyCells(Cell cell) {
        Queue<Cell> floodQueue = new LinkedList<>();
        floodQueue.add(cell);
        revealCell(cell);

        while (floodQueue.peek() != null) {
            Cell currentCell = floodQueue.poll();

            if (currentCell.getMinedNeighbours().isEmpty()) {
                List<Cell> toProcess = currentCell.getNeighbours().stream()
                        .filter(nextCell ->
                                nextCell.getCol() == currentCell.getCol() ||
                                        nextCell.getRow() == currentCell.getRow()
                        ) // Only consider direct neighbours
                        .filter(not(Cell::isRevealed))
                        .filter(not(Cell::isFlagged)) // If you mark something that isn't a mine it's your own fault
                        .toList();
                toProcess.forEach(this::revealCell); // reveal here to prevent double add to queue
                floodQueue.addAll(toProcess);
            }
        }
    }

    public void uncoverAllCells() {
        Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .forEach(this::revealCell);
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
        return (int) flaggedCells.stream()
                .filter(cell -> minedCells.contains(cell))
                .count();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCellsUncovered() {
        return revealedCells.size();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
