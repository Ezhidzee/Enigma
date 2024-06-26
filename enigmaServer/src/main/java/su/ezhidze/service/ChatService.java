package su.ezhidze.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.ezhidze.entity.*;
import su.ezhidze.exception.RecordNotFoundException;
import su.ezhidze.model.ChatModel;
import su.ezhidze.repository.ChatRepository;
import su.ezhidze.repository.MessageRepository;
import su.ezhidze.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public ChatModel addNewChat() {
        return new ChatModel(chatRepository.save(new Chat()));
    }

    public Chat getChatById(Integer id) {
        return chatRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Chat not found"));
    }

    public ArrayList<ChatModel> getChats() {
        return new ArrayList<>(((Collection<Chat>) chatRepository.findAll()).stream().map(ChatModel::new).toList());
    }

    public ChatModel joinUser(Integer chatId, Integer userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RecordNotFoundException("Chat not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("User not found"));

        user.getChats().add(chat);
        userRepository.save(user);

        chat.getUsers().add(user);

        return new ChatModel(chatRepository.save(chat));
    }

    public ChatModel deleteUser(Integer chatId, Integer userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RecordNotFoundException("Chat not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("User not found"));

        chat.getUsers().remove(user);

        return new ChatModel(chatRepository.save(chat));
    }


    public ChatModel addMessage(Integer chatId, final Message message) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RecordNotFoundException("Chat not found"));

        chat.getMessages().add(message);

        return new ChatModel(chatRepository.save(chat));
    }

    public ChatModel deleteMessage(Integer chatId, Integer messageId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RecordNotFoundException("Chat not found"));
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RecordNotFoundException("Message not found"));

        chat.getMessages().remove(message);

        return new ChatModel(chatRepository.save(chat));
    }

    public List<User> deleteChat(Integer id) {
        Chat chat = getChatById(id);
        List<User> users = chat.getUsers();
        for (User user : users) {
            for (int i = 0; i < user.getChats().size(); i++) {
                if (Objects.equals(user.getChats().get(i).getId(), id)) {
                    user.getChats().remove(i);
                    break;
                }
            }
            userRepository.save(user);
        }
        chat.getMessages().clear();
        chatRepository.save(chat);
        List<Message> messages = messageRepository.findMessagesByChat(chat).orElseThrow(() -> new RecordNotFoundException("Messages not found"));
        messageRepository.deleteAll(messages);

        chatRepository.delete(chat);
        return users;
    }
}
