package hexlet.code.dto.taskStatus;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusesUpdateDto {
    private JsonNullable<String> name;
    private JsonNullable<String> slug;
}
