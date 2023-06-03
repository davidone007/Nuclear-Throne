module com.example.employeemanager {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.moneymanager to javafx.fxml;
    exports com.example.moneymanager;

    opens com.example.moneymanager.controller to javafx.fxml;
    exports com.example.moneymanager.controller;

    opens com.example.moneymanager.model to javafx.fxml;
    exports com.example.moneymanager.model;
}