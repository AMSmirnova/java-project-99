package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;


    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "toTaskStatus")
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toLabels")
    public abstract Task map(TaskCreateDTO taskCreateDTO);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "name", target = "title")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "labels", target = "taskLabelIds", qualifiedByName = "toIds")
    public abstract TaskDTO map(Task task);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "assignee.id", ignore = true)
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "toLabels")
    public abstract void update(TaskUpdateDTO data, @MappingTarget Task model);



    @Named("toTaskStatus")
    public TaskStatus toTaskStatus(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
    }

    @Named("toIds")
    public List<Long> toIds(Set<Label> labels) {
        return labels == null ? null : labels.stream()
                .map(Label::getId)
                .toList();
    }

    @Named("toLabels")
    public Set<Label> toLabels(List<Long> taskLabelIds) {
        return new HashSet<>(labelRepository.findByIdIn(taskLabelIds).orElse(new HashSet<>()));
    }







}
