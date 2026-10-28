package com.vafin.patternatlas.pattern;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatternRepository extends JpaRepository<Pattern, Integer> {
    List<Pattern> findByCategory(PatternCategory category);
}
