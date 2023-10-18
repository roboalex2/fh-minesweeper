package at.ac.fhcampuswien;

import at.ac.fhcampuswien.board.Board;
import at.ac.fhcampuswien.board.Cell;
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

import java.util.Objects;

public class Controller {
    private final Stage stage;

    // Model
    private Board board;

    // private
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
        GameSettings settings = SettingsDialog.display(getDefaultSettingsIfNull(this.gameSettings));
        if (settings == null && this.gameSettings != null) {
            return;
        }
        this.gameSettings = getDefaultSettingsIfNull(settings);

        grid.getChildren().clear();
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
        if (!board.isGameOver()) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                int col = (int) event.getX() / Board.CELL_SIZE;
                int row = (int) event.getY() / Board.CELL_SIZE;
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (board.uncover(row, col)) {
                        if (board.isGameOver()) {
                            board.uncoverAllCells();
                            message.setText("Sorry. Leider verloren.");
                        }
                    } else {
                        message.setText("Can't reveal marked cell. Unmark first!");
                        return;
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    board.markCell(row, col);
                }

                // We win if all unrevealed fields are only mines or if we have marked all mines but not more.
                if (board.getCellsUncovered() == (gameSettings.getHeight() * gameSettings.getWidth() - gameSettings.getMines())
                        || (board.getMinesMarked() == gameSettings.getMines() && board.getMinesMarked() == board.getFlaggedCells().size())) {
                    message.setText("Glückwunsch! Du hast gewonnen.");
                    board.setGameOver(true);
                    board.uncoverAllCells();
                }
                if (!board.isGameOver())
                    message.setText(" Marker: " + board.getFlaggedCells().size() + "/" + gameSettings.getMines());
            }
        }
    }

    @FXML
    public void restart(ActionEvent actionEvent) {
        initialize();
    }

    private GameSettings getDefaultSettingsIfNull(GameSettings gameSettings) {
        return Objects.isNull(gameSettings) ? new GameSettings(64, 25, 25) : gameSettings;
    }
}
