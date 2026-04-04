package org.example.demo.repository;

import org.example.demo.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

  // ✅ Find progress by user + episode (for upsert logic)
  Optional<UserProgress> findByUserIdAndEpisodeId(Long userId, Long episodeId);

  // ✅ Get all progress for a user (for "Continue Watching")
  @Query("SELECT up FROM UserProgress up " +
      "JOIN FETCH up.episode e " +
      "JOIN FETCH e.season s " +
      "JOIN FETCH s.series " +
      "WHERE up.user.id = :userId " +
      "ORDER BY up.lastWatchedAt DESC")
  List<UserProgress> findByUserIdWithDetails(@Param("userId") Long userId);

  // ✅ Get completed episodes for a user
  List<UserProgress> findByUserIdAndIsCompletedTrue(Long userId);
}
