package at.ac.fhcampuswien.dialog;

import at.ac.fhcampuswien.listener.NumberInputChangeListener;
import at.ac.fhcampuswien.model.GameSettings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.atomic.AtomicInteger;

public class SettingsDialog {

    private static final String DEFAULT_MINE_AMOUNT = "64";
    private static final String DEFAULT_CELL_WIDTH = "25";
    private static final String DEFAULT_CELL_HEIGHT = "25";

    public static GameSettings display() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField minesField = new TextField();
        minesField.textProperty().addListener(new NumberInputChangeListener(minesField));
        minesField.setText(DEFAULT_MINE_AMOUNT);
        minesField.setMaxWidth(50);

        TextField widthField = new TextField();
        widthField.textProperty().addListener(new NumberInputChangeListener(widthField));
        widthField.setText(DEFAULT_CELL_WIDTH);
        widthField.setMaxWidth(50);

        TextField heightField = new TextField();
        heightField.textProperty().addListener(new NumberInputChangeListener(heightField));
        heightField.setText(DEFAULT_CELL_HEIGHT);
        heightField.setMaxWidth(50);


        Button button = new Button("Start Game");
        AtomicInteger mines = new AtomicInteger();
        AtomicInteger width = new AtomicInteger();
        AtomicInteger height = new AtomicInteger();
        button.setOnAction(e -> {
            mines.set(Integer.parseInt(minesField.getText()));
            width.set(Integer.parseInt(widthField.getText()));
            height.set(Integer.parseInt(heightField.getText()));

            if (mines.get() >= width.get() * height.get() || mines.get() <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("To many mines!");
                alert.setHeaderText("To many mines!");
                alert.setContentText(String.format(
                        "Expected between [%d, %d] mines. Got: %d",
                        1,
                        width.get() * height.get(),
                        mines.get()
                ));
                alert.showAndWait();
                return;
            }

            if (width.get() <= 1 || width.get() > 70 || height.get() <= 1 || height.get() > 70) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Board size invalid!");
                alert.setHeaderText("Board size invalid!");
                alert.setContentText(String.format(
                        "Width and height must be between [2, 70]. Got: width='%d', height='%d'",
                        width.get(),
                        height.get()
                ));
                alert.showAndWait();
                return;
            }

            stage.close();
        });

        Label label2 = new Label("Amount of Mines:");
        Label label3 = new Label("Cell-Height:");
        Label label4 = new Label("Cell-Width:");

        GridPane layout = new GridPane();

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        layout.add(label2, 0,0);
        layout.add(minesField, 1,0);

        layout.add(label3, 0,1);
        layout.add(label4, 2,1);
        layout.add(heightField, 1,1);
        layout.add(widthField, 3,1);
        layout.add(button, 0, 2);

        Scene scene = new Scene(layout, 300, 100);
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);
        stage.setOnCloseRequest(e -> {
            mines.set(Integer.parseInt(DEFAULT_MINE_AMOUNT));
            width.set(Integer.parseInt(DEFAULT_CELL_WIDTH));
            height.set(Integer.parseInt(DEFAULT_CELL_HEIGHT));
            stage.close();
        });
        stage.setTitle("Game Settings");
        stage.setScene(scene);
        stage.showAndWait();

        return new GameSettings(mines.get(), height.get(), width.get());
    }
}
