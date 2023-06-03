package com.example.moneymanager.controller;

import com.example.moneymanager.MainApplication;
import com.example.moneymanager.model.Income;
import com.example.moneymanager.model.MoneyList;
import com.example.moneymanager.model.Money;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import javax.security.auth.callback.Callback;
import java.util.Collections;
import java.util.Comparator;

public class MainController {

    @FXML
    public Button reloadButton;
    @FXML
    public Label balanceLabel;
    @FXML
    public TableColumn typeMoveColumn;



    @FXML
    private TableView<Money> tableView;
    @FXML
    private TableColumn<Money, String> descriptionColumn;
    @FXML
    private TableColumn<Money, Double> quantityColumn;
    @FXML
    private TableColumn<Money, String> dateColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button viewMoneyButton;
    @FXML
    private Button viewExpenseButton;

    @FXML
    private  Button viewIncomeButton;

    private MoneyList moneyList;

    private ObservableList<Money> expenseList;
    private ObservableList<Money> incomeList;

    public void initialize(MoneyList moneyList) {
        this.moneyList = moneyList;
        this.expenseList = FXCollections.observableArrayList();
        this.incomeList = FXCollections.observableArrayList();

        typeMoveColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        moneyList.getMonies().sort(Comparator.comparing(Money::getDate).reversed());

        System.out.println(tableView);
        tableView.setItems(
                moneyList.getMonies()
        );
    }

    public void onAddMoney(ActionEvent event) {
        FXMLLoader loader = MainApplication.renderView("add-money-view.fxml");
        AddMoneyController addMoneyController = loader.getController();
        addMoneyController.setMoneyList(this.moneyList);
        addMoneyController.getStage().setHeight(addMoneyController.getAnchorPane().getPrefHeight());
        addMoneyController.getStage().setWidth(addMoneyController.getAnchorPane().getPrefWidth());
    }

    public Label getBalanceLabel() {
        return balanceLabel;
    }

    public void setBalanceLabel(Label balanceLabel) {
        this.balanceLabel = balanceLabel;
    }

    public void onReloadBalance() {
        updateIncomeAndExpensesLists();
        moneyList.calculateBalance();
        balanceLabel.setText("" + moneyList.getBalance());
    }

    public void onViewAll(ActionEvent actionEvent) {
        organize();
        System.out.println(tableView);
        tableView.setItems(
                moneyList.getMonies()
        );
    }

    public void updateIncomeAndExpensesLists() {
        organize();
        incomeList.clear();
        expenseList.clear();
        for (Money money : moneyList.getMonies()) {
            if (money instanceof Income) {
                incomeList.add(money);
            } else {
                expenseList.add(money);
            }
        }
    }

    public void onViewExpense(ActionEvent actionEvent) {
        updateIncomeAndExpensesLists();
        System.out.println(tableView);
        tableView.setItems(
                expenseList
        );
    }

    public void onViewIncome(ActionEvent actionEvent) {
        updateIncomeAndExpensesLists();
        System.out.println(tableView);
        tableView.setItems(
                incomeList
        );
    }

    public void organize(){
        Collections.sort(moneyList.getMonies(), new Comparator<Money>() {
            @Override
            public int compare(Money p1, Money p2) {
                return p2.getDateFormatDate().compareTo(p1.getDateFormatDate());
            }
        });
    }
}
