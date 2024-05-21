package hexlet.code.dto.task;

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
    private Long assigneeId;
    private List<Long> labelIds;

    private Date createdAt;
}
