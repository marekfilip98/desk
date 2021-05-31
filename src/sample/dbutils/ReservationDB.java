package sample.dbutils;

import sample.entities.Reservation;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class ReservationDB {

    public void fetchAllDateAndTimeThatAlreadyExists(Connection con, ArrayList<Reservation> reservations) throws SQLException {
        String query = "select * from swinz_db order by datum";
        var statement = con.prepareStatement(query);
        ResultSet r = statement.executeQuery();

        while (r.next()){

            int id = r.getInt("id");
            Date datum = r.getDate("datum");
            Time cas = r.getTime("cas");
            String spz = r.getString("spz");
            String zavada = r.getString("zavada");
            String jmeno_prijmeni = r.getString("jmeno_prijmeni");


            reservations.add(new Reservation(id,datum,cas.toLocalTime(),spz,zavada,jmeno_prijmeni));

    }
    }



    public static int removeRowFromDb(Connection con, int id) throws SQLException {
        String SQL = "DELETE FROM swinz_db WHERE id = ? ";
        PreparedStatement pstmt = null;

        pstmt = con.prepareStatement(SQL);


        pstmt.setInt(1, id);
        int out = pstmt.executeUpdate();

        return out;
    }
}
