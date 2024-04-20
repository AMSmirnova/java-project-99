package hexlet.code.mapper;

import hexlet.code.dto.TaskStatusesCreateDto;
import hexlet.code.dto.TaskStatusesDto;
import hexlet.code.dto.TaskStatusesUpdateDto;
import hexlet.code.model.TaskStatuses;
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
public abstract class TaskStatusesMapper {
    public abstract TaskStatuses map(TaskStatusesCreateDto dto);
    public abstract TaskStatusesDto map(TaskStatuses model);
    public abstract void update(TaskStatusesUpdateDto dto, @MappingTarget TaskStatuses model);
}
