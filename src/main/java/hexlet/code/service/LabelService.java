package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository repository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        var labels = repository.findAll();
        var result = labels.stream()
                .map(labelMapper::map)
                .toList();
        return result;
    }

    public LabelDTO create(LabelCreateDTO labelData) {
        var label = labelMapper.map(labelData);
        repository.save(label);
        var labelDTO = labelMapper.map(label);
        return labelDTO;
    }

    public LabelDTO findById(Long id) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));
        var labelDTO = labelMapper.map(label);
        return labelDTO;
    }

    public LabelDTO update(LabelUpdateDTO labelData, Long id) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));
        labelMapper.update(labelData, label);
        repository.save(label);
        var labelDTO = labelMapper.map(label);
        return labelDTO;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
