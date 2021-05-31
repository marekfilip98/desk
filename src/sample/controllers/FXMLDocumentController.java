package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.dbutils.DbConnect;
import sample.entities.Reservation;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.function.DoubleToIntFunction;

public class FXMLDocumentController implements Initializable {
    @FXML
    private DatePicker datePicker;
    @FXML
    public Button button;
    @FXML
    public Button listbutton;
    @FXML
    public Button employeebutton;
    @FXML
    public TextField cas;
    @FXML
    public TextField id_pracovnik;
    @FXML
    public TextField zavada;
    @FXML
    public TextField spz;
    @FXML
    public TextField jmenoprijmeni;

    public void buttonHandle(){
        System.out.println("Text");
        button.setText("Ahoj");
    }

    public void postResetvation() throws SQLException{
        DbConnect dbConnect = new DbConnect();


        String spzText = spz.getText();
        String time = cas.getText();
        LocalDate date = datePicker.getValue();
        String problem = zavada.getText();
        //int id_pracovnika = Integer.valueOf(id_pracovnik.getText());
        String jmenop = jmenoprijmeni.getText();

        //________________________________________
        //PŘEHODIT DO TŘÍDY UTILS
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //creating the instance of LocalDate using the day, month, year info
        LocalDate localDate = LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth());

        //local date + atStartOfDay() + default time zone + toInstant() = Date
        Date dateDate = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

        LocalTime timeTime = LocalTime.parse(time);

        Reservation reservation = new Reservation(dateDate,timeTime,spzText,problem,jmenop);
        //PŘEHODIT DO TŘÍDY UTILS
        //________________________________________

        int ou = dbSelectMatchingDates(dbConnect.con,date,time);
            if( ou == 1){
                            insertReservation(dbConnect.con,reservation);
                            successAddInfo();
                            spz.clear();
                            cas.clear();
                            zavada.clear();
                            jmenoprijmeni.clear();
                    }

              if( ou == 0) {
                        failInsertLogicReseration();
                    }

              if(ou == -1){
                  failInsertLogicReserationDueToDate();
              }
                    /*if(dbSelectMatchingDates(dbConnect.con,date,time) == false){
                        ();
                    }*/






    }


    public boolean insertReservation(Connection conn, Reservation reservation) throws SQLException {

        String query = " insert into swinz_db (datum, cas, spz, zavada, jmeno_prijmeni)"
                + " values (?, ?,?,?,?)";

        boolean count = false;
        try{
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            LocalDate date = reservation.getDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            preparedStmt.setDate (1, java.sql.Date.valueOf(date));
            preparedStmt.setTime (2, java.sql.Time.valueOf(reservation.getCas().plusHours(1)));
            preparedStmt.setString(3, reservation.getSpz());
            //preparedStmt.setInt    (4, reservation.getId_pracovnik());
            preparedStmt.setString    (4, reservation.getZavada());
            preparedStmt.setString    (5, reservation.getJmeno_prijmeni());
            // execute the preparedstatement
            count = preparedStmt.execute();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return count;
    }

    public void failInsertLogicReseration(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Pro tento termín už nemůžete přidat další rezervaci." + "\n" + "Pro jedno datum a čas mohou být maximálně 2 rezervace.");
        alert.showAndWait();
    }

    public void failInsertLogicReserationDueToDate(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Nelze přidat rezervaci v minulosti!");
        alert.showAndWait();
    }

    public void failInsertLogicReserationSpzExists(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Auto s touto SPZ v databázi již existuje.");
        alert.showAndWait();
    }

    public void failInsertLogicReserationErrorDatabseInsert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Chyba! Problém při vkládání do databáze.");
        alert.showAndWait();
    }

    public void failInsertLogicReserationEmployee(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Pracovník s tímto ID neexistuje.");
        alert.showAndWait();
    }

    public void goToList() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("list.fxml"));

        Stage window = (Stage) listbutton.getScene().getWindow();
        window.setScene(new Scene(root,750,331));


    }

    public boolean searchForEmployee(Connection connection, int idEmployee) throws SQLException {
        String query = "select id from swinz_db_employee where id =" + idEmployee;
        var statement = connection.prepareStatement(query);
        ResultSet r = statement.executeQuery();
        boolean resu = false;
        int count = 0;

        while (r.next()) {
            if(r.getInt("id") == idEmployee){
                count++;
            }
        }

        if(count > 0){
            resu = true;
        } else {
            resu = false;
        }

        return resu;
    }

    public boolean searchForExistingCar(Connection connection, String spzExist) throws SQLException {
        String query = "select id from swinz_db where spz =" + spzExist;
        var statement = connection.prepareStatement(query);
        ResultSet r = statement.executeQuery();
        boolean resu = false;
        int count = 0;

        while (r.next()) {
            if(r.getString("spz") == spzExist){
                count++;
            }
        }

        if(count > 0){
            resu = true;
        } else {
            resu = false;
        }

        return resu;
    }

    public void successAddInfo(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Rezervace Byla přidána!");
        alert.showAndWait();
    }

    public int dbSelectMatchingDates(Connection con, LocalDate datePicked, String timePicked) throws SQLException {
        LocalTime t = LocalTime.parse(timePicked);
        LocalDate d = datePicked;


        String query = "select * from swinz_db";
        var statement = con.prepareStatement(query);
        ResultSet r = statement.executeQuery();
        int resu = 0;
        int count = 0;

        while (r.next()) {
               Time cas = r.getTime("cas");
               Date da = r.getDate("datum");



               //čas z db
               LocalTime time = cas.toLocalTime();
               time = time.minusHours(1);
                //čas z inputu
               //t = t.minusHours(1);

            System.out.println("V databázi:");
            System.out.println(time);
            System.out.println(da);


            System.out.println("zadáno:");
            System.out.println(time);
            System.out.println(d);

            boolean x = time.equals(t);
            boolean y = da.equals(java.sql.Date.valueOf(datePicked));

            if(x && y){
                    count++;
                }

        }

        if(count < 2){
            resu = 1;
        } else {
            resu = 0;
        }



        if(datePicked.isBefore(LocalDate.now())){
            resu = -1;
        }

        return resu;
    }

    public void goToListEmployee() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("list_pracovnici.fxml"));

        Stage window = (Stage) employeebutton.getScene().getWindow();
        window.setScene(new Scene(root,475,331));


    }

    public void test(TextField field){

        field.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            String newText = change.getControlNewText();
            String max5chars = "";



            if(newText.length() == 5){
                max5chars = newText;
            }

            if(newText.length() > 5){
                newText = max5chars;
            }

            return change;
        }));
    }


    // Factory to create Cell of DatePicker
    private Callback<DatePicker, DateCell> getDayCellFactory() {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.getDayOfWeek() == DayOfWeek.SATURDAY //
                                || item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }




    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        datePicker.setDayCellFactory(dayCellFactory);
        test(cas);

    }

}
