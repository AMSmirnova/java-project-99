package hexlet.code.controller;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.service.UserService;
import hexlet.code.util.UserUtils;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
public class UserController {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserService userService;

    @Operation(description = "Get list all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List all users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) })
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> index() {
        var users = userService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    @Operation(description = "Find user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "User with that id not found",
                    content = @Content) })
    public UserDTO show(@PathVariable Long id) {
        return userService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    @Operation(description = "Create user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid user data",
                    content = @Content) })
    public UserDTO create(
            @Parameter(description = "Data to save")
            @Valid @RequestBody UserCreateDTO userCreateDto) {
        return userService.create(userCreateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{id}")
    @Operation(description = "Update user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid user data",
                    content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found")})
    public UserDTO update(@Valid @RequestBody UserUpdateDTO userUpdateDto, @PathVariable Long id) {
        return userService.update(userUpdateDto, id);
    }

    @DeleteMapping(path = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    @Operation(summary = "Delete user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted", content = @Content),
        @ApiResponse(responseCode = "405", description = "Operation not possible", content = @Content)
    })
    public void destroy(@PathVariable Long id) {
        userService.delete(id);
    }
}
