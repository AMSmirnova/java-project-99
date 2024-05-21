package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Setter
@Getter
public class TaskUpdateDTO {

    private JsonNullable<String> title;
    private JsonNullable<String> status;
    private JsonNullable<Integer> index;
    private JsonNullable<String> content;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    @JsonProperty("taskLabelIds")
    private JsonNullable<List<Long>> labelIds;

}
