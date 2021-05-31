package sample.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sample.dbutils.DbConnect;
import sample.dbutils.ReservationDB;
import sample.entities.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FXMLDocumentControllerTest {

    @Test//Existující zaměstnanec
    void searchForEmployeeExistingEmployee() {
        FXMLDocumentController fxmlDocumentController = new FXMLDocumentController();
        DbConnect connect = new DbConnect();
        try {
            assertEquals(true, fxmlDocumentController.searchForEmployee(connect.con, 2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test//Nexistující zaměstnanec
    void searchForEmployeeNonExistingEmployee() {
        FXMLDocumentController fxmlDocumentController = new FXMLDocumentController();
        DbConnect connect = new DbConnect();
        try {
            assertEquals(false, fxmlDocumentController.searchForEmployee(connect.con, 200));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test//Nexistující zaměstnanec
    void searchForEmployeeNonExistingCar() {
        FXMLDocumentController fxmlDocumentController = new FXMLDocumentController();
        DbConnect connect = new DbConnect();

        String spz = "XXXXX";

        try {
            assertEquals(false, fxmlDocumentController.searchForExistingCar(connect.con, spz));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test//Nexistující zaměstnanec
    void searchForEmployeeExistingCar() {
        FXMLDocumentController fxmlDocumentController = new FXMLDocumentController();
        DbConnect connect = new DbConnect();

        String spz = "6A42222";

        try {
            assertEquals(false, fxmlDocumentController.searchForExistingCar(connect.con, spz));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test//Načtení dat z databáze
    void fetchAllReservations() {
        FXMLDocumentController fxmlDocumentController = new FXMLDocumentController();
        DbConnect connect = new DbConnect();
        ReservationDB reservationDB = new ReservationDB();

        ArrayList<Reservation> reservations = new ArrayList<>();


        try {
            reservationDB.fetchAllDateAndTimeThatAlreadyExists(connect.con, reservations);
            assertTrue(reservations.size() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test//ID zaměstnance jako text
    void textIDEmployee() {
        TextField idpracovnik = new TextField();
        idpracovnik.setText("Dva");


        Assertions.assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt(idpracovnik.getText());
        });
    }

}