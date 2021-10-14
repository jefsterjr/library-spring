package org.edu.springmicroservice.config.validation.exception;

public class BookIsAlreadyBorrowedException extends BusinessRuleException{

    public BookIsAlreadyBorrowedException() {
        super("Book is already borrowed!");
    }
}
