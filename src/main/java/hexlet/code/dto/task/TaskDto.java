package hexlet.code.dto.task;

import hexlet.code.model.TaskStatuses;
import hexlet.code.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDto {

    private Long id;
    private String name;
    private TaskStatuses taskStatus;
    private int index;
    private String description;
    private User assignee;

    private LocalDate createdAt;
}
