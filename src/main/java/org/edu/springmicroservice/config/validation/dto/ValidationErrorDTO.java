package org.edu.springmicroservice.config.validation.dto;

public class ValidationErrorDTO extends ErrorDTO {

    private final String field;

    public ValidationErrorDTO(String msg, String field) {
        super(msg);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
