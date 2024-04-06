package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
