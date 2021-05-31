package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import sample.entities.App;
import sample.dbutils.DbConnect;
import sample.entities.Employees;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;



public class ListControllerPracovnici implements Initializable {

    @FXML
    public Button goback;
    @FXML
    public TableView<Employees> tableReservations;

    public void returnToMain() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Stage window = (Stage) goback.getScene().getWindow();
        window.setTitle("Autoservis");

        window.setScene(new Scene(root,300, 275));
    }

    public void setTableContent(ArrayList<Employees> workers) {
        ObservableList<Employees> data = FXCollections.<Employees>observableArrayList();
        data.addAll(workers);

        tableReservations.setItems(data);
    }

    public void fetchAllEmployees(Connection con, ArrayList<Employees> employees) throws SQLException {
        String query = "select * from swinz_db_employee order by id";
        var statement = con.prepareStatement(query);
        ResultSet r = statement.executeQuery();

        while (r.next()){

            int id = r.getInt("id");
            String name = r.getString("name");
            String surname = r.getString("surname");
            String position = r.getString("position");



            employees.add(new Employees(id,name,surname,position));

        }
    }

    public void removeSelectedItem() throws SQLException, SQLIntegrityConstraintViolationException {
        DbConnect dbConnect = new DbConnect();
        ObservableList<Employees> selectedItem = tableReservations.getSelectionModel().getSelectedItems();
        int result = 0;
        try{
            result = removeRowFromDb(dbConnect.connectToLocalhost(), selectedItem.get(0).getId());
            if(result == 1){
                ObservableList<Employees> allItems = tableReservations.getItems();
                selectedItem.forEach(allItems::remove);
                successDeleteInfo();
            }else {
                failDeleteInfo();
            }
        } catch (SQLIntegrityConstraintViolationException e){
            integrityFailDeleteInfo();
        }
    }

    public void successDeleteInfo(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Zaměstnanec byl smazána!");

        alert.showAndWait();
    }

    public void failDeleteInfo(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Zaměstnance se nepodařilo smazat.");

        alert.showAndWait();
    }

    public void integrityFailDeleteInfo(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Zaměstnance se nepodařilo smazat, protože k němu existuje rezervace.");
        alert.showAndWait();
    }

    public void failDb(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Nepodařilo se připojit k databázi.");

        alert.showAndWait();
    }

    public static int removeRowFromDb(Connection con, int id) throws SQLException {
        String SQL = "DELETE FROM swinz_db_employee WHERE id = ? ";
        PreparedStatement pstmt = null;

        pstmt = con.prepareStatement(SQL);


        pstmt.setInt(1, id);
        int out = pstmt.executeUpdate();

        return out;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DbConnect dbConnect = new DbConnect();
        App app = new App();
        Connection con = dbConnect.con;
        if(con == null){
            failDb();
        }

        try {
            fetchAllEmployees(dbConnect.con, app.employees);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        setTableContent(app.employees);
    }
}
