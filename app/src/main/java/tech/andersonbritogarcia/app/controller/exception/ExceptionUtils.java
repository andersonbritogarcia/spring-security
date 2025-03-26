package tech.andersonbritogarcia.app.controller.exception;

import java.util.Objects;

public class ExceptionUtils {

    public static Throwable getRootCause(Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return null;
        }

        Throwable cause;
        Throwable rootCause;
        for (cause = throwable; Objects.nonNull(rootCause = cause.getCause()); cause = rootCause) {
        }

        return cause;
    }
}