package hexlet.code.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private Date createdAt;
    private Date updatedAt;
}
