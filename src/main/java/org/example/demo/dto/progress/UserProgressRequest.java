package org.example.demo.dto.progress;

public record UserProgressRequest(
    Long episodeId,
    Integer watchedDuration, // in seconds
    Boolean isCompleted // optional: mark as fully watched
) {
}
