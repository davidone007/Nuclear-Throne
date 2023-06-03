package com.example.moneymanager.model;

import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Money {

    private double quantity;
    private String description;
    private String type;
    private String date;
    private Date dateFormatDate;

    public Money(double quantity, String description, String date, String type) {
        if (quantity < 0) {
            this.quantity = 0;
        } else {
            this.quantity = quantity;
        }
        this.description = description;
        this.type = type;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // formato de fecha
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // establecer hora, minutos, segundos y milisegundos en cero
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        this.date = dateFormat.format(calendar.getTime()); // guardar la fecha formateada en el atributo 'date'
        this.dateFormatDate = parsedDate; // guardar la fecha sin formato en el atributo 'dateFormatDate'
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getDateFormatDate() {
        return dateFormatDate;
    }

    public void setDateFormatDate(Date dateFormatDate) {
        this.dateFormatDate = dateFormatDate;
    }
}
