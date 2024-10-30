package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;

import java.util.List;

public class MessageService {
     private MessageDAO messageDAO;
     private AccountDAO accountDAO;

     public MessageService(){
        messageDAO = new MessageDAO();
     }

     public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
     }
     //Create message
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
        if (!messageDAO.accountIDExists(message.getPosted_by())){
            throw new IllegalArgumentException();
        }
        return messageDAO.insertMessage(message);
    }

    //Get All messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMesssages();
    }

    //Get message by id
    public Message getMessageById(int getMessageById){
        return messageDAO.getMessageById(getMessageById);
    }
}
