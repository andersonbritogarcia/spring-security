package tech.andersonbritogarcia.app.controller.exception;

import java.net.URI;
import java.net.URISyntaxException;

public enum ProblemType {

    INVALID_DATA("/invalid-data", "Invalid data"),
    SYSTEM_FAILURE("/system-failure", "System failure"),
    INVALID_PARAMETER("/invalid-parameter", "Invalid parameter"),
    INCOMPREHENSIBLE_MESSAGE("/incomprehensible-message", "Incomprehensible message"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    METHOD_NOT_ALLOWED("/method-not-allowed", "Method not allowed"),
    BUSINESS_RULE_VIOLATION("/business-rule-violation", "Business rule violation"),
    UNAUTHORIZED("/unauthorized", "Unauthorized"),
    ACCESS_DENIED("/access-denied", "Access denied");

    private final String title;
    private final URI uri;

    ProblemType(String path, String title) {
        this.uri = uriBuilder(path);
        this.title = title;
    }

    private URI uriBuilder(String path) {
        try {
            return new URI("https://andersonbrito.tech" + path);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public URI getUri() {
        return uri;
    }
}