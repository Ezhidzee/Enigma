package su.ezhidze.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import su.ezhidze.model.UserRegistrationModel;

import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    private String nickname;

    private String phoneNumber;

    private String password;

    private String UUID;

    private Boolean isOnline;

    @Column(columnDefinition = "TEXT")
    private String publicKey;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Chat> chats;

    @OneToMany(fetch = FetchType.EAGER)
    private List<InputMessage> unreadMessages;

    public User() {
        isOnline = false;
    }

    public User(final User user) {
        id = user.getId();
        nickname = user.getNickname();
        phoneNumber = user.getPhoneNumber();
        password = user.getPassword();
        UUID = user.getUUID();
        isOnline = user.getIsOnline();
        chats = user.getChats();
    }

    public User(final UserRegistrationModel userRegistrationModel) {
        nickname = userRegistrationModel.getNickname();
        phoneNumber = userRegistrationModel.getPhoneNumber();
        password = userRegistrationModel.getPassword();
        isOnline = false;
    }
}
