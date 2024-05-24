package hexlet.code.controller;

import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.service.TaskStatusService;
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
public class TaskStatusesController {

    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping("/task_statuses")
    @Operation(description = "Get list all statuses")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List all statuses",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatusDTO.class)) })
    })
    public ResponseEntity<List<TaskStatusDTO>> index() {
        var taskStatuses = taskStatusService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskStatuses.size()))
                .body(taskStatuses);
    }

    @GetMapping("/task_statuses/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Find status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the status",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatusDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Status with that id not found",
                    content = @Content) })
    public TaskStatusDTO show(@PathVariable Long id) {
        return taskStatusService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/task_statuses")
    @Operation(description = "Create status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Status created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatusDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid status data",
                    content = @Content) })
    public TaskStatusDTO create(
            @Parameter(description = "Data to save")
            @Valid @RequestBody TaskStatusCreateDTO taskStatusCreateDto) {
        return taskStatusService.create(taskStatusCreateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/task_statuses/{id}")
    @Operation(description = "Update status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatusDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid status data",
                    content = @Content),
        @ApiResponse(responseCode = "404", description = "Status not found")})
    public TaskStatusDTO update(@Valid @RequestBody TaskStatusUpdateDTO taskStatusesUpdateDto,
                                @PathVariable Long id) {
        return taskStatusService.update(taskStatusesUpdateDto, id);
    }

    @DeleteMapping("/task_statuses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Status deleted", content = @Content),
        @ApiResponse(responseCode = "405", description = "Operation not possible", content = @Content)
    })
    public void destroy(@PathVariable Long id) {
        taskStatusService.delete(id);
    }
}
