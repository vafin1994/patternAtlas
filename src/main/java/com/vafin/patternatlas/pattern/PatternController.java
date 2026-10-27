package com.vafin.patternatlas.pattern;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*") // TODO  set proxy in angular
@RequiredArgsConstructor
@RequestMapping("/api/patterns")
public class PatternController {

    private final PatternService patternService;

    @GetMapping()
    public List<PatternResponse> getAll(@RequestParam(required = false) PatternCategory category) {
        if(category == null) {
        return patternService.findAll();
        } else {
            return patternService.findByCategory(category);
        }
    }

    @GetMapping("/{id}")
    public PatternResponse findById(@PathVariable Integer id) {
        return patternService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatternResponse save(@Valid @RequestBody PatternRequest patternRequest) {
        return patternService.create(patternRequest);
    }

    @PutMapping("/{id}")
    public PatternResponse update(@PathVariable Integer id, @Valid @RequestBody PatternRequest patternRequest) {
        return patternService.update(id, patternRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        patternService.deleteById(id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Integer id) {
        Resource image = patternService.loadImage(id);
        MediaType type = MediaTypeFactory.getMediaType(image).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return  ResponseEntity.ok().contentType(type).body(image);
    }

    @PostMapping("/{id}/image")
    public PatternResponse uploadImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        return patternService.uploadImage(id, file);
    }
}
