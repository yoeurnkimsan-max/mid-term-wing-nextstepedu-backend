package com.NextStepEdu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.ServletRequestBindingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiException {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        Map<?, ?> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
                List<FieldError> fieldErrors = new ArrayList<>();
                e.getFieldErrors()
                                .forEach(fieldError -> fieldErrors
                                                .add(FieldError.builder()
                                                                .field(fieldError.getField())
                                                                .detail(fieldError.getDefaultMessage())
                                                                .build()));
                return Map.of("errors", ErrorResponse.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .reason(fieldErrors)
                                .build());

        }

        @ExceptionHandler(ResponseStatusException.class)
        ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
                System.out.println(e.getMessage());
                ErrorResponse<String> errorResponse = ErrorResponse.<String>builder()
                                .code(e.getStatusCode().value())
                                .reason(e.getReason())
                                .build();
                return ResponseEntity
                                .status(e.getStatusCode())
                                .body(Map.of("error", errorResponse));
        }

        @ExceptionHandler(MultipartException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        ResponseEntity<Map<String, Object>> handleMultipartException(MultipartException e) {
                System.err.println("Multipart parsing error: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                Map.of(
                                                "status", HttpStatus.BAD_REQUEST.value(),
                                                "error", "Bad Request",
                                                "message", "File upload failed: " + e.getMessage(),
                                                "details",
                                                e.getCause() != null ? e.getCause().getMessage() : "Unknown error"));
        }

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
                return ResponseEntity.status(413).body(
                                Map.of(
                                                "status", 413,
                                                "error", "Payload Too Large",
                                                "message", "File size exceeds maximum allowed size"));
        }

        @ExceptionHandler(ServletRequestBindingException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        ResponseEntity<Map<String, Object>> handleServletRequestBindingException(ServletRequestBindingException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                Map.of(
                                                "status", HttpStatus.BAD_REQUEST.value(),
                                                "error", "Bad Request",
                                                "message", "Invalid request parameters: " + e.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                Map.of(
                                                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                "error", "Internal Server Error",
                                                "message", "An unexpected error occurred"));
        }

}

