package org.edu.springmicroservice.config.validation.exception;

public class BookNotFoundException extends BusinessRuleException {

    public BookNotFoundException() {
        super("Book not found");
    }
}
