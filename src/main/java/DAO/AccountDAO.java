package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public Account createAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO Account(username, password) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedAccountId = generatedKeys.getInt(1);
                    account.setAccount_id(generatedAccountId);
                    return account;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account findAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM Account WHERE username = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int accountId = resultSet.getInt("account_id");
                    String password = resultSet.getString("password");
                    return new Account(accountId, username, password);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
