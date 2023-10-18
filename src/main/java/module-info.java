module at.ac.fhcampuswien {
    requires javafx.controls;
    requires javafx.fxml;

    opens at.ac.fhcampuswien to javafx.fxml;
    exports at.ac.fhcampuswien;
    exports at.ac.fhcampuswien.dialog;
    opens at.ac.fhcampuswien.dialog to javafx.fxml;
    exports at.ac.fhcampuswien.board;
    opens at.ac.fhcampuswien.board to javafx.fxml;
}