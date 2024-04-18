package su.ezhidze.model;

import lombok.Getter;
import lombok.Setter;
import su.ezhidze.entity.User;

import java.util.Map;

@Getter
@Setter
public class UserResponseModel {

    private Integer id;

    private String nickname;

    private String phoneNumber;

    private String publicKey;

    public UserResponseModel(final User user) {
        id = user.getId();
        nickname = user.getNickname();
        phoneNumber = user.getPhoneNumber();
        publicKey = user.getPublicKey();
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "id", id.toString(),
                "nickname", nickname,
                "phoneNumber", phoneNumber,
                "publicKey", publicKey);
    }
}
