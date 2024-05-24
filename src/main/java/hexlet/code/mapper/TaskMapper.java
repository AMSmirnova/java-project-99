package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "assignee.id", ignore = true)
    @Mapping(target = "labels", ignore = true)
    public abstract Task map(TaskCreateDTO taskCreateDTO);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "name", target = "title")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(target = "taskLabelIds", ignore = true)
    public abstract TaskDTO map(Task task);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "assignee.id", ignore = true)
    @Mapping(target = "labels", ignore = true)
    public abstract void update(TaskUpdateDTO data, @MappingTarget Task model);

}
