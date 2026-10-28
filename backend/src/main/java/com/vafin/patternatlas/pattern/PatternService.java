package com.vafin.patternatlas.pattern;

import com.vafin.patternatlas.storage.StorageFileNotFoundException;
import com.vafin.patternatlas.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatternService {
    private final PatternRepository patternRepository;
    private final StorageService storageService;

    public List<PatternResponse> findAll() {
        List<Pattern> patterns = patternRepository.findAll();
        List<PatternResponse> patternResponses = new ArrayList<>();
        for (Pattern pattern : patterns) {
            PatternResponse patternResponse = PatternResponse.from(pattern);
            patternResponses.add(patternResponse);
        }
        return patternResponses;
    }

    public List<PatternResponse> findByCategory(PatternCategory category) {
        return patternRepository.findByCategory(category).stream()
                .map(PatternResponse::from)
                .toList();
    }

    public PatternResponse findById(Integer id) {
        Pattern foundPattern = patternRepository.findById(id).orElseThrow(() -> new PatternNotFoundException(id));
        return PatternResponse.from(foundPattern);
    }

    public PatternResponse create(PatternRequest patternRequest) {
        Pattern pattern = Pattern.from(patternRequest);
        Pattern savedPattern = patternRepository.save(pattern);
        return PatternResponse.from(savedPattern);
    }

    @Transactional
    public PatternResponse update(Integer id, PatternRequest patternRequest) {
        Pattern pattern = patternRepository.findById(id).orElseThrow(() -> new PatternNotFoundException(id));
        pattern.apply(patternRequest);
        return PatternResponse.from(pattern);
    }

    @Transactional
    public void deleteById(Integer id) {
        Pattern patternToDelete = patternRepository.findById(id).orElseThrow(() -> new PatternNotFoundException(id));
        patternRepository.delete(patternToDelete);
    }

    @Transactional
    public PatternResponse uploadImage(Integer id, MultipartFile file) {
        Pattern pattern = patternRepository.findById(id).orElseThrow(() -> new PatternNotFoundException(id));
        String imagePath = storageService.store(file);
        pattern.setImagePath(imagePath);
        return PatternResponse.from(pattern);
    }

    public Resource loadImage(Integer id) {
        Pattern pattern = patternRepository.findById(id).orElseThrow(() -> new PatternNotFoundException(id));
        if (pattern.getImagePath() == null) {
            throw new StorageFileNotFoundException("Pattern " + id + " has no image");
        }
        return storageService.load(pattern.getImagePath());
    }
}
