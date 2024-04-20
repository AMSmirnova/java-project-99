package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusesUpdateDto {
    private JsonNullable<String> name;
    private JsonNullable<String> slug;
}
