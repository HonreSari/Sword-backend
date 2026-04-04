package org.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProgress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // ✅ Correct
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "episode_id", nullable = false) // ✅ Correct
  private Episode episode;

  // ❌ DO NOT ADD series_id here! Episode already links to Series

  @Column(name = "watched_duration")
  private Integer watchedDuration;

  @Column(name = "is_completed")
  private boolean isCompleted = false;

  @Column(name = "last_watched_at")
  private LocalDateTime lastWatchedAt = LocalDateTime.now();

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
