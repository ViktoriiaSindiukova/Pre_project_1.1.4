package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS user (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), last_name VARCHAR(255), age TINYINT)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (name, last_name, age) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                preparedStatement.executeUpdate();
                System.out.println("User с именем - " + name + " добавлен в базу данных");
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id = ?")) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> listUser = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                listUser.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return listUser;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
