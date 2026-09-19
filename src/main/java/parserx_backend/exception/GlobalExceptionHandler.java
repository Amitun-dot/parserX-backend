package parserx_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AIServiceException.class)
    public ResponseEntity<Map<String, Object>> handleAIServiceException(
            AIServiceException e
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("error", "AI_SERVICE_ERROR");
        response.put(
                "message",
                e.getMessage() != null
                        ? e.getMessage()
                        : "AI service is temporarily unavailable. Please try again later."
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException e
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("error", "REQUEST_ERROR");
        response.put(
                "message",
                e.getMessage() != null
                        ? e.getMessage()
                        : "Something went wrong. Please try again."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


}
