package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.entities.App;
import sample.dbutils.DbConnect;
import sample.entities.Reservation;
import sample.dbutils.ReservationDB;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ListController implements Initializable {

    @FXML
    public Button goback;
    @FXML
    public TableView<Reservation> tableReservations;





    public void returnToMain() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));

        Stage window = (Stage) goback.getScene().getWindow();
        window.setTitle("Autoservis");

        window.setScene(new Scene(root,300, 275));
    }

    public void setTableContent(ArrayList<Reservation> reservations) {
        ObservableList<Reservation> data = FXCollections.<Reservation>observableArrayList();
        data.addAll(reservations);

        tableReservations.setItems(data);
    }

    public void removeSelectedItem() throws SQLException, InterruptedException {
        DbConnect dbConnect = new DbConnect();
        ObservableList<Reservation> selectedItem = tableReservations.getSelectionModel().getSelectedItems();
        int result = ReservationDB.removeRowFromDb(dbConnect.connectToLocalhost(), selectedItem.get(0).getId());


        if(result == 1){
            ObservableList<Reservation> allItems = tableReservations.getItems();
            selectedItem.forEach(allItems::remove);
            successDeleteInfo();
        } else {
            failDeleteInfo();
        }
    }

    public void successDeleteInfo(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Rezervace byla smazána!");

        alert.showAndWait();
    }

    public void failDeleteInfo(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Rezervaci se nepodařilo smazat.");

        alert.showAndWait();
    }

    public void failDb(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Nepodařilo se připojit k databázi.");

        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        DbConnect dbConnect = new DbConnect();
        App app = new App();
        ReservationDB reservationDB = new ReservationDB();


        try {
            Connection con = dbConnect.con;
            if(dbConnect.con == null){
                failDb();
            }
            reservationDB.fetchAllDateAndTimeThatAlreadyExists(con, app.reservations);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        setTableContent(app.reservations);

        /*columnTitle.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("id"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<Reservation, String>("spz"));
        columnUUID.setCellValueFactory(new PropertyValueFactory<Reservation, Date>("datum"));
        columnUUID.setCellValueFactory(new PropertyValueFactory<Reservation, LocalTime>("cas"));*/





    }
}
