package su.ezhidze.model;

import lombok.Getter;
import lombok.Setter;
import su.ezhidze.entity.Message;

@Getter
@Setter
public class MessageResponseModel {

    private Integer id;

    private Integer chatId;

    private String messageText;

    private String senderSubject;

    public MessageResponseModel(final Message message) {
        id = message.getId();
        chatId = message.getChat().getId();
        messageText = message.getMessageText();
        senderSubject = message.getSenderSubject();
    }
}
