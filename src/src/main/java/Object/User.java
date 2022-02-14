package Object;

/**
 * Class for storing the data of a user
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private int admin;

    /**
     * Creates a new object user
     * @param id primary key of the mysql database
     * @param username username of user
     * @param email email address of user
     * @param password hashed password of the user
     * @param admin admin flag of every user. If 1, then this user is an admin.
     */
    public User(int id, String username, String email, String password, int admin) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }
}
