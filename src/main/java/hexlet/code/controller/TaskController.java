package hexlet.code.controller;

import hexlet.code.dto.task.TaskCreateDto;
import hexlet.code.dto.task.TaskDto;
import hexlet.code.dto.task.TaskUpdateDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
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
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> index() {
        var tasks = taskRepository.findAll();
        var result = tasks.stream()
                .map(t -> taskMapper.map(t))
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tasks/{id}")
    public TaskDto show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        var taskDto = taskMapper.map(task);
        return taskDto;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tasks")
    public TaskDto create(@Valid @RequestBody TaskCreateDto taskCreateDto) {
        var task = taskMapper.map(taskCreateDto);
        taskRepository.save(task);
        var taskDto = taskMapper.map(task);
        return taskDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/tasks/{id}")
    public TaskDto update(@Valid @RequestBody TaskUpdateDto taskUpdateDto, @PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskMapper.update(taskUpdateDto, task);
        taskRepository.save(task);
        var taskDto = taskMapper.map(task);
        return taskDto;
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
