import java.sql.*;

public class TimeSchedulerDB {
    private final String dbUrl;
    private final String user;
    private final String pass;
    static final String QUERY = "SELECT * FROM benutzer";

    //Constructor
    public TimeSchedulerDB(String dbUrl, String user, String pass) {
        this.dbUrl = dbUrl;
        this.user = user;
        this.pass = pass;
    }
    // function to insert into DB table
    public void insertRow(String query) {
        try {
            Connection conn = DriverManager.getConnection(dbUrl, user, pass);
            Statement stmt = conn.createStatement();
            stmt.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTable(String query) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbUrl, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                System.out.println(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
