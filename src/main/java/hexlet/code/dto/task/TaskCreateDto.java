package hexlet.code.dto.task;

import hexlet.code.model.TaskStatuses;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCreateDto {

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    private TaskStatuses taskStatus;

    private int index;
    private String description;
    private User assignee;

}
