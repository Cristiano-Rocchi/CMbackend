package pizzamafia.CMbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //==== 401 UNAUTHORIZED ====
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public  ErrorsPayload handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsPayload(ex.getMessage());
    }
    // === 404 NOT FOUND ===
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsPayload handleAccessDenied(AccessDeniedException ex) {
        ErrorsPayload errors = new ErrorsPayload("NON hai i permessi per accedere");
        errors.addError("timestamp", LocalDateTime.now().toString());
        return errors;
    }
    // === 400 BAD REQUEST ===
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayload handleBadRequest(BadRequestException ex) {
        return new ErrorsPayload(ex.getMessage());
    }

    // === 400 VALIDATION ERROR ===
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayload handleValidation(MethodArgumentNotValidException ex) {
        ErrorsPayload errors = new ErrorsPayload("Errore di validazione.");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.addError(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }

    // === 400 NUMBER FORMAT ===
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayload handleNumberFormat(NumberFormatException ex) {
        return new ErrorsPayload("Errore di formattazione numerica: " + ex.getMessage());
    }

    // === 500 INTERNAL SERVER ERROR ===
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsPayload handleGeneric(Exception ex) {
        return new ErrorsPayload("Errore interno del server: " + ex.getMessage());
    }

}
