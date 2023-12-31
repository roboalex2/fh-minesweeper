package at.ac.fhcampuswien.board;

import at.ac.fhcampuswien.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Cell extends Pane {
    private static final int NUM_IMAGES = 13;
    private static final Image[] IMAGES = loadImages();

    private final Board gameBoard;
    private final int row;
    private final int col;
    private ImageView view;
    private List<Cell> neighbours;

    public Cell(Board board, int row, int col) {
        this.gameBoard = board;
        this.row = row;
        this.col = col;
        view = new ImageView(IMAGES[10]);
        view.setFitHeight(Board.CELL_SIZE);
        view.setFitWidth(Board.CELL_SIZE);
        getChildren().add(view);
    }

    public void setNeighbours(List<Cell> neighbours) {
        this.neighbours = neighbours;
        refreshImage();
    }

    public List<Cell> getNeighbours() {
        return neighbours;
    }

    public List<Cell> getMinedNeighbours() {
        return getNeighbours().stream()
                .filter(Cell::hasMine)
                .toList();
    }

    public boolean hasMine() {
        return gameBoard.getMinedCells().contains(this);
    }

    public boolean isFlagged() {
        return gameBoard.getFlaggedCells().contains(this);
    }

    public boolean isRevealed() {
        return gameBoard.getRevealedCells().contains(this);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void refreshImage() {
        if (isFlagged() && hasMine()) {
            this.view.setImage(IMAGES[11]);
        } else if (isFlagged() && !hasMine()) {
            if (gameBoard.isGameOver()) {
                this.view.setImage(IMAGES[12]);
            } else {
                this.view.setImage(IMAGES[11]);
            }
        } else if (hasMine() && (gameBoard.isGameOver() || isRevealed())) {
            this.view.setImage(IMAGES[9]);
        } else if (isRevealed()) {
            this.view.setImage(IMAGES[getMinedNeighbours().size()]);
        } else {
            this.view.setImage(IMAGES[10]);
        }
    }

    private static Image[] loadImages() {
        Image[] images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = i + ".png";
            try {
                URL url = Main.class.getClassLoader().getResource("sprites/" + path);
                if (url == null) {
                    // For some godforsaken reason if getClassLoader().getResource() is called in gradle context it can't access subfolders...
                    // But it can load the folder itself... so i just add the file manually.
                    url = Main.class.getClassLoader().getResource("sprites");
                    url = new URL(url.getProtocol(), "", -1, url.getFile() + path);
                }
                images[i] = new Image(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }
}
