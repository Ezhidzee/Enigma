package su.ezhidze.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationModel {

    @NotNull(message = "nickname cannot be null")
    @Size(min = 1, message = "nickname should not be empty")
    @Size(max = 100, message = "nickname length should not be greater than 100 symbols")
    private String nickname;

    @NotNull(message = "phoneNumber cannot be null")
    @Size(min = 1, message = "phoneNumber should not be empty")
    @Size(max = 100, message = "phoneNumber length should not be greater than 100 symbols")
    private String phoneNumber;

    @NotNull(message = "password cannot be null")
    @Pattern(regexp = "(?=.*[0-9]).+", message = "A digit must occur at least once in password")
    @Pattern(regexp = "(?=.*[a-z]).+", message = "A lower case letter must occur at least once in password")
    @Pattern(regexp = "(?=.*[A-Z]).+", message = "An upper case letter must occur at least once in password")
    @Pattern(regexp = "(?=.*[@#$%^&+=]).+", message = "A special character(@#$%^&+=) must occur at least once in password")
    @Pattern(regexp = "(?=\\S+$).+", message = "No whitespace allowed in the entire password")
    @Pattern(regexp = ".{7,}.+", message = "password should contain at least 8 characters")
    private String password;
}
