package su.ezhidze.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.ezhidze.entity.Message;
import su.ezhidze.exception.RecordNotFoundException;
import su.ezhidze.model.InputMessageModel;
import su.ezhidze.model.MessageResponseModel;
import su.ezhidze.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public MessageResponseModel addNewMessage(InputMessageModel inputMessageModel, ChatService chatService) {
        Message m = new Message(inputMessageModel, chatService);
        return new MessageResponseModel(messageRepository.save(m));
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Message not found"));
    }
}
