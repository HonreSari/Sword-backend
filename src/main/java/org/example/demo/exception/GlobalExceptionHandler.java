package org.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(
      ResourceNotFoundException ex, HttpServletRequest request) {

    ErrorResponse error = ErrorResponse.of(
        404, "Not Found", ex.getMessage(), request.getRequestURI());
    return ResponseEntity.status(404).body(error);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(
      IllegalArgumentException ex, HttpServletRequest request) {

    ErrorResponse error = ErrorResponse.of(
        400, "Bad Request", ex.getMessage(), request.getRequestURI());
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler({
      MethodArgumentTypeMismatchException.class,
      MissingServletRequestParameterException.class,
      MethodArgumentNotValidException.class
  })
  public ResponseEntity<ErrorResponse> handleRequestValidation(
      Exception ex, HttpServletRequest request) {

    ErrorResponse error = ErrorResponse.of(
        400, "Bad Request", ex.getMessage(), request.getRequestURI());
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(ErrorResponseException.class)
  public ResponseEntity<ErrorResponse> handleFrameworkErrors(
      ErrorResponseException ex, HttpServletRequest request) {

    HttpStatusCode status = ex.getStatusCode();
    String message = ex.getBody() != null && ex.getBody().getDetail() != null
        ? ex.getBody().getDetail()
        : ex.getMessage();

    ErrorResponse error = ErrorResponse.of(
        status.value(), status.toString(), message, request.getRequestURI());
    return ResponseEntity.status(status).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(
      Exception ex, HttpServletRequest request) {

    ErrorResponse error = ErrorResponse.of(
        500, "Internal Server Error", ex.getMessage(), request.getRequestURI());
    return ResponseEntity.status(500).body(error);
  }
}
