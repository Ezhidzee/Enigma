package su.ezhidze.entity;

import jakarta.persistence.*;
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

    @Column(length = 10000)
    private String messageText;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public InputMessage() {
    }

    public InputMessage(String senderSubject, Integer chatId, String messageText) {
        this.senderSubject = senderSubject;
        this.chatId = chatId;
        this.messageText = messageText;
    }

    public InputMessage(final InputMessageModel inputMessage, User user) {
        senderSubject = inputMessage.getSenderSubject();
        chatId = inputMessage.getChatId();
        messageText = inputMessage.getMessageText();
        this.user = user;
    }
}
