package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateDto;
import hexlet.code.dto.label.LabelDto;
import hexlet.code.dto.label.LabelUpdateDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;

import hexlet.code.repository.LabelRepository;
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
public class LabelController {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    @GetMapping("/labels")
    public ResponseEntity<List<LabelDto>> index() {
        var labels = labelRepository.findAll();
        var result = labels.stream()
                .map(l -> labelMapper.map(l))
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/labels/{id}")
    public LabelDto show(@PathVariable Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("label not found"));
        var labelDto = labelMapper.map(label);
        return labelDto;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/labels")
    public LabelDto create(@Valid @RequestBody LabelCreateDto labelCreateDto) {
        var label = labelMapper.map(labelCreateDto);
        labelRepository.save(label);
        var labelDto = labelMapper.map(label);
        return labelDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/labels/{id}")
    public LabelDto update(@Valid @RequestBody LabelUpdateDto labelUpdateDto,
                           @PathVariable Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("label not found"));
        labelMapper.update(labelUpdateDto, label);
        labelRepository.save(label);
        var labelDto = labelMapper.map(label);
        return labelDto;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/labels/{id}")
    public void destroy(@PathVariable Long id) {
        labelRepository.deleteById(id);
    }
}
