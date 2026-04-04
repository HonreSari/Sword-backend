package org.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.example.demo.dto.PageResponseDTO;
import org.example.demo.dto.series.SeriesDetailDTO;
import org.example.demo.dto.series.SeriesListDTO;
import org.example.demo.service.SeriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/series")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeriesController {

  private final SeriesService seriesService;

  @GetMapping
  public ResponseEntity<PageResponseDTO<SeriesListDTO>> listSeries(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "12") int size) {

    // ✅ Service now returns PageResponseDTO directly
    return ResponseEntity.ok(seriesService.getAllSeries(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SeriesDetailDTO> getSeries(@PathVariable Long id) {
    return ResponseEntity.ok(seriesService.getSeriesById(id));
  }
}
