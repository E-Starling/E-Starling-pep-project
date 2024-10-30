package Controller;

import javax.sound.midi.ControllerEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postAccountLogin);
        app.post("/messages", this::postMessage);
        app.get("/messages", this::getAllMessages);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    //make account
    private void postAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        context.status(200).json(mapper.writeValueAsString(addedAccount));
        } catch (IllegalArgumentException e){
            context.status(400).json("");
        }
    }

    //login account
    private void postAccountLogin(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        try{
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account loggedinAccount = accountService.loginAccount(account);

            if (loggedinAccount == null){
                ctx.status(401).json("");
            } else {
                ctx.status(200).json(mapper.writeValueAsString(loggedinAccount));
            }
            
        } catch (IllegalArgumentException e){
            ctx.status(400).json("");
        }
    }

    //Create message
    private void postMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        try{
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message addedMessage = messageService.addMessage(message);
            ctx.status(200).json(mapper.writeValueAsString(addedMessage));
        } catch (IllegalArgumentException e){
            ctx.status(400).json("");
        }
    }

    //Get all messages
    public void getAllMessages(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
}