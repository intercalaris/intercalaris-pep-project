package Service;

import DAO.MessageDAO;
import Model.Message;

import java.sql.SQLException;
import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }
        try {
            return messageDAO.insertMessage(message);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Message> getAllMessages() {
        try {
            return messageDAO.getAllMessages();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Message getMessageById(int messageId) {
        try {
            return messageDAO.getMessageById(messageId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean deleteMessage(int messageId) {
        try {
            return messageDAO.deleteMessage(messageId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Message updateMessageText(int messageId, String newText) {
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            return null;
        }
        try {
            boolean success = messageDAO.updateMessageText(messageId, newText);
            if (success) {
                return messageDAO.getMessageById(messageId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessagesByUserId(int userId) {
        try {
            return messageDAO.getAllMessagesByUserId(userId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
