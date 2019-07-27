package sd.config.users.repository;

import sd.config.JdbcConfig;
import sd.config.users.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private static final String IS_EXISTING_USER = "select password from users where login = ?";
    private static final String USER_BY_ID = "select * from users where usersId = ?";
    private static final String USER_ID = "select usersId from users where login = ?";

    public boolean exists(User user) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(IS_EXISTING_USER)) {
            ps.setString(1, user.getLogin());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return user.getPassword().equals(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;


    }

    public User getUserById(Integer id) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(USER_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet resultset = ps.executeQuery()) {
                User user = new User();
                while (resultset.next()) {
                    user.setLogin(resultset.getString("login"));
                    user.setPassword(resultset.getString("password"));
                }
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;


    }

    public Integer getUserId(String login) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(USER_ID)) {
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("User dont exist");

    }
}
