import java.sql.*;
public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String tmp = "INSERT INTO benutzer (full_name, email_id, password) VALUES ('a', 'a', 'a')";
        TimeSchedulerDB db = new TimeSchedulerDB("jdbc:mysql://localhost:3306/time_scheduler", "root", "Passwort123");
        db.insertRow(tmp);
        db.getTable("SELECT * FROM benutzer");
    }

}
