package su.ezhidze.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import su.ezhidze.model.InputMessageModel;

@Entity
@Getter
@Setter
public class InputMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String senderSubject;

    private Integer chatId;

    private String messageText;

    public InputMessage() {
    }

    public InputMessage(String senderSubject, Integer chatId, String messageText) {
        this.senderSubject = senderSubject;
        this.chatId = chatId;
        this.messageText = messageText;
    }

    public InputMessage(final InputMessageModel inputMessage) {
        senderSubject = inputMessage.getSenderSubject();
        chatId = inputMessage.getChatId();
        messageText = inputMessage.getMessageText();
    }
}
