package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class AccountDAO {
    //TODO Make Account
    //## 1: Our API should be able to process new User registrations.
//As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
//The body will contain a representation of a JSON Account, but will not contain an account_id.
//- The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, 
//and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, 
//including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
//- If the registration is not successful, the response status should be 400. (Client error)
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

    
    


    //TODO Login Account
    //## 2: Our API should be able to process User logins.

    //As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will contain a 
    //JSON representation of an Account, not containing an account_id. In the future, this action may generate a Session 
    //token to allow the user to securely use the site. We will not worry about this for now.
    
    //- The login will be successful if and only if the username and password provided in the request 
    //body JSON match a real account existing on the database. If successful, the response body should contain a JSON of 
    //the account in the response body, including its account_id. The response status should be 200 OK, which is the default.
    //- If the login is not successful, the response status should be 401. (Unauthorized)
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
