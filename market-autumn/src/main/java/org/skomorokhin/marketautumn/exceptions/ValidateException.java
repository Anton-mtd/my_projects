package org.skomorokhin.marketautumn.exceptions;

import java.util.List;
import java.util.stream.Collectors;

public class ValidateException extends RuntimeException {

    public ValidateException(List<String> errors) {
        super(errors.stream().collect(Collectors.joining(",")));
    }
}
