package at.ac.fhcampuswien.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class NumberInputChangeListener implements ChangeListener<String> {

    private TextField numberField;

    public NumberInputChangeListener(TextField numberField) {
        this.numberField = numberField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue,
                        String newValue) {
        if (!newValue.matches("\\d*")) {
            numberField.setText(newValue.replaceAll("[^\\d]", ""));
        } else if (newValue.isBlank()) {
            numberField.setText("0");
        }
        if (numberField.getText().matches("\\d*")) {
            numberField.setText(Integer.parseInt(numberField.getText()) + "");
        }
    }
}
