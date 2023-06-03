package com.example.moneymanager.controller;

import com.example.moneymanager.model.Expense;
import com.example.moneymanager.model.Income;
import com.example.moneymanager.model.MoneyList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddMoneyController {

    @FXML
    private TextField descriptionMoneyTF;

    @FXML
    private TextField quantityMoneyTF;
    @FXML
    private TextField dateMoneyTF;

    @FXML
    private CheckBox income;

    @FXML
    private CheckBox expense;


    private MoneyList moneyList;

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    @FXML
    private AnchorPane anchorPane;

    public void setMoneyList(MoneyList moneyList){
        this.moneyList = moneyList;
    }

    @FXML
    public void onAddEmployee(ActionEvent event) {

        String description = descriptionMoneyTF.getText();
        String quantity = quantityMoneyTF.getText();
        String date = dateMoneyTF.getText();
        double money = (double) 0.0;
        boolean isIncome = false;
        String type = "Expense";

        if (income.isSelected()){
            isIncome = true;
            type = "Income";
        }

        try {
            money = Double.parseDouble(quantity);
            if (isIncome){
                Income income = new Income(money,description,date, type);
                moneyList.addMoney(income);
            } else{
                Expense expense = new Expense(money, description, date, type);
                moneyList.addMoney(expense);
            }
            descriptionMoneyTF.setText("");
            quantityMoneyTF.setText("");
            dateMoneyTF.setText("");
            expense.setSelected(false);
            income.setSelected(false);


        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al agregar el movimiento");
            alert.setContentText("La cantidad del dinero debe ser un número válido.");
            alert.showAndWait();
        }
    }

    @FXML
    public void onClose(ActionEvent event) {
        Stage stage = (Stage) descriptionMoneyTF.getScene().getWindow();
        stage.close();
    }

    public void onIncome() {
        if (income.isSelected()){
            expense.setSelected(false);
        }
    }

    public void onExpense() {
        if (expense.isSelected()){
            income.setSelected(false);
        }
    }

    public Stage getStage() {
        return (Stage) anchorPane.getScene().getWindow();
    }
}
