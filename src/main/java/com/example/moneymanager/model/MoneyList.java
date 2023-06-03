package com.example.moneymanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MoneyList {

    private ObservableList<Money> monies;

    private double balance;

    public MoneyList() {
        monies = FXCollections.observableArrayList();
    }

    public void calculateBalance(){
        double expense = 0;
        double income = 0;

        for (Money money: monies ) {
            if ( money instanceof Expense){
                expense += money.getQuantity();
            }
            if ( money instanceof Income){
                income += money.getQuantity();
            }
        }

        double temp = income - expense;

        if (temp < 0){
            setBalance(0);
        } else{
            setBalance(temp);
        }

    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ObservableList<Money> getMonies() {
        return monies;
    }



    public void addMoney(Money money) {
        monies.add(money);
    }

    public void updateMoney(int index, Money money) {
        monies.set(index, money);
    }

    public void deleteMoney(Money money) {
        monies.remove(money);
    }
}
