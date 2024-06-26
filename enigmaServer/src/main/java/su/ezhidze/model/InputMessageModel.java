package su.ezhidze.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import su.ezhidze.entity.InputMessage;

@Getter
@Setter
public class InputMessageModel {

    @NotNull(message = "senderNickname cannot be null")
    @Size(min = 1, message = "senderNickname should not be empty")
    @Size(max = 100, message = "senderNickname length should not be greater than 100 symbols")
    private String senderSubject;

    @NotNull(message = "chatId cannot be null")
    private Integer chatId;

    @NotNull(message = "messageText cannot be null")
    @Size(min = 1, message = "messageText should not be empty")
    private String messageText;

    public InputMessageModel(String senderSubject, Integer chatId, String messageText) {
        this.senderSubject = senderSubject;
        this.chatId = chatId;
        this.messageText = messageText;
    }

    public InputMessageModel(InputMessage inputMessage) {
        senderSubject = inputMessage.getSenderSubject();
        chatId = inputMessage.getChatId();
        messageText = inputMessage.getMessageText();
    }
}
