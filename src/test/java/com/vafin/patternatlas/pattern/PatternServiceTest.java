package com.vafin.patternatlas.pattern;

import com.vafin.patternatlas.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatternServiceTest {

    @Mock
    private PatternRepository patternRepository;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private PatternService patternService;

    @Test
    void findById_returnsPattern_whenItExists() {
        // given
        Pattern pattern = createPattern(1, "Singleton");
        when(patternRepository.findById(1)).thenReturn(Optional.of(pattern));

        // when
        PatternResponse result = patternService.findById(1);

        // then
        assertEquals("Singleton", result.name());
        assertEquals(PatternCategory.CREATIONAL, result.category());
    }

    @Test
    void findById_throwsException_whenPatternNotFound() {
        when(patternRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(PatternNotFoundException.class, () -> patternService.findById(999));
    }

    @Test
    void deleteById_deletesPattern_whenItExists() {
        Pattern pattern = createPattern(1, "Singleton");
        when(patternRepository.findById(1)).thenReturn(Optional.of(pattern));

        patternService.deleteById(1);

        verify(patternRepository).delete(pattern);
    }

    @Test
    void deleteById_throwsException_whenPatternNotFound() {
        when(patternRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(PatternNotFoundException.class, () -> patternService.deleteById(999));
        verify(patternRepository, never()).delete(any());
    }

    @Test
    void update_appliesChanges_whenPatternExists() {
        Pattern pattern = createPattern(1, "Singleton");
        PatternRequest patternRequest = new PatternRequest("Adapter", PatternCategory.BEHAVIORAL, "shortDesc", "description");
        when(patternRepository.findById(1)).thenReturn(Optional.of(pattern));

        PatternResponse patternResponse = patternService.update(1, patternRequest);

        assertEquals("Adapter", patternResponse.name());
        assertEquals(PatternCategory.BEHAVIORAL, patternResponse.category());
        assertEquals("shortDesc", patternResponse.shortDescription());
        assertEquals("description", patternResponse.description());

        assertEquals("Adapter", pattern.getName());
        assertEquals(PatternCategory.BEHAVIORAL, pattern.getCategory());
        assertEquals("shortDesc", pattern.getShortDescription());
        assertEquals("description", pattern.getDescription());
    }

    @Test
    void update_throwsException_whenPatternNotFound() {
        when(patternRepository.findById(999)).thenReturn(Optional.empty());
        PatternRequest patternRequest = new PatternRequest("Adapter", PatternCategory.BEHAVIORAL, "shortDesc", "description");
        assertThrows(PatternNotFoundException.class, () -> patternService.update(999, patternRequest));
    }


    private Pattern createPattern(Integer id, String name) {
        Pattern pattern = new Pattern();
        pattern.setId(id);
        pattern.setName(name);
        pattern.setCategory(PatternCategory.CREATIONAL);
        return pattern;
    }
}
