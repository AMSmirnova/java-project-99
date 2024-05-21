package hexlet.code.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserCreateDTO {

    private String firstName;
    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 3)
    private String password;

}