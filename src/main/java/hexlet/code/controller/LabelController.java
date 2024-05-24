package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import hexlet.code.dto.label.LabelUpdateDTO;

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
@Tag(name = "Label controller")
@RestController
@RequestMapping("/api")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping("/labels")
    @Operation(description = "Get list all labels")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List all labels",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabelDTO.class)) })
    })
    public ResponseEntity<List<LabelDTO>> index() {
        var labels = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/labels/{id}")
    @Operation(description = "Find label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the label",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabelDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Label with that id not found",
                    content = @Content) })
    public LabelDTO show(@PathVariable Long id) {
        return labelService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/labels")
    @Operation(description = "Create label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Label created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabelDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid label data",
                    content = @Content) })
    public LabelDTO create(
            @Parameter(description = "Data to save")
            @Valid @RequestBody LabelCreateDTO labelCreateDto) {
        return labelService.create(labelCreateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/labels/{id}")
    @Operation(description = "Update label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabelDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid label data",
                    content = @Content),
        @ApiResponse(responseCode = "404", description = "Label not found")})
    public LabelDTO update(@Valid @RequestBody LabelUpdateDTO labelUpdateDto,
                           @PathVariable Long id) {
        return labelService.update(labelUpdateDto, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/labels/{id}")
    @Operation(summary = "Delete label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Label deleted", content = @Content),
        @ApiResponse(responseCode = "405", description = "Operation not possible", content = @Content)
    })
    public void destroy(@PathVariable Long id) {
        labelService.delete(id);
    }
}
