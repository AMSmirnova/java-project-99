package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskSpecification taskSpecification;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;


    public List<TaskDTO> getAll(TaskParamsDTO taskParamsDTO) {
        Specification<Task> specification = taskSpecification.build(taskParamsDTO);
        var tasks = repository.findAll(specification);
        var result = tasks.stream()
                .map(taskMapper::map)
                .toList();
        return result;
    }

    public TaskDTO create(TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);

        var assigneeId = taskData.getAssigneeId();

        if (assigneeId != null) {
            var assignee = userRepository.findById(assigneeId).orElse(null);
            task.setAssignee(assignee);
        }

        var statusSlug = taskData.getStatus();
        var taskStatus = taskStatusRepository.findBySlug(statusSlug).orElse(null);
        task.setTaskStatus(taskStatus);

        var taskLabelIds = taskData.getTaskLabelIds();
        var labels = new HashSet<>(labelRepository.findByIdIn(taskLabelIds).orElse(new HashSet<>()));
        task.setLabels(labels);

        repository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    public TaskDTO findById(Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    public TaskDTO update(TaskUpdateDTO taskData, Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskMapper.update(taskData, task);

        setTaskUpdates(taskData, task);

        repository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    private void setTaskUpdates(TaskUpdateDTO taskUpdates, Task task) {
        var assigneeId = taskUpdates.getAssigneeId();
        if (assigneeId != null) {
            var assignee = assigneeId.get() == null
                    ? null
                    : userRepository.findById(assigneeId.get()).orElseThrow();
            task.setAssignee(assignee);
        }

        var statusSlug = taskUpdates.getStatus();
        if (statusSlug != null) {
            var taskStatus = statusSlug.get() == null
                    ? null
                    : taskStatusRepository.findBySlug(statusSlug.get()).orElseThrow();
            task.setTaskStatus(taskStatus);
        }
        var taskLabelIds = taskUpdates.getTaskLabelIds();
        if (taskLabelIds != null) {
            var taskLabels = new HashSet<>(labelRepository
                    .findByIdIn(taskLabelIds.get())
                    .orElse(new HashSet<>()));
            task.setLabels(taskLabels);
        }
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

