package com.NextStepEdu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiException {

    // ✅ 1) Validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {
        List<Map<String, String>> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("field", err.getField());
                    m.put("message", err.getDefaultMessage());
                    return m;
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", "Validation failed",
                "fields", errors
        ));
    }

    // ✅ 2) ResponseStatusException (your custom 404/400/etc)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatus(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                "status", e.getStatusCode().value(),
                "error", e.getStatusCode().toString(),
                "message", e.getReason()
        ));
    }

    // ✅ 3) Multipart parsing issues (THIS is your main error)
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipart(MultipartException e) {
        e.printStackTrace(); // IMPORTANT: shows real root cause in console

        Throwable root = e.getRootCause(); // may be null
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", e.getMessage(),
                "rootCause", root != null ? root.getMessage() : null,
                "hint", "Check Postman: do NOT manually set Content-Type; also check upload temp folder/size limits."
        ));
    }

    // ✅ 4) File too large
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(Map.of(
                "status", 413,
                "error", "Payload Too Large",
                "message", "File too large. Increase spring.servlet.multipart.max-file-size / max-request-size."
        ));
    }

    // ✅ 5) RuntimeException (important if MultipartException gets wrapped)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "error", "Internal Server Error",
                "message", e.getMessage()
        ));
    }

    // ✅ 6) Fallback for everything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAny(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "error", "Internal Server Error",
                "message", e.getMessage()
        ));
    }
}

