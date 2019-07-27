package sd.tweets.repository;

import sd.config.JdbcConfig;
import sd.tweets.model.Tweet;
import sd.users.model.User;
import sd.users.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TweetReposiotory {
    private static final String ALL = "select * from tweets";
    private static final String ADD_TWEET = "insert into tweets (message, photo, userId, data) values (?,?,?,?)";
    private static final String DELETE_TWEET = "delete from tweets where data = ? and userId = ?";

    public void addTweet(Tweet tweet, Integer userId) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(ADD_TWEET)) {
            ps.setString(1, tweet.getMessage());
            ps.setString(2, tweet.getImage());
            ps.setInt(3, userId);
            ps.setString(4, dateFormat.format(date));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tweet> findAll() {
        UserRepository userRepository = new UserRepository();
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ALL);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            List<Tweet> tweets = new ArrayList<>();
            while (resultSet.next()) {
                Tweet tweet = new Tweet();
                tweet.setMessage(resultSet.getString("message"));
                tweet.setImage(resultSet.getString("photo"));
                tweet.setDate(resultSet.getString("data"));
                tweet.setUser(userRepository.getUserById(resultSet.getInt("userId")));
                tweets.add(tweet);
            }
            return tweets;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean deleteTweet(String data, Integer userId) {
        Connection connection = JdbcConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TWEET)) {
            preparedStatement.setString(1, data);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
