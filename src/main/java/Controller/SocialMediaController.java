package Controller;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    private final AccountService accountService = new AccountService();
    private final MessageService messageService = new MessageService();
    private final ObjectMapper mapper = new ObjectMapper();

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageTextHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserIdHandler);
        return app;
    }

    private void registerHandler(Context ctx) throws Exception {
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);
        if (registeredAccount != null) {
            ctx.json(registeredAccount);
        } else {
            ctx.status(400);
        }
    }

    private void loginHandler(Context ctx) throws Exception {
        Account credentials = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.validateLogin(credentials);
        if (loggedInAccount != null) {
            ctx.json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws Exception {
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.json(createdMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200).result("");
        }
    }

    private void deleteMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage != null) {
            ctx.status(200).json(deletedMessage);
        } else {
            ctx.status(200);
        }
    }
    

    private void updateMessageTextHandler(Context ctx) throws Exception {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message newMessageDetails = mapper.readValue(ctx.body(), Message.class);
                if (newMessageDetails.getMessage_text().length() > 200) {
            ctx.status(400);
            return;
        }
        Message updatedMessage = messageService.updateMessageText(messageId, newMessageDetails.getMessage_text());
        if (updatedMessage != null ) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }
    private void getAllMessagesByUserIdHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByUserId(accountId));
    }
}
