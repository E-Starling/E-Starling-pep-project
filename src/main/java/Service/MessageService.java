package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
     private MessageDAO messageDAO;

     public MessageService(){
        messageDAO = new MessageDAO();
     }

     public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
     }
     //Create message

     //- The creation of the message will be successful if and only if the message_text is not blank, 
//is not over 255 characters, and posted_by refers to a real, existing user. If successful, 
//the response body should contain a JSON of the message, including its message_id. 
//The response status should be 200, which is the default. The new message should be persisted to the database.
//- If the creation of the message is not successful, the response status should be 400. (Client error)
     public Message addMessage(Message message){
         //check blank
         if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()){
            throw new IllegalArgumentException();
        }
        //check over 255 char
        if (message.getMessage_text().length() > 255){
            throw new IllegalArgumentException();
        }
        //check posted_by is real
        if (message.getPosted_by() <= 0) {
            throw new IllegalArgumentException();
        }

        return messageDAO.insertMessage(message);
     }

}
