package at.ac.fhcampuswien;

import at.ac.fhcampuswien.dialog.SettingsDialog;
import at.ac.fhcampuswien.model.GameSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Controller {
    private final Stage stage;

    // Model
    private Board board;

    // private
    private boolean isActive;
    private GameSettings gameSettings;

    // View Fields
    @FXML
    private Label message;
    @FXML
    private GridPane grid;
    @FXML
    private Button restart;

    public Controller(Stage stage) {
        this.stage = stage;
    }
    
    
    @FXML
    public void initialize() {
        isActive = true;
        this.gameSettings = SettingsDialog.display();
        this.board = new Board(gameSettings);
        Cell[][] cells = this.board.getCells();
        for (int i = 0; i < gameSettings.getHeight(); i++) {
            for (int j = 0; j < gameSettings.getWidth(); j++) {
                grid.add(cells[i][j], j, i);
            }
        }
        stage.setWidth(gameSettings.getWidth() * Board.CELL_SIZE + 17);
        stage.setHeight(gameSettings.getHeight() * Board.CELL_SIZE + 90);
    }

    @FXML
    public void update(MouseEvent event) {
        if (isActive) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                int col = (int) event.getX() / Board.CELL_SIZE;
                int row = (int) event.getY() / Board.CELL_SIZE;
                if (event.getButton() == MouseButton.PRIMARY) {
                    board.uncoverAllCells();// TODO remove
                    if (board.uncover(row, col)) {
                        if (board.isGameOver()) {
                            board.uncoverAllCells();
                            message.setText("Sorry. Leider verloren.");
                            board.uncoverAllCells();
                            isActive = false;
                        }
                    } else {
                        //TODO Give hint when one tries to uncover a marked cell.
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    board.markCell(row, col);
                }
                if (board.getCellsUncovered() == (gameSettings.getHeight() * gameSettings.getWidth() - gameSettings.getMines())
                        && board.getMinesMarked() == gameSettings.getMines()) {
                    message.setText("Glückwunsch! Du hast gewonnen.");
                    isActive = false;
                }
                if (isActive)
                    message.setText(" Marker: " + board.getMinesMarked() + "/" + gameSettings.getMines());
            }
        }
    }

    @FXML
    public void restart(ActionEvent actionEvent) {
        grid.getChildren().clear();
        initialize();
    }
}
