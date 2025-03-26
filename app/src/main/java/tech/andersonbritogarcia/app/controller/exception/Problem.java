package tech.andersonbritogarcia.app.controller.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem extends ProblemDetail {

    private List<FieldError> fields;

    public Problem(int status, URI type, String title, String detail, URI instance, List<FieldError> fields) {
        this.fields = fields;
        setStatus(status);
        setType(type);
        setTitle(title);
        setDetail(detail);
        setInstance(instance);
    }

    public Problem(int status, String title, String detail) {
        this(status, null, title, detail, null);
    }

    public Problem(int status, URI type, String title, String detail, List<FieldError> fields) {
        setStatus(status);
        setType(type);
        setTitle(title);
        setDetail(detail);
        this.fields = fields;
    }

    public List<FieldError> getFields() {
        return fields;
    }
}
