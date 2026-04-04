package org.example.demo.dto.progress;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public record UserProgressResponse(
    Long id,
    Long episodeId,
    String episodeTitle,
    Long seriesId,
    String seriesTitle,
    String coverImageUrl,
    Integer watchedDuration,
    Integer totalDuration,
    Boolean isCompleted,
    LocalDateTime lastWatchedAt) {
  // Factory method will be in Service layer
}
