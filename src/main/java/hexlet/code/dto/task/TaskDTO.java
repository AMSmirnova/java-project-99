package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TaskDTO {

    private Long id;
    private String title;
    private String status;
    private int index;
    private String content;

    @JsonProperty("assignee_id")
    private Long assigneeId;
    @JsonProperty("taskLabelIds")
    private List<Long> taskLabelIds;

    private Date createdAt;
}
