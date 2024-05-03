package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDto;
import hexlet.code.dto.task.TaskDto;
import hexlet.code.dto.task.TaskUpdateDto;
import hexlet.code.model.Task;
import org.mapstruct.Mapper;
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
    public abstract Task map(TaskCreateDto dto);
    public abstract TaskDto map(Task model);
    public abstract void update(TaskUpdateDto dto, @MappingTarget Task model);
}
