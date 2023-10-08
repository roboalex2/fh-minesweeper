module at.ac.fhcampuswien {
    requires javafx.controls;
    requires javafx.fxml;

    opens at.ac.fhcampuswien to javafx.fxml;
    exports at.ac.fhcampuswien;
}