package sd.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConfig{

    private static final String url = "jdbc:mysql://192.168.99.100:3306/tweeter?serverTimezone=UTC&useSSL=false";
    private static final String user = "root";
    private static final String password = "root";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("cannot obtain connection to database", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found", e);
        }
    }

}
