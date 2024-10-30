package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    //## 4: Our API should be able to retrieve all messages.

//As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

//- The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
//It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.

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
}