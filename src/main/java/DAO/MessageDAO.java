package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    

   // ## 3: Our API should be able to process the creation of new messages.

//As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
//The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.

//- The creation of the message will be successful if and only if the message_text is not blank, 
//is not over 255 characters, and posted_by refers to a real, existing user. If successful, 
//the response body should contain a JSON of the message, including its message_id. 
//The response status should be 200, which is the default. The new message should be persisted to the database.
//- If the creation of the message is not successful, the response status should be 400. (Client error)

    //Create new message
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message(posted_by,message_text,time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text()); 
            preparedStatement.setLong(3, message.getTime_posted_epoch()); 
            preparedStatement.executeUpdate(); 
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
