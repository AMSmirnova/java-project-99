package hexlet.code.controller;

import hexlet.code.dto.user.UserCreateDto;
import hexlet.code.dto.user.UserDto;
import hexlet.code.dto.user.UserUpdateDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.UserUtils;
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
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserUtils userUtils;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> index() {
        var users = userRepository.findAll();
        var result = users.stream()
                .map(u -> userMapper.map(u))
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public UserDto show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var userDto = userMapper.map(user);
        return userDto;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public UserDto create(@Valid @RequestBody UserCreateDto userCreateDto) {
        var user = userMapper.map(userCreateDto);
        userRepository.save(user);
        var userDto = userMapper.map(user);
        return userDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{id}")
    public UserDto update(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.update(userUpdateDto, user);
        userRepository.save(user);
        var userDto = userMapper.map(user);
        return userDto;
    }

    @DeleteMapping(path = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public void destroy(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
