package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class AccountDAO {
    //Make account
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account(username, password) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int genarated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(genarated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }
    //Check if username is taken
    public boolean usernameExists(String username) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return false;
    }
    //Login Account
    public Account loginAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account acc = new Account(rs.getInt("account_id"),rs.getString("username"),rs.getString("password"));
                return acc;
            }
        }catch(SQLException e){
            System.out.print(e.getMessage());
        }
        return null;
    }
    
}
