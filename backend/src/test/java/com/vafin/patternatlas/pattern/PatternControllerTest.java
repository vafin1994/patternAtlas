package com.vafin.patternatlas.pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatternController.class)
class PatternControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatternService patternService;

    @Test
    void getById_returns200_whenPatternFound() throws Exception {
        PatternResponse response = new PatternResponse(1, "Singleton", PatternCategory.CREATIONAL, "shortDesc", "Description", "imageUrl");
        when(patternService.findById(1)).thenReturn(response);

        mockMvc.perform(get("/api/patterns/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Singleton"))
                .andExpect(jsonPath("$.category").value("CREATIONAL"));
    }

    @Test
    void getById_returns404_whenPatternNotFound() throws Exception {
        when(patternService.findById(999)).thenThrow(new PatternNotFoundException(999));
        mockMvc.perform(get("/api/patterns/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @Test
    void save_returns201_whenRequestIsValid() throws Exception {
        PatternResponse response = new PatternResponse(1, "Singleton", PatternCategory.CREATIONAL, "short", "description", null);
        when(patternService.create(any(PatternRequest.class))).thenReturn(response);
        mockMvc.perform(post("/api/patterns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Singleton",
                                  "category": "CREATIONAL",
                                  "shortDescription": "short",
                                  "description": "description"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Singleton"));

    }

    @Test
    void save_returns400_whenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/patterns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "category": "CREATIONAL",
                                  "shortDescription": "short",
                                  "description": "description"
                                }
                                """))
                .andExpect(status().isBadRequest());
        verify(patternService, never()).create(any());
    }

    @Test
    void save_returns409_whenNameAlreadyExists() throws Exception {
        PatternRequest patternRequest = new PatternRequest("Adapter", PatternCategory.BEHAVIORAL, "shortDesc", "description");
        when(patternService.create(patternRequest)).thenThrow(new DataIntegrityViolationException("duplicate"));

        mockMvc.perform(post("/api/patterns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Adapter",
                                  "category": "BEHAVIORAL",
                                  "shortDescription": "shortDesc",
                                  "description": "description"
                                }
                                """))
                .andExpect(status().isConflict());

    }
}
