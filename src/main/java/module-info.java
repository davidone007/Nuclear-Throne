module com.example.gamedemo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gamedemo to javafx.fxml;
    exports com.example.gamedemo;
    exports com.example.gamedemo.control;
    opens com.example.gamedemo.control to javafx.fxml;
}