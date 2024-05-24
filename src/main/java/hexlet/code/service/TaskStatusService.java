package hexlet.code.service;

import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        var taskStatuses = repository.findAll();
        var result = taskStatuses.stream()
                .map(taskStatusMapper::map)
                .toList();
        return result;
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusData) {
        var taskStatus = taskStatusMapper.map(taskStatusData);
        repository.save(taskStatus);
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;
    }

    public TaskStatusDTO findById(Long id) {
        var taskStatus = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;
    }

    public TaskStatusDTO findBySlug(String slug) {
        var taskStatus = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO taskStatusData, Long id) {
        var taskStatus = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
        taskStatusMapper.update(taskStatusData, taskStatus);
        repository.save(taskStatus);
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
