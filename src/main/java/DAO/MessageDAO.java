package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    private final Connection connection = ConnectionUtil.getConnection();

    // Retrieve all messages
    public List<Message> getAllMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        }
        return messages;
    }

    // Retrieve a specific message by its ID
    public Message getMessageById(int messageId) throws SQLException {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, messageId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                }
            }
        }
        return null;
    }

    // Insert a new message
    public Message insertMessage(Message message) throws SQLException {
        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setMessage_id(rs.getInt(1));
                }
            }
        }
        return message;
    }

    // Update a message's text by its ID
    public boolean updateMessageText(int messageId, String newText) throws SQLException {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newText);
            preparedStatement.setInt(2, messageId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    // Delete a message by its ID
    public boolean deleteMessage(int messageId) throws SQLException {
        String sql = "DELETE FROM message WHERE message_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, messageId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    // Retrieve all messages posted by a particular user
    public List<Message> getAllMessagesByUserId(int userId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            preparedStatement.setInt(1, userId);
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        }
        return messages;
    }

}
