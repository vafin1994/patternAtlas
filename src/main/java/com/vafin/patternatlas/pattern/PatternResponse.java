package com.vafin.patternatlas.pattern;

public record PatternResponse(
        Integer id,
        String name,
        PatternCategory category,
        String shortDescription,
        String description
) {

    public static PatternResponse from(Pattern pattern) {
        return new PatternResponse(pattern.getId(), pattern.getName(), pattern.getCategory(), pattern.getShortDescription(), pattern.getDescription());
    }
}
