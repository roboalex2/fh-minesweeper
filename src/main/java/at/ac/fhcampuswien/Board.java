package at.ac.fhcampuswien;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final int CELL_SIZE = 15;
    public static final int ROWS = 25;
    public static final int COLS = 25;
    public static final int NUM_IMAGES = 13;
    public static final int NUM_MINES = 50;

    // Add further constants or let the cell keep track of its state.

    private Cell cells[][];
    private Image[] images;
    private int cellsUncovered;
    private int minesMarked;
    private boolean gameOver;

    /**
     * Constructor preparing the game. Playing a new game means creating a new Board.
     */
    public Board() {
        cells = new Cell[ROWS][COLS];
        cellsUncovered = 0;
        minesMarked = 0;
        gameOver = false;
        loadImages();
        // at the beginning every cell is covered
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                cells[r][c] = new Cell(images[0], 0);
            }
        }
        // TODO cover cells. complete the grid with calls to new Cell();

        // set neighbours for convenience
        // TODO compute all neighbours for a cell.

        // then we place NUM_MINES on the board and adjust the neighbours (1,2,3,4,... if not a mine already)
        // TODO place random mines.
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
        //TODO Uncover everything in case a mine was hit and the game is over.
    }


    public List<Cell> computeNeighbours(int x, int y) {
        List<Cell> neighbours = new ArrayList<>();
        // TODO get all the neighbours for a given cell. this means coping with mines at the borders.
        return neighbours;
    }

    /**
     * Loads the given images into memory. Of course you may use your own images and layouts.
     */
    private void loadImages() {
        images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "sprites/" + i + ".png";
            try {
                URL url = getClass().getClassLoader().getResource(path);
                this.images[i] = new Image(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        return minesMarked;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCellsUncovered() {
        return cellsUncovered;
    }

}
