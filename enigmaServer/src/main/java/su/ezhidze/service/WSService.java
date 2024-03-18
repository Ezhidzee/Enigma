package su.ezhidze.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import su.ezhidze.entity.Chat;
import su.ezhidze.entity.User;
import su.ezhidze.model.InputMessageModel;

import java.util.Objects;

@Service
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(final InputMessageModel message) {
        Chat chat = chatService.getChatById(message.getChatId());
        for (User user : chat.getUsers()) {
            if (user.getIsOnline() && !Objects.equals(user.getNickname(), message.getSenderSubject())) {
                messagingTemplate.convertAndSendToUser(user.getUUID(), "/topic/private-messages", message);
            }
        }
        Integer newMessageId = messageService.addNewMessage(message, chatService).getId();
        chatService.addMessage(chat.getId(), messageService.getMessageById(newMessageId));
    }
}
