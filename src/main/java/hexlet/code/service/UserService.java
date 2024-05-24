package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserUtils userUtils;

    public List<UserDTO> getAll() {
        var users = repository.findAll();
        var result = users.stream()
                .map(userMapper::map)
                .toList();
        return result;
    }

    public UserDTO create(UserCreateDTO userData) {
        var user = userMapper.map(userData);
        repository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    public UserDTO findById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    public UserDTO update(UserUpdateDTO userData, Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.update(userData, user);
        repository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
