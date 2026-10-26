package com.vafin.patternatlas.pattern;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatternRequest(
        @NotBlank @Size(min=3, max=255) String name,
        @NotNull PatternCategory category,
        @Size(max=255) String shortDescription,
        @Size(max=10000) String description
        ) { }
