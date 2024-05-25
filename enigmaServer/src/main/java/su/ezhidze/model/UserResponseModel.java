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

    private String image;

    private String publicKey;

    public UserResponseModel(final User user) {
        id = user.getId();
        nickname = user.getNickname();
        phoneNumber = user.getPhoneNumber();
        image = user.getImage();
        publicKey = user.getPublicKey();
    }
}
