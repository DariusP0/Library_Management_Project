package repository.user;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;
import static database.Constants.Tables.USER_ROLE;
import static java.util.Collections.singletonList;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            String fetchAllUsersSql = "SELECT * FROM " + USER;
            ResultSet usersResultSet = statement.executeQuery(fetchAllUsersSql);

            while (usersResultSet.next()) {
                User user = new UserBuilder()
                        .setId(usersResultSet.getLong("id"))
                        .setUsername(usersResultSet.getString("username"))
                        .setPassword(usersResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(usersResultSet.getLong("id")))
                        .build();

                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public User findById(Long id) {
        try {
            Statement statement = connection.createStatement();

            String fetchUserByIdSql = "SELECT * FROM " + USER + " WHERE id=" + id;
            ResultSet userResultSet = statement.executeQuery(fetchUserByIdSql);

            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if user with the specified ID is not found
    }
    @Override
    public boolean removeById(Long id) {
        try {
            // Use a PreparedStatement for parameterized queries to avoid SQL injection
            String deleteSql = "DELETE FROM " + USER + " WHERE id=?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);

            // Set the user ID for the WHERE clause
            deleteStatement.setLong(1, id);

            // Execute the delete statement
            int rowsAffected = deleteStatement.executeUpdate();

            return (rowsAffected!=0);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // SQL Injection Attacks should not work after fixing functions
    // Be careful that the last character in sql injection payload is an empty space
    // alexandru.ghiurutan95@gmail.com' and 1=1; --
    // ' or username LIKE '%admin%'; --

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next())
            {
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean updateEmployee(Long id, String newUsername, String newPassword) {
        try {
            // Use a PreparedStatement for parameterized queries to avoid SQL injection
            String updateSql = "UPDATE " + USER + " SET username=?, password=? WHERE id=?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);

            // Set the new values for username and password
            updateStatement.setString(1, newUsername);
            updateStatement.setString(2, newPassword);
            // Specify the user ID for the WHERE clause
            updateStatement.setLong(3, id);

            // Execute the update statement
            int rowsAffected = updateStatement.executeUpdate();
            return (rowsAffected != 1);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean updateUserRole(Long id, Long role){
        try {
            // Use a PreparedStatement for parameterized queries to avoid SQL injection
            String updateSql = "UPDATE " + USER_ROLE + " SET role_id=? WHERE user_id=?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);

            // Set the new values for username and password
            updateStatement.setLong(1, role);
            updateStatement.setLong(2, id);
            // Specify the user ID for the WHERE clause

            // Execute the update statement
            int rowsAffected = updateStatement.executeUpdate();
            return (rowsAffected != 1);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + email + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}