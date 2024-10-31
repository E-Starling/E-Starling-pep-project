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

    //## 7: Our API should be able to update a message text identified by a message ID.

//As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}.
 //The request body should contain a new message_text values to replace the message identified by message_id.
  //The request body can not be guaranteed to contain any other information.

//- The update of a message should be successful if and only if the message id already exists
 //and the new message_text is not blank and is not over 255 characters. If the update is successful,
  //the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch),
   //and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
//- If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    //Update message By ID
    public Message updateMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = null;
        try {
           
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                String updateSQL = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, message.getMessage_text());
                updateStatement.setInt(2, message.getMessage_id());
                updateStatement.executeUpdate();
                updatedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return updatedMessage;
    }
}