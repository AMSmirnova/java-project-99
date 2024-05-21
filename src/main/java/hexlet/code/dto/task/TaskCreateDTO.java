package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TaskCreateDTO {

    @NotNull
    @Size(min = 1)
    private String title;

    @NotNull
    private String status;

    private int index;
    private String content;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    @JsonProperty("taskLabelIds")
    private List<Long> labelsIds;

}
