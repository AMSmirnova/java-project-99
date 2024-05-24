package hexlet.code.controller;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private TaskService taskService;

    @GetMapping("/tasks")
    @Operation(description = "Get list all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List all tasks",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)) })
    })
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO params) {
        var tasks = taskService.getAll(params);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tasks/{id}")
    @Operation(description = "Find task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the task",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Task with that id not found",
                    content = @Content) })
    public TaskDTO show(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tasks")
    @Operation(description = "Create task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid task data",
                    content = @Content) })
    public TaskDTO create(
            @Parameter(description = "Data to save")
            @Valid @RequestBody TaskCreateDTO taskCreateDto) {
        return taskService.create(taskCreateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/tasks/{id}")
    @Operation(description = "Update task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid task data",
                    content = @Content),
        @ApiResponse(responseCode = "404", description = "Task not found")})
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO taskUpdateDto, @PathVariable Long id) {
        return taskService.update(taskUpdateDto, id);
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task deleted", content = @Content),
        @ApiResponse(responseCode = "405", description = "Operation not possible", content = @Content)
    })
    public void destroy(@PathVariable Long id) {
        taskService.delete(id);
    }
}
