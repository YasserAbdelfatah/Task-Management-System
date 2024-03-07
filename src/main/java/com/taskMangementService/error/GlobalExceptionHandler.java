package com.taskMangementService.error;

import com.taskMangementService.error.dto.ExceptionResponse;
import com.taskMangementService.error.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 400.
     */
    @ExceptionHandler({UserAlreadyExistException.class, InvalidDataException.class})
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistException(RuntimeException ex) {
        return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * 401.
     */
    @ExceptionHandler({UnauthorizedException.class, BadCredentialsException.class})
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(RuntimeException ex) {
        return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 403.
     */
    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(ForbiddenException ex) {
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN);
    }

    /**
     * 404
     */
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(NotFoundException ex) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * 409.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalExceptions(RuntimeException ex) {
        return buildResponseEntity(ex, HttpStatus.CONFLICT);
    }

    /**
     * 500.
     * Throw exception for any other unpredicted reason.
     * Avoid modification of this method to make problem visible in logs.
     * Don't delete this method to avoid descendants declare it and catch 'any error'.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleOtherExceptions(Exception ex) throws Exception {
        throw ex;
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(Exception ex, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(new ExceptionResponse(ex.getMessage(), httpStatus.name(), LocalDateTime.now()));
    }
}
