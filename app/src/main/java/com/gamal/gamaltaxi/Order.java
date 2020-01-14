package com.gamal.gamaltaxi;

import androidx.annotation.NonNull;

public class Order {

    private String clientNumber, departure, date, time;

     Order(String clientNumber, String departure, String date, String time) {
        this.clientNumber = clientNumber;
        this.departure = departure;
        this.date = date;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    @NonNull
    @Override
    public String toString() {
        return "Full order description: \n " +
                "Client Number: " + clientNumber + "\n" +
                "Departure from: " + departure + "\n" +
                "Order was made in: " + time + " " + date + " .";
    }
}
