package su.ezhidze.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputMessageModel {

    @NotNull(message = "type cannot be null")
    private String type;

    @NotNull(message = "senderNickname cannot be null")
    @Size(min = 1, message = "senderNickname should not be empty")
    @Size(max = 100, message = "senderNickname length should not be greater than 100 symbols")
    private String senderSubject;

    @NotNull(message = "chatId cannot be null")
    private Integer chatId;

    @NotNull(message = "data cannot be null")
    @Size(min = 1, message = "data should not be empty")
    private String data;

    public InputMessageModel(String type, String senderSubject, Integer chatId, String data) {
        this.type = type;
        this.senderSubject = senderSubject;
        this.chatId = chatId;
        this.data = data;
    }
}
