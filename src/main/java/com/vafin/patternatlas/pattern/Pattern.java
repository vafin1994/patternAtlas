package com.vafin.patternatlas.pattern;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PatternCategory category;
    private String shortDescription;
    @Column(columnDefinition = "TEXT")
    private String description;

    public static Pattern from(PatternRequest patternRequest) {
        Pattern pattern = new Pattern();
        pattern.setName(patternRequest.name());
        pattern.setCategory(patternRequest.category());
        pattern.setDescription(patternRequest.description());
        pattern.setShortDescription(patternRequest.shortDescription());
        return pattern;
    }

    public void apply(PatternRequest request) {
        this.name = request.name();
        this.category = request.category();
        this.shortDescription = request.shortDescription();
        this.description = request.description();
    }
}
