package sample.dbutils;

import java.sql.*;
import java.util.ArrayList;

public class DbConnect {
    public Connection con = null;

    public DbConnect() {
        this.con = connectToLocalhost();
    }

    public Connection connectToLocalhost() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/swinz?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String user = "root";
            String pass = "";

            this.con = DriverManager.getConnection(url, user, pass);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }


}
