package com.vafin.patternatlas.pattern;

public record PatternResponse(
        Integer id,
        String name,
        PatternCategory category,
        String shortDescription,
        String description,
        String imageUrl
) {

    public static PatternResponse from(Pattern pattern) {
        String imageUrl = null;
        if (pattern.getImagePath() != null) {
            imageUrl = "/api/patterns/" + pattern.getId() + "/image";
        }
        return new PatternResponse(pattern.getId(), pattern.getName(), pattern.getCategory(), pattern.getShortDescription(), pattern.getDescription(), imageUrl);
    }
}
