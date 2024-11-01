package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

public class MessageDAO {
    //Create new message
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text()); 
            preparedStatement.setLong(3, message.getTime_posted_epoch()); 
            preparedStatement.executeUpdate(); 
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
             if(pkeyResultSet.next()){          
                int genarated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(genarated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());           
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Check if account id exists
    public boolean accountIDExists(int accountID) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Get all messages
    public List<Message> getAllMesssages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
           String sql = "SELECT * FROM message";
          PreparedStatement preparedStatement = connection.prepareStatement(sql);
           ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messages.add(message);
            }
        }catch(SQLException e){
          System.out.print(e.getMessage());
        }
        return messages;
    }

    //Get message by id
    public Message getMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Delete message by Id
    public Message deleteMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                deletedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
            String deleteSql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, messageId);
            deleteStatement.executeQuery(); 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deletedMessage;
    }
    
    //Update message By ID
    public Message updateMessageById(Message message){
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = null;
        try {
           
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, message.getMessage_id());
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                String updateSQL = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
                updateStatement.setString(1, message.getMessage_text());
                updateStatement.setInt(2, message.getMessage_id());
                updateStatement.executeUpdate();
                
                updatedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    message.getMessage_text(),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return updatedMessage;
    }

 //Retrieve all message by user id
    public List<Message> getAllMesssagesByAccount(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}