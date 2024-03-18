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
}
