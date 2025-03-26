package tech.andersonbritogarcia.app.controller.exception;

import jakarta.validation.ConstraintViolation;

public class FieldError {

    private final String name;
    private final String message;

    public FieldError(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public FieldError(org.springframework.validation.FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }

    public FieldError(ConstraintViolation constraint) {
        this(String.valueOf(constraint.getPropertyPath()), constraint.getMessage());
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
