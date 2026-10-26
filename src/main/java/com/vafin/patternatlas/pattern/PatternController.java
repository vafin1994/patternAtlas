package com.vafin.patternatlas.pattern;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
