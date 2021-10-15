package org.edu.springmicroservice.config.validation.exception;

public class BookIsAlreadyReturnedException extends BusinessRuleException{

    public BookIsAlreadyReturnedException() {
        super("Book is already returned!");
    }
}
