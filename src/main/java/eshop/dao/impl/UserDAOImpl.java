package eshop.dao.impl;

import org.springframework.stereotype.Repository;
import eshop.utils.DBUtils;
import eshop.dao.UserDAO;
import eshop.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Repository
public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class.getName());

    private static final String QUERY_SELECT_FROM_USER = "SELECT * FROM user";
    private static final String QUERY_SELECT_FROM_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?";
    private static final String QUERY_INSERT_INTO_USER = "INSERT INTO user(login, password) VALUES (?,?)";

    @Override
    public void save(User user) {
        try (PreparedStatement statement = getPreparedStatement(QUERY_INSERT_INTO_USER)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Request to save new user has failed. Error message: {}", e.getMessage());
        }
        if (user == null || user.getLogin().isEmpty()) {
            throw new IllegalArgumentException("User is null or name is empty");
        }
        if (user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is null or name is empty");
        }
    }

    @Override
    public User getByLogin(String login) {
        User user = new User();
        try (PreparedStatement statement = getPreparedStatement(QUERY_SELECT_FROM_USER_BY_LOGIN)) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setId(rs.getLong("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            LOGGER.error("Request to get user by login has failed. Error message: {}", e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = getPreparedStatement(QUERY_SELECT_FROM_USER);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                User user = new User(rs.getLong("id"),
                        rs.getString("login"),
                        rs.getString("password"));

                users.add(user);
            }
        } catch (SQLException e) {
            LOGGER.error("Request to get all users has failed. Error message: {}", e.getMessage());
        }

        return users;
    }

    private PreparedStatement getPreparedStatement(String query) throws SQLException {
        return DBUtils.getConnection().prepareStatement(query);
    }
}
