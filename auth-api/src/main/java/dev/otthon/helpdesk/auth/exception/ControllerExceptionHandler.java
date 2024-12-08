package dev.otthon.helpdesk.auth.exception;

import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handlerEntityNotFoundException(UsernameNotFoundException exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("User not found");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errorSourceClass", exception.getStackTrace()[0].getClassName());
        problemDetail.setProperty("errorSourceMethod", exception.getStackTrace()[0].getMethodName());

        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Payload field validation failed");
        problemDetail.setTitle("Invalid input");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setInstance(URI.create(request.getDescription(true)));

        Map<String, List<String>> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(
                                FieldError::getDefaultMessage, Collectors.toList()
                        )
                ));
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
    }

}
