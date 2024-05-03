package hexlet.code.dto.task;

import hexlet.code.model.TaskStatuses;
import hexlet.code.model.User;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class TaskUpdateDto {

    private JsonNullable<String> name;
    private JsonNullable<TaskStatuses> taskStatus;
    private JsonNullable<Integer> index;
    private JsonNullable<String> description;
    private JsonNullable<User> assignee;

}
