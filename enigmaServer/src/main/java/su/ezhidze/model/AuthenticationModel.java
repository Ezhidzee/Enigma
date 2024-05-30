package su.ezhidze.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationModel {

    @NotNull(message = "Nickname cannot be null")
    private String nickname;

    @NotNull(message = "Password cannot be null")
    private String password;

    @NotNull(message = "PublicKey cannot be null")
    private String publicKey;

    public AuthenticationModel(String nickname, String password, String publicKey) {
        this.nickname = nickname;
        this.password = password;
        this.publicKey = publicKey;
    }
}
