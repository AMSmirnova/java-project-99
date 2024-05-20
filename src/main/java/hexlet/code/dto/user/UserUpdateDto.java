package hexlet.code.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;


@Getter
@Setter
public class UserUpdateDto {

    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;

    private JsonNullable<String> email;
    private JsonNullable<String> password;

}