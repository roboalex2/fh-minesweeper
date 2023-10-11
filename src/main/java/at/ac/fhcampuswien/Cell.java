package at.ac.fhcampuswien;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Cell extends Pane {
    private static final int NUM_IMAGES = 13;
    private static final Image[] IMAGES = loadImages();

    private final Board gameBoard;
    private ImageView view;

    private List<Cell> neighbours;

    public Cell(Board board) {
        view = new ImageView(IMAGES[10]);
        getChildren().add(view);
        this.gameBoard = board;
    }

    public void setNeighbours(List<Cell> neighbours) {
        this.neighbours = neighbours;
        refreshImage();
    }

    private List<Cell> getNeighbours() {
        return neighbours;
    }

    private List<Cell> getMinedNeighbours() {
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

    public void refreshImage() {
        if (isFlagged() && !hasMine()) {
            this.view.setImage(IMAGES[11]);
        } else if (isFlagged() && hasMine()) {
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
            var path = "sprites/" + i + ".png";
            try {
                URL url = Cell.class.getClassLoader().getResource(path);
                images[i] = new Image(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }
}
