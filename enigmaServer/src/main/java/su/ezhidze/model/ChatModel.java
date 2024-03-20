package su.ezhidze.model;

import lombok.Getter;
import lombok.Setter;
import su.ezhidze.entity.Chat;

import java.util.List;

@Getter
@Setter
public class ChatModel {

    private Integer id;

    private List<UserResponseModel> users;

    private List<MessageResponseModel> messages;

    private Integer g;

    private Integer p;

    private Integer A;

    private Integer B;

    public ChatModel(final Chat chat) {
        id = chat.getId();
        users = chat.getUsers().stream().map(UserResponseModel::new).toList();
        messages = chat.getMessages().stream().map(MessageResponseModel::new).toList();
        g = chat.getG();
        p = chat.getP();
        A = chat.getA();
        B = chat.getB();
    }
}
