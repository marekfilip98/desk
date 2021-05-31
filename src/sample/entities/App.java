package sample.entities;

import sample.entities.Employees;
import sample.entities.Reservation;

import java.util.ArrayList;

public class App {
    public ArrayList<Reservation> reservations;
    public ArrayList<Employees> employees;

    public App() {
        reservations = new ArrayList<>();
        employees = new ArrayList<>();
    }
}
