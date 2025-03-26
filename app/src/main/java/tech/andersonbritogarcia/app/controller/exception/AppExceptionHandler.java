package tech.andersonbritogarcia.app.controller.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    public static String MSG_REQUEST_BODY_INVALID = "The request body is invalid. Check the syntax error.";
    public static String MSG_PROPERTY_NOT_RECOGNIZED = "The property '%s' is not recognized for the model '%s'.";
    public static String MSG_RESOURCE_NOT_FOUND = "The resource %s you tried to access does not exist.";
    public static String MSG_INVALID_FIELDS = "One or more fields are invalid. Fill them in correctly and try again.";
    public static String MSG_INTERNAL_ERROR =
            "An unexpected internal system error occurred. Try again, and if the problem persists, contact the system administrator.";
    public static String MSG_PROPERTY_INVALID_TYPE =
            "The property received the value '%s', which is an invalid type. Correct it to a value compatible with type %s.";
    public static String MSG_PARAMETER_INVALID_TYPE =
            "The URL parameter '%s' received the value '%s', which is an invalid type. Correct it to a value compatible with type %s.";
    public static String MSG_PARAMETER_ENUM_INVALID_TYPE =
            "The URL parameter '%s' received the value '%s', which is an invalid type. Correct it to a value among the possible ones (%s).";
    public static String MSG_INVALID_REQUEST_METHOD = "The method '%s' is not compatible for the request.";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode statusCode,
                                                             WebRequest request) {
        if (Objects.isNull(body)) {
            body = problemBuild(HttpStatus.valueOf(statusCode.value()));
        } else if (body instanceof String bodyString) {
            body = problemBuild(HttpStatus.valueOf(statusCode.value()), bodyString);
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private Problem problemBuild(HttpStatus status) {
        return problemBuild(status, status.getReasonPhrase());
    }

    private Problem problemBuild(HttpStatus status, String title) {
        return new Problem(status.value(), title, MSG_INTERNAL_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var fieldErrors = ex.getBindingResult().getFieldErrors();
        var problemFields = fieldErrors.stream().map(FieldError::new).toList();
        var problem = problemBuild(HttpStatus.BAD_REQUEST, ProblemType.INVALID_DATA, MSG_INVALID_FIELDS, problemFields);

        return super.handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private Problem problemBuild(HttpStatusCode statusCode, ProblemType problemType, String detail) {
        return problemBuild(statusCode, problemType, detail, null);
    }

    private Problem problemBuild(HttpStatusCode statusCode, ProblemType problemType, String detail, List<FieldError> fields) {
        return new Problem(statusCode.value(), problemType.getUri(), problemType.getTitle(), detail, fields);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatusCode status,
                                                                   WebRequest request) {
        var detail = String.format(MSG_RESOURCE_NOT_FOUND, ex.getRequestURL());
        var problem = problemBuild(status, ProblemType.RESOURCE_NOT_FOUND, detail);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
                                                                    HttpHeaders headers,
                                                                    HttpStatusCode status,
                                                                    WebRequest request) {
        var detail = String.format(MSG_RESOURCE_NOT_FOUND, ex.getResourcePath());
        var problem = problemBuild(status, ProblemType.RESOURCE_NOT_FOUND, detail);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        var rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException invalidEx) {
            return handleInvalidFormat(invalidEx, headers, status, request);
        }

        if (rootCause instanceof PropertyBindingException propertyEx) {
            return handlePropertyBinding(propertyEx, headers, status, request);
        }

        var problem = problemBuild(HttpStatus.BAD_REQUEST, ProblemType.INCOMPREHENSIBLE_MESSAGE, MSG_REQUEST_BODY_INVALID);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        var problem = problemBuild(HttpStatus.METHOD_NOT_ALLOWED,
                                   ProblemType.METHOD_NOT_ALLOWED,
                                   String.format(MSG_INVALID_REQUEST_METHOD, ex.getMethod()));
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    public ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                      HttpHeaders headers,
                                                      HttpStatusCode status,
                                                      WebRequest request) {

        var path = joinPatch(ex);
        var detail = String.format(MSG_PROPERTY_INVALID_TYPE, path, ex.getValue(), ex.getTargetType().getSimpleName());
        var problem = problemBuild(HttpStatus.BAD_REQUEST, ProblemType.INCOMPREHENSIBLE_MESSAGE, detail);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPatch(MismatchedInputException ex) {
        return ex.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
    }

    public ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                        HttpHeaders headers,
                                                        HttpStatusCode status,
                                                        WebRequest request) {

        var path = joinPatch(ex);
        var detail = String.format(MSG_PROPERTY_NOT_RECOGNIZED, path, ex.getReferringClass());
        var problem = problemBuild(HttpStatus.BAD_REQUEST, ProblemType.INCOMPREHENSIBLE_MESSAGE, detail);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        var problem = problemBuild(HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.SYSTEM_FAILURE, MSG_INTERNAL_ERROR);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex, WebRequest request) {
        var problem = problemBuild(HttpStatus.BAD_REQUEST, ProblemType.BUSINESS_RULE_VIOLATION, ex.getMessage());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        var problem = problemBuild(HttpStatus.NOT_FOUND, ProblemType.RESOURCE_NOT_FOUND, ex.getMessage());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        var problem = problemBuild(HttpStatus.UNAUTHORIZED, ProblemType.UNAUTHORIZED, ex.getMessage());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        var problem = problemBuild(HttpStatus.FORBIDDEN, ProblemType.ACCESS_DENIED, ex.getMessage());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        var problemFields = ex.getConstraintViolations().stream().map(FieldError::new).toList();

        var problem = problemBuild(HttpStatus.BAD_REQUEST, ProblemType.INVALID_DATA, MSG_INVALID_FIELDS, problemFields);
        return super.handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
