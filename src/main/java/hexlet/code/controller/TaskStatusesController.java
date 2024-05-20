package hexlet.code.controller;

import hexlet.code.dto.taskStatus.TaskStatusCreateDto;
import hexlet.code.dto.taskStatus.TaskStatusDto;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskStatusesController {
    @Autowired
    private TaskStatusMapper taskStatusesMapper;

    @Autowired
    private TaskStatusRepository taskStatusesRepository;

    @GetMapping("/task_statuses")
    public ResponseEntity<List<TaskStatusDto>> index() {
        var taskStatuses = taskStatusesRepository.findAll();
        var result = taskStatuses.stream()
                .map(t -> taskStatusesMapper.map(t))
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskStatuses.size()))
                .body(result);
    }

    @GetMapping("/task_statuses/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDto show(@PathVariable Long id) {
        var taskStatus = taskStatusesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found"));
        var taskStatusDto = taskStatusesMapper.map(taskStatus);
        return taskStatusDto;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/task_statuses")
    public TaskStatusDto create(@Valid @RequestBody TaskStatusCreateDto taskStatusCreateDto) {
        var taskStatus = taskStatusesMapper.map(taskStatusCreateDto);
        taskStatusesRepository.save(taskStatus);
        var taskStatusDto = taskStatusesMapper.map(taskStatus);
        return taskStatusDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/task_statuses/{id}")
    public TaskStatusDto update(@Valid @RequestBody TaskStatusUpdateDto taskStatusesUpdateDto,
                                @PathVariable Long id) {
        var taskStatus = taskStatusesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found"));
        taskStatusesMapper.update(taskStatusesUpdateDto, taskStatus);
        taskStatusesRepository.save(taskStatus);
        var taskStatusDto = taskStatusesMapper.map(taskStatus);
        return taskStatusDto;
    }

    @DeleteMapping("/task_statuses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        taskStatusesRepository.deleteById(id);
    }
}
