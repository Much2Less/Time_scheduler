package Object;

/**
 * A class to store the login credentials for the mysql database
 */
public class DBData {
    final static String DBUser = "root";
    final static String DBPassword = "Prabin2468";

    public DBData() {}

    public static String getDBUser() {
        return DBUser;
    }

    public static String getDBPassword() {
        return DBPassword;
    }
}
