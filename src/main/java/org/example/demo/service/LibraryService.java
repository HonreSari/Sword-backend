package org.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.example.demo.dto.series.SeriesListDTO;
import org.example.demo.entity.Series;
import org.example.demo.entity.User;
import org.example.demo.exception.ResourceNotFoundException;
import org.example.demo.repository.SeriesRepo;
import org.example.demo.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

  private final UserRepository userRepository;
  private final SeriesRepo seriesRepository;

  // ✅ Add series to user's library
  @Transactional
  @CacheEvict(value = "user:library", key = "#username")
  public SeriesListDTO addToLibrary(String username, Long seriesId) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    Series series = seriesRepository.findById(seriesId)
        .orElseThrow(() -> new ResourceNotFoundException("Series", "id", seriesId));

    user.addToLibrary(series);
    userRepository.save(user);

    return SeriesListDTO.fromEntity(series);
  }

  // ✅ Remove series from library
  @Transactional
  @CacheEvict(value = "user:library", key = "#username")
  public void removeFromLibrary(String username, Long seriesId) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    Series series = seriesRepository.findById(seriesId)
        .orElseThrow(() -> new ResourceNotFoundException("Series", "id", seriesId));

    user.removeFromLibrary(series);
    userRepository.save(user);
  }

  // ✅ Get user's library (cached)
  @Cacheable(value = "user:library", key = "#username")
  @Transactional(readOnly = true)
  public List<SeriesListDTO> getLibraryByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    return user.getLibrary().stream()
        .map(SeriesListDTO::fromEntity)
        .toList();
  }

  // ✅ Check if series is in library
  @Transactional(readOnly = true)
  public boolean isInLibrary(String username, Long seriesId) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    return user.getLibrary().stream()
        .anyMatch(s -> s.getId().equals(seriesId));
  }
}
